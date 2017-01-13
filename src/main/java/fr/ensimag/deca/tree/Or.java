package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGenBooleanOpToLabel;
import fr.ensimag.deca.codegen.codeGenBooleanOpToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;


/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal regLeft = this.getLeftOperand().codeGen(compiler);
        DVal regRight = this.getRightOperand().codeGen(compiler);
        regLeft = codeGenBooleanOpToReg.generate(compiler,1,regRight,regLeft);
        return regLeft;
    }
    @Override
    protected void codeGenCond(DecacCompiler compiler,Label l,boolean jump) {
        DVal regLeft = this.getLeftOperand().codeGen(compiler);
        DVal regRight = this.getRightOperand().codeGen(compiler);
        codeGenBooleanOpToLabel.generate(compiler,1,l,jump,regRight,regLeft);
    }
}
