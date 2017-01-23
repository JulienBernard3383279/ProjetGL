package fr.ensimag.deca;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.Definition;
import java.util.*;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.ListDeclField;
import fr.ensimag.deca.tree.Deadstore;
import fr.ensimag.deca.tree.ConstantFolding;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl58
 * @date 01/01/2017
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");
    
    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.envTypes = new HashMap<>();
        this.symbols = new SymbolTable();
//        this.regLim=6;
        if (compilerOptions!=null && source !=null) {this.regLim=compilerOptions.getNbRegisters();}
        //Les tests contextuels créent leurs arbres indépendamment de la construction syntaxique correcte
        //pour qu'un échec syntaxique n'impacte pas ces tests.
        //Pour ce faire, ils appellent compiler sans initialiser ses options.
        //D'où la nécessité de ce test.
    }

    private Map<Symbol, Definition> envTypes;
    //symbol table implemented here so tests can use existing symbols
    private SymbolTable symbols;
    
    public Map<Symbol, Definition> getEnvTypes(){
        return envTypes;
    }
    
    public SymbolTable getSymbols() {
        return symbols;
    }
    
    public void initSymbolsAndEnvTypes(SymbolTable table) {
        // create symbols for predefined types
        this.symbols = table;
        Symbol symInt = symbols.create("int");
        Symbol symBool = symbols.create("boolean");
        Symbol symFloat = symbols.create("float");
        Symbol symVoid = symbols.create("void");
        Symbol symObj = symbols.create("Object");
        Symbol symEquals = symbols.create("equals");
        // create definitions from symbols
        TypeDefinition defInt = new TypeDefinition(new IntType(symInt),Location.BUILTIN);
        TypeDefinition defBool = new TypeDefinition(new BooleanType(symBool),Location.BUILTIN);
        TypeDefinition defFloat = new TypeDefinition(new FloatType(symFloat),Location.BUILTIN);
        TypeDefinition defVoid = new TypeDefinition(new VoidType(symVoid),Location.BUILTIN);
        ClassType ctObj = new ClassType(symObj,Location.BUILTIN,null);
        ClassDefinition defObj = ctObj.getDefinition();
        Signature sigEq = new Signature();
        sigEq.add(ctObj);
        MethodDefinition defEq = new MethodDefinition(new BooleanType(symBool),Location.BUILTIN,sigEq,0);
        defEq.setLabel(new Label("equals_Object"));
        // add types to envTypes
        this.envTypes.put(symInt, defInt);
        this.envTypes.put(symBool, defBool);
        this.envTypes.put(symFloat, defFloat);
        this.envTypes.put(symVoid, defVoid);
        try {
            defObj.getMembers().declare(symEquals, defEq);
            defObj.incNumberOfMethods();
        } catch (EnvironmentExp.DoubleDefException d) {
            
        }
        this.envTypes.put(symObj, defObj);
        
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    public void addASMCode(String code) {
        program.addASMCode(code);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
    
    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile=null;
        if(sourceFile.substring(sourceFile.length()-5).equals(".deca")){
            destFile = sourceFile.substring(0,sourceFile.length()-4)+"ass";
        }
        else {
            System.out.println("Exception : Entry file not deca file");
        }
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            //modif
            if ( compilerOptions.getParse() ) {
                AbstractProgram prog = doLexingAndParsing(sourceFile, out);
                prog.decompile(out);
                return false;
            } else if (compilerOptions.getVerif() ) {
                AbstractProgram prog = doLexingAndParsing(sourceFile, out);
                prog.verifyProgram(this);
                //prog.prettyPrint(out);
                return false ;
            }
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());


        prog.verifyProgram(this);
        assert(prog.checkAllDecorations());
        
        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(new ANTLRFileStream(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        this.initSymbolsAndEnvTypes(parser.getTable());
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }
    
    private int regLim = 16 ;
    private final ArrayList<Boolean> stack=new ArrayList<Boolean>();
    private static boolean [] reg=new boolean[16];
    private static boolean [] regUsed=new boolean[16];
    int overFlow=1;
    int maxOverFlow=0;
    
    public void setRegLim(int lim) {
        regLim = lim;
    }
    public void initRegister () {
        Arrays.fill(reg,false);
        Arrays.fill(regUsed,false);
    }/**
     * Fonction critique ne doit jamais être appellé sauf si l'on sait que R2 est libre 
     */
    public void allocR2() {
        if(reg[2]==false) {
            reg[2]=true;
            regUsed[2]=true;
        }
    }
    public void decOverFlow() {
        overFlow--;
    }
    public void incOverFlow() {
        if(overFlow>maxOverFlow) {
            maxOverFlow=overFlow;
        }
        overFlow++;
    }
    public DVal allocRegister () {
        int i;
        DVal regis;
        for(i=3;i<regLim;i++) {//R2 est reservé aux classe offset 
            if(reg[i]==false) {
                reg[i]=true;
                regUsed[i]=true;
                return Register.getR(i);
            }
        }
        for(i=0;i<overFlow-2;i++) {
            if(!stack.get(i)) {
                stack.set(i,true);
                regis= new RegisterOffset(i+1,Register.SP);
                return regis;
            }
        }
        if (overFlow>maxOverFlow ) {
            maxOverFlow=overFlow;
            stack.add(true);
        }
        else {
            stack.set(overFlow-1,true);
        }
        this.addInstruction(new PUSH(Register.R0));
        regis = new RegisterOffset(overFlow,Register.SP);
        overFlow++;
        return regis;
    }
    public int getOverFlow() {
        return overFlow;
    }
    public void freeValue(DVal register) {
        register.free(this);
    }
    public void freeStack(int index) {
        int gap=index;
        if(stack.get(index-1)) {
            stack.set(index-1,false);
            //System.out.println("Je libère l'indexe: "+(index-1));
            while(gap==overFlow-2&&!stack.get(gap)&&overFlow-2>0) {
                overFlow--;
                gap--;
                this.addInstruction(new POP(Register.R0));
            }
        }
    }
    public void resetReg() {
        for(int i=0;i<regLim-1;i++)  {
            reg[i]=false;
        }
        for(int i=0;i<maxOverFlow-1;i++) {
            stack.set(i,false);
        }
        while(overFlow>1) {
            this.addInstruction(new POP(Register.R0));
            overFlow--;
        }
    }
    public void freeRegister(Register register) {
        for(int i=0;i<regLim;i++) {
            if(register.equals(Register.getR(i))) {
                if(reg[i]!=false){
                    reg[i]=false;
                }
                return;
            }
        }
        throw new UnsupportedOperationException("ERROR : free register error");
    }
    public RegisterOffset translate (RegisterOffset register) {
        if(!register.getRegister().equals(Register.SP)) {
            return register;
        }
        return new RegisterOffset(register.getOffset()-overFlow+1,register.getRegister());
    }

    //IfThenElse & While
    
    private int fiCounter = -1;
    private int elseCounter = -1;
    private int beginWhileCounter = -1;
    private int endWhileCounter = -1;
    
    public int getFiCounter() {
        fiCounter++;
        return fiCounter;
    }
    
    public int getElseCounter() {
        elseCounter++;
        return elseCounter;
    }
    
    public int getBeginWhileCounter() {
        beginWhileCounter++;
        return beginWhileCounter;
    }
    
    public int getEndWhileCounter() {
        endWhileCounter++;
        return endWhileCounter;
    }
    
    //DeclVar
    
    private Map<String, VariableDefinition> varMap = new HashMap<>();
    private int varCounter = 0;
    private boolean isInMethod = false;
    public boolean isInMethod() {
        return isInMethod;
    }
    private Extension ext=new Extension();

    public Extension getExtension() {
        return ext;
    }

    public void setExtension(Extension ext) {
        this.ext = ext;
    }   
    public DAddr allocateVar() {
        this.varCounter++;
        if(isInMethod) 
            return new RegisterOffset(this.varCounter+this.methodCounter,Register.LB);
        else 
            return new RegisterOffset(this.varCounter+this.methodCounter,Register.GB);
    }
    public void addVarToTable(String sym,VariableDefinition def) {
        this.varMap.put(sym, def);
    }
    public VariableDefinition getVarData(String sym) {
        return this.varMap.get(sym);
    }
    public boolean varExist(String sym) {
        return this.varMap.containsKey(sym);
    }
    
    public void execute_dead_store(ListDeclVar list_var, ListInst list_inst){
        if(this.compilerOptions.getDead()){
            setExtension(new Deadstore());
            Deadstore dead=(Deadstore) ext;
            dead.store_dec(list_var);
            dead.store_var_inst(list_inst);
            dead.remove_var(list_var);
        }
    }
    
    public void execute_constant_folding(ListDeclVar list_var,ListInst list_inst){
       if(this.compilerOptions.getFolding()){
           setExtension(new ConstantFolding());
       } 
    }
    
    //TSTO
    
    public int argTSTO() {
        if(!isInMethod){
            return maxOverFlow+ varCounter+this.methodCounter+1 /*- this.compilerOptions.getNbRegisters()*/;
        }
        else {
            return maxOverFlow+ varCounter;
        }
    }

    public void addInstructionAtProgramBeginning(Instruction instruction) {
        program.addFirst(instruction);
    }
    public int createFlag() {
        return program.addFlag();
    }
    public void addToFlag(int flag,Instruction instruction) {
        program.addToFlag(flag,instruction);
    }
    public void writeFlag(int flag) {
        program.writeFlagToProgram(flag);
    }
    private int countAndOr = -1;
    
    public int newAndOr() {
        countAndOr++;
        return countAndOr;
    }
    private Label IOOverflow = new Label("io_error");
    public Label getIOLabel() {
        return IOOverflow;
    }
    private Label OVArith = new Label("overflow_error");
    public Label getOVLabel() {
        return OVArith;
    }
    private Label HeapOV = new Label("heap_overflow_error");
    public Label getHeapOV() {
        return HeapOV;
    }
    private Label pilePleineLabel = new Label("pile_pleine");
    public Label getStackOV() {
        return pilePleineLabel;
    }
    private boolean printx=false;
    public void setPrintHex(boolean value) {
        printx=value;
    }
    public boolean getPrintHex() {
        return printx;
    }
    public int getSizeOfConstantStack() {
        return this.varCounter;
    }
    private int methodCounter = 0;
    public int getCurrentMethodNumber() {
        return methodCounter;
    }
    public void addMethod(int offSet) {
        methodCounter += offSet;
    }
    /** Code For Methods Only **/
    private int paramNumber = 0;
    private ListDeclField fields;
    public void resetCompiler(int paramNb,ListDeclField fields) {
        this.isInMethod = true;
        this.varCounter = 0;
        this.overFlow = 1;
        this.maxOverFlow = 1;
        Arrays.fill(reg,false);
        Arrays.fill(regUsed,false);
        stack.clear();
        paramNumber = paramNb;
        varMap.clear();
        this.fields=fields;
        returns=false;
    }
    public boolean hgsdgs(int x) {
        return false;
        
    }
    private int saveRegisterFlag=-1;//use less to create a flag to 0
    public void setSaveRegisterFlag(int i) {
        if(i<0){
            throw new UnsupportedOperationException("Can't Intialize Flag to Negative Number!");   
        }
        saveRegisterFlag=i;
    }
    public int getSaveRegisterFlag() {
        if(saveRegisterFlag<0){
            throw new UnsupportedOperationException("Forget to intialize flag");   
        }
        return saveRegisterFlag;
    }
    public int[] getUsedRegister() {
        int [] returns=new int[16];
        Arrays.fill(returns,-1);
        int j=0;
        for(int i=0;i<regLim;i++) {
            if(regUsed[i]) {
                returns[j]=i;
                j++;
            }
        }
        return returns;
    }
    ClassDefinition currentClass=null;
    public void currentClass(ClassDefinition definition) {
        currentClass=definition;
    }
    public ClassDefinition getClassDefinition() {
        return currentClass;
    }
    private boolean returns=false;
    public boolean hasReturn() {
        return returns;
    }
    public void isNotVoid() {
        returns =true;
    }
    private String methodName;
    public void setMethodName (String name) {
        methodName=name;
    }
    public String getMethodName() {
        return methodName;
    }
    private Label endMethod;//Label vers la fin de la méthode pour les returns 
    public void setEndMethodLabel(Label l) {
        endMethod=l;
    }
    public Label getEndMethodLabel() {
        return endMethod;
    }

    public void newMethodKey() {
        methodKey++;
    }
    int methodKey=0;
    public String getMethodkey() {
        return ""+methodKey;
    }
    /** End of Code for Methods Only**/
}
