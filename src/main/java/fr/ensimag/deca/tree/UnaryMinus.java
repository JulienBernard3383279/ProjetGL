package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl58
 * @date 01/01/2017
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t;
        try {
            t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }
        
        if (!t.isInt() && !t.isFloat()) {
            throw new ContextualError("Operand must be int or float",this.getOperand().getLocation());
        }
        this.setType(t);
        return t;
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
