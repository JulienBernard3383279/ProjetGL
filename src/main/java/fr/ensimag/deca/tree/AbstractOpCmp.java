package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
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
        //error in case either operand is not numerical and if they have different types
        if ((!t1.isInt() && !t1.isFloat())||(!t2.isInt() && !t2.isFloat())) {
            throw new ContextualError("operands must be int or float",this.getLocation());
        } else if(!t1.sameType(t2)) {
            throw new ContextualError("operands must have same type",this.getLocation());
        }
        Type t = new BooleanType(compiler.getSymbols().create("boolean"));
        this.setType(t);
        return t;
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Should not be called");
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void codeGenCond(DecacCompiler compiler,Label l,boolean jump) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
