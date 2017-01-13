package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t1;
        Type t2;
        try {
            t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }
        
        if (!t1.isBoolean() || !t2.isBoolean()) {
            throw new ContextualError("operands must be boolean", this.getLocation());
        }
        this.setType(t1);
        return t1;
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    @Override
    protected void codeGenCond(DecacCompiler compiler,Label l,boolean jump) {
        throw new UnsupportedOperationException("Should not be Called");
    }
}
