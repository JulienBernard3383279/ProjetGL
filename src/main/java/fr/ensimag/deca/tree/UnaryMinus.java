package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ConstructMUL;
import fr.ensimag.deca.codegen.ConstructOPP;
import fr.ensimag.deca.codegen.codeGenBinaryInstructionDValToReg;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;

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
        
        return t;
    }
    @Override 
    protected DVal codeGenPrint(DecacCompiler compiler) {
        return new NullOperand();
    }
    @Override 
    protected DVal codeGen(DecacCompiler compiler) {
        DVal reg = this.getOperand().codeGen(compiler);
         DVal returns = codeGenBinaryInstructionDValToReg.generate(compiler,
                super.getType(),
                new ConstructOPP(),
                reg,
                reg);
        return returns;
    }
    @Override
    protected String getOperatorName() {
        return "-";
    }

}
