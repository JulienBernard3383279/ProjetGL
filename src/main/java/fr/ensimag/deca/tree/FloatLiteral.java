package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Single precision, floating-point literal
 *
 * @author gl58
 * @date 01/01/2017
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;

    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        return new FloatType(null);       
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toHexString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
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
        compiler.addInstruction(new LOAD((int)value,Register.R1));
        compiler.addInstruction(new FLOAT(Register.R1,Register.R1));
        compiler.addInstruction(new WFLOAT());
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int []regWrite = compiler.openWrite();
        if(regWrite[0]!=-1) {
            compiler.addInstruction(new LOAD((int)value,
                    Register.getR(regWrite[0])));
            compiler.addInstruction(new FLOAT(Register.getR(regWrite[0]),
                    Register.getR(regWrite[0])));
        }
        else {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

}