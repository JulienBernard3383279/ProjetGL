package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;


/**
 * @author gl58
 * @date 01/01/2017
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        try {
            EnvironmentExp localEnv = new EnvironmentExp(null);
            this.declVariables.verifyListDeclVariable(compiler, localEnv, null);
            this.insts.verifyListInst(compiler, localEnv, null, null);
        } catch (ContextualError e) {
            throw e;
        }
        LOG.debug("verify Main: end");
        
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        execute_dead(compiler); //on nettoie l'arbre avant
        execute_folding(compiler);
        compiler.addComment("Seprate constant stack and temporary variables");
        compiler.addComment("Beginning of main instructions:");
        declVariables.codeGenListVar(compiler);
        insts.codeGenListInst(compiler);
        compiler.addInstruction(new HALT());
        if(compiler.getCompilerOptions().getChecks()) {
            
            compiler.addLabel(compiler.getStackOV());
            compiler.addInstruction(new WSTR("Erreur : pile pleine"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            compiler.addInstructionAtProgramBeginning(new ADDSP(compiler.getSizeOfConstantStack()+compiler.getCurrentMethodNumber()));
            compiler.addInstructionAtProgramBeginning(new BOV(compiler.getStackOV()));
            compiler.addInstructionAtProgramBeginning(new TSTO(compiler.argTSTO()));
            compiler.addLabel(compiler.getIOLabel());
            compiler.addInstruction(new WSTR("Error: Input/Output error"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            compiler.addLabel(compiler.getOVLabel());
            compiler.addInstruction(new WSTR("Error: Arithmetic Overflow"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            compiler.addLabel(compiler.getHeapOV());
            compiler.addInstruction(new WSTR("Error: Heap OverFlow"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
    }
    
    public void execute_dead(DecacCompiler compiler){
        if(compiler.getCompilerOptions().getDead()){  //si on a bien ajouté l'option -o1 dans la ligne de commande           
            compiler.getDead().execute(declVariables, insts);
        }    
    }
    
    public void execute_folding(DecacCompiler compiler){
        if(compiler.getCompilerOptions().getFolding()){  //si on a bien ajouté l'option -o1 dans la ligne de commande           
            compiler.getFolding().execute(insts);
        }    
    }
    
    
   /* protected void execute_constant_folding(DecacCompiler compiler){
        if(compiler.getCompilerOptions().getFolding()){
            compiler.get
        }
    }*/
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
