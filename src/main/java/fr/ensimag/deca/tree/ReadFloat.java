package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import java.io.PrintStream;

/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = new FloatType(compiler.getSymbols().create("float"));
        this.setType(t);
        return t;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
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
        compiler.addInstruction(new RFLOAT());
        if(compiler.getCompilerOptions().getChecks()) {
            compiler.addInstruction(new BOV(compiler.getIOLabel()));
        }
        compiler.addInstruction(new WFLOAT());
        return new NullOperand();
    }
    @Override 
    protected DVal codeGen(DecacCompiler compiler) {
        compiler.addInstruction(new RFLOAT());
        if(compiler.getCompilerOptions().getChecks()) {
            compiler.addInstruction(new BOV(compiler.getIOLabel()));
        }
        return Register.R1;
    }
}
