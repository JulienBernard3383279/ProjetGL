package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;


/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
