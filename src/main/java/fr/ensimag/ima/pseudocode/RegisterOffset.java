package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

/**
 * Operand representing a register indirection with offset, e.g. 42(R3).
 *
 * @author Ensimag
 * @date 01/01/2017
 */
public class RegisterOffset extends DAddr {
    public int getOffset() {
        return offset;
    }
    public void setOffset(int offset) {
        this.offset=offset;
    }
    public Register getRegister() {
        return register;
    }
    private int offset;
    private final Register register;
    public RegisterOffset(int offset, Register register) {
        super();
        this.offset = offset;
        this.register = register;
    }
    @Override
    public String toString() {
        return offset + "(" + register + ")";
    }
    @Override 
    public void free(DecacCompiler compiler) {
        if(this.register.equals(Register.SP))
            compiler.freeStack(this.offset);
        else if(this.register.equals(Register.GB)) {}
            //nothing to be done we dont free var 
        else if(this.register.equals(Register.LB)) {}
            //nothing to be done we don't free param
        else 
            this.register.free(compiler);
        
    }
    @Override
    public boolean isRegisterOffset() {
        return true;
    }
}
