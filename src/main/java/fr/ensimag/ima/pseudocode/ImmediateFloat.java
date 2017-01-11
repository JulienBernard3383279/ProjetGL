package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

/**
 * Immediate operand containing a float value.
 * 
 * @author Ensimag
 * @date 01/01/2017
 */
public class ImmediateFloat extends DVal {
    private float value;

    public ImmediateFloat(float value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + Float.toHexString(value);
    }
    @Override 
    public void free(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supposed to be call");
    }
}
