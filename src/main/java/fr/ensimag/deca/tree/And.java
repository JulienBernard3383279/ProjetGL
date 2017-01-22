package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGenBooleanOpToLabel;
import fr.ensimag.deca.codegen.codeGenBooleanOpToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.MUL;


/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String getOperatorName() {
        return "&&";
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Shouldn't be called");
    }
    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal regLeft = this.getLeftOperand().codeGen(compiler);
        DVal regRight = this.getRightOperand().codeGen(compiler);
        regLeft = codeGenBooleanOpToReg.generate(compiler,0,regRight,regLeft);
        return regLeft;
    }
    @Override
    protected void codeGenCond(DecacCompiler compiler,Label l,boolean jump) {
        DVal regLeft = this.getLeftOperand().codeGen(compiler);
        DVal regRight = this.getRightOperand().codeGen(compiler);
        codeGenBooleanOpToLabel.generate(compiler,0,l,jump,regRight,regLeft);
    }
    
}
