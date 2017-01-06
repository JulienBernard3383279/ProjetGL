package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;


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
    /*@Override
    protected void codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(null);
    }*/
    

}
