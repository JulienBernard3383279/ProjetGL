package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ConstructADD;
import fr.ensimag.deca.codegen.ConstructDIV;
import fr.ensimag.deca.codegen.ConstructQUO;
import fr.ensimag.deca.codegen.codeGenBinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;


/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        DVal regRight = this.getRightOperand().codeGen(compiler);
        DVal regLeft  = this.getLeftOperand().codeGen(compiler);
        if(super.getType().isInt()) {
            codeGenBinaryInstructionDValToReg.generatePrint(compiler,
                super.getType(),
                new ConstructQUO(),
                regLeft,
                regRight);
        return new NullOperand();
        }
        else if(super.getType().isFloat()){
            codeGenBinaryInstructionDValToReg.generatePrint(compiler,
                super.getType(),
                new ConstructDIV(),
                regLeft,
                regRight);
        return new NullOperand();
        }
        else {
            throw new UnsupportedOperationException("Not supposed to be called");
        }
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal regRight = this.getRightOperand().codeGen(compiler);
        DVal regLeft  = this.getLeftOperand().codeGen(compiler);
        DVal returns;
        if(super.getType().isInt()) {
            returns = codeGenBinaryInstructionDValToReg.generate(compiler,
                super.getType(),
                new ConstructQUO(),
                regLeft,
                regRight);
        }
        else if(super.getType().isFloat()){
            returns = codeGenBinaryInstructionDValToReg.generate(compiler,
                super.getType(),
                new ConstructDIV(),
                regLeft,
                regRight);
        }
        else {
            throw new UnsupportedOperationException("Not supposed to be called");
        }
        return returns;
    }
}
