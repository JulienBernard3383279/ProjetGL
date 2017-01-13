package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;

/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
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
        
        if (!t.isBoolean()) {
            throw new ContextualError("Operand must be boolean",this.getOperand().getLocation());
        }
        this.setType(t);
        return t;
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
    @Override 
    protected DVal codeGenPrint(DecacCompiler compiler) {
        return new NullOperand();
    }
    @Override 
    protected DVal codeGen(DecacCompiler compiler) {
        return new NullOperand();
    }
}
