package fr.ensimag.ima.pseudocode;

/**
 * Operand that points to a memory location.
 * 
 * @author Ensimag
 * @date 01/01/2017
 */
public abstract class DAddr extends DVal {
    //TODO gestion des Addr
    @Override
    public boolean isDAddr() {
        return true;
    }
}
