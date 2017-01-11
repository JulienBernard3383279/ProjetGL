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
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
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
        // create definitions from symbols
        TypeDefinition defInt = new TypeDefinition(new IntType(symInt),Location.BUILTIN);
        TypeDefinition defBool = new TypeDefinition(new BooleanType(symBool),Location.BUILTIN);
        TypeDefinition defFloat = new TypeDefinition(new FloatType(symFloat),Location.BUILTIN);
        TypeDefinition defVoid = new TypeDefinition(new VoidType(symVoid),Location.BUILTIN);
        ClassDefinition defObj = new ClassDefinition(new ClassType(symObj,Location.BUILTIN,null),Location.BUILTIN,null);
        // add types to envTypes
        this.envTypes.put(symInt, defInt);
        this.envTypes.put(symBool, defBool);
        this.envTypes.put(symFloat, defFloat);
        this.envTypes.put(symVoid, defVoid);
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
        //assert(prog.checkAllDecorations());
        
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
    
    private static int regLim = 15;
    private static int stackLim = 15;
    private static boolean [] stack=new boolean[stackLim];
    private static boolean [] reg=new boolean[15];
    int overFlow=0;
    public static void setRegLim(int lim) {
        regLim = lim;
    }
    public void initRegister () {
        Arrays.fill(reg,false);
        Arrays.fill(stack,false);
    }
    public DVal allocRegister () {
        int i=0;
        for(i=2;i<regLim;i++) {
            if(reg[i]==false) {
                reg[i]=true;
                return Register.getR(i);
            }
        }
        for(i=0;i<overFlow;i++) {
            if(!stack[i]) {
                stack[i]=true;
                return new RegisterOffset(i,Register.SP);
            }
        }
        overFlow++;
        return new RegisterOffset(overFlow,Register.SP);
    }
    public void freeValue(DVal register) {
        register.free(this);
    }
    public void freeStack(int index) {
        if(index==overFlow)
            overFlow--;
        stack[index]=false;
    }
    public void freeRegister(Register register) {
        for(int i=0;i<regLim;i++) {
            if(register.equals(Register.getR(i))) {
                reg[i]=false;
                return;
            }
        }
        throw new UnsupportedOperationException("ERROR : free register error");
    }
    public RegisterOffset translate (RegisterOffset register) {
        return new RegisterOffset(register.getOffset()-overFlow,register.getRegister());
    }
}
