package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ConstructSUB;
import fr.ensimag.deca.codegen.codeGenBinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;


/**
 * @author gl58
 * @date 01/01/2017
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public String getOperatorName() {
        return "-";
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        DVal regLeft  = this.getLeftOperand().codeGen(compiler);
        DVal regRight = this.getRightOperand().codeGen(compiler);
        codeGenBinaryInstructionDValToReg.generatePrint(compiler,
                super.getType(),
                new ConstructSUB(),
                regLeft,
                regRight);
        return new NullOperand();
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal regLeft  = this.getLeftOperand().codeGen(compiler);
        DVal regRight = this.getRightOperand().codeGen(compiler);
        DVal returns = codeGenBinaryInstructionDValToReg.generate(compiler,
                super.getType(),
                new ConstructSUB(),
                regLeft,
                regRight);
        return returns;
    }
    
}
