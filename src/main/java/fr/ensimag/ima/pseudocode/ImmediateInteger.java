package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

/**
 * Immediate operand representing an integer.
 * 
 * @author Ensimag
 * @date 01/01/2017
 */
public class ImmediateInteger extends DVal {
    private int value;

    public ImmediateInteger(int value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + value;
    }
    @Override 
    public void free(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supposed to be call");
    }
}
