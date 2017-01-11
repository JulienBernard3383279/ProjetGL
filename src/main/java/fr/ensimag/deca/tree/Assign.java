package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t;
        try {
            t = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, t);
        } catch (ContextualError e) {
            throw e;
        }
        this.setType(t);
        return t;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
