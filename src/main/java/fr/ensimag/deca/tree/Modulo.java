package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ConstructREM;
import fr.ensimag.deca.codegen.codeGenBinaryInstructionDValToReg;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;

/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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
        
        if ((! t1.isInt()) && (! t2.isInt())) {
            throw new ContextualError("Modulo operands must be int",this.getLocation());
        }
        this.setType(t1);
        return t1;
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }
    
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        DVal regRight = this.getRightOperand().codeGen(compiler);
        DVal regLeft  = this.getLeftOperand().codeGen(compiler);
        codeGenBinaryInstructionDValToReg.generatePrint(compiler,
                super.getType(),
                new ConstructREM(),
                regRight,
                regLeft);
        return new NullOperand();
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal regRight = this.getRightOperand().codeGen(compiler);
        DVal regLeft  = this.getLeftOperand().codeGen(compiler);
        DVal returns = codeGenBinaryInstructionDValToReg.generate(compiler,
                super.getType(),
                new ConstructREM(),
                regRight,
                regLeft);
        return returns;
    }

}
