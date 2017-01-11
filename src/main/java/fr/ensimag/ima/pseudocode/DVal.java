package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

/**
 * Operand that contains a value.
 * 
 * @author Ensimag
 * @date 01/01/2017
 */
public abstract class DVal extends Operand {
    public abstract void free(DecacCompiler compiler);
    public boolean isRegister() {
        return false;
    }
    public boolean isDAddr() {
        return false;
    }
    public boolean isRegisterOffset()  {
        return false;
    }
    public boolean isGPRegister() {
        return false;
    }
}
