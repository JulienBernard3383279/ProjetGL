package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ConstructBLT;
import fr.ensimag.deca.codegen.ConstructCMP;
import fr.ensimag.deca.codegen.ConstructSLT;
import fr.ensimag.deca.codegen.codeGenBinaryInstructionDValToReg;
import fr.ensimag.deca.codegen.codeGenUnaryInstructionToLabel;
import fr.ensimag.deca.codegen.codeGenUnaryInstructionToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public String getOperatorName() {
        return "<";
    }
    
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Shouldn't be called");
    }
    //TODO attention ce ci est le code de MUL
    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal regLeft=this.getLeftOperand().codeGen(compiler);
        DVal regRight=this.getRightOperand().codeGen(compiler);
        DVal returns = codeGenBinaryInstructionDValToReg.generate(compiler,
                super.getType(),
                new ConstructCMP(),
                regLeft,
                regRight);
        returns = codeGenUnaryInstructionToReg.generate(compiler,
                super.getType(),
                new ConstructSLT(),
                returns);
        return returns;
    }
    @Override
    protected void codeGenCond(DecacCompiler compiler,Label l,boolean jump) {
        DVal regLeft=this.getLeftOperand().codeGen(compiler);
        DVal regRight=this.getRightOperand().codeGen(compiler);
        codeGenBinaryInstructionDValToReg.generate(compiler,
                super.getType(),
                new ConstructCMP(),
                regLeft,
                regRight);
        codeGenUnaryInstructionToLabel.generate(compiler,
                l,
                jump,
                new ConstructBLT());
        regLeft.free(compiler);
        regRight.free(compiler);
    }
}
