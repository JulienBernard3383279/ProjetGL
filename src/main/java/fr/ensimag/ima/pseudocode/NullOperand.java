package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

/**
 * The #null operand.
 *
 * @author Ensimag
 * @date 01/01/2017
 */
public class NullOperand extends DVal {

    @Override
    public String toString() {
        return "#null";
    }
    public NullOperand () {
        
    }
    @Override
    public void free(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supposed to be call");
    }
}
