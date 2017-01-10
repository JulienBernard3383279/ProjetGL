package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.LiteralInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author gl58
 * @date 01/01/2017
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = new IntType(null);
        this.setType(t);
        return t;
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
    protected void codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(value,Register.R1));
        compiler.addInstruction(new WINT());
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        
        int []regWrite = compiler.openWrite();
        if(regWrite[0]!=-1) {
            compiler.addInstruction(new LOAD(new LiteralInteger(this.value),Register.getR(regWrite[0])));
        }
        else {
            //TODO voir ce que l'on doit faire si tout les registre sont attribué 
            throw new UnsupportedOperationException("not yet implemented");
        }
        compiler.closeWrite();
    }

}
