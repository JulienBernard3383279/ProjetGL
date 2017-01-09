package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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
        
        if ((!t1.isInt() && !t1.isFloat())||(!t2.isInt() && !t2.isFloat())) {
            throw new ContextualError("Operands must be int or float",this.getLocation());
        } else if(!t1.sameType(t2)) {
            throw new ContextualError("Operands must have same type",this.getLocation());
        }
        return new BooleanType(null);
    }


}
