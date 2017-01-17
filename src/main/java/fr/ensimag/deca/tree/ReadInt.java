package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import java.io.PrintStream;

/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = new IntType(compiler.getSymbols().create("readInt"));
        this.setType(t);
        return t;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    @Override 
    protected DVal codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new RINT());
        if(compiler.getCompilerOptions().getChecks()) {
            compiler.addInstruction(new BOV(compiler.getIOLabel()));
        }
        compiler.addInstruction(new WINT());
        return new NullOperand();
    }
    @Override 
    protected DVal codeGen(DecacCompiler compiler) {
        compiler.addInstruction(new RINT());
        if(compiler.getCompilerOptions().getChecks()) {
            compiler.addInstruction(new BOV(compiler.getIOLabel()));
        }
        return new NullOperand();
    }

}
