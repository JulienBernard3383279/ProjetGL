package fr.ensimag.deca.tree;
import java.lang.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.commons.lang.Validate;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl58
 * @date 01/01/2017
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t1;
        Type t2;
        try{
            t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }
        if ((!t1.isInt()&&!t1.isFloat())||(!t2.isInt()&&!t2.isFloat())) {
            throw new ContextualError("Operands must be int or float",this.getLocation());
        } else {
            if (t1.isFloat() && t2.isInt()){
                this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass,t1));
                return t1;
            } else if (t1.isInt() && t2.isFloat()) {
                this.setLeftOperand(this.getLeftOperand().verifyRValue(compiler, localEnv, currentClass, t2));
                return t2;
            }
        }
        return t1;
    }
}
