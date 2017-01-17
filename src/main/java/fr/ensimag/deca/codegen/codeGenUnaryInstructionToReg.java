/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

/**
 *
 * @author gl58
 */
public class codeGenUnaryInstructionToReg {
    public static DVal generate (DecacCompiler compiler,
            Type expType,
            ConstructUnaryInstructionToReg constructor,
            DVal reg) {
        if(reg.isGPRegister()) {
            compiler.addInstruction(constructor.construct((GPRegister)reg));
        }
        else if(reg.isRegisterOffset()) {
            compiler.addInstruction(new LOAD (compiler.translate((RegisterOffset)reg),Register.R0));
            compiler.addInstruction(constructor.construct(Register.R0));
            compiler.addInstruction(new LEA(compiler.translate((RegisterOffset)reg),Register.R0));
        }
        else {
            throw new UnsupportedOperationException("Not supposed to be called"); 
        }
        return reg;
    }
    /**
     * Only used for Unary Minus Normaly
     * @param compiler
     * @param expType
     * @param constructor
     * @param reg 
     */
    public static void generatePrint(DecacCompiler compiler,
            Type expType,
            ConstructUnaryInstructionToReg constructor,
            DVal reg) {
        if(reg.isGPRegister()) {
            compiler.addInstruction(constructor.construct((GPRegister)reg));
            compiler.addInstruction(new LOAD((GPRegister)reg,Register.R1));
        }
        else if(reg.isRegisterOffset()) {
            compiler.addInstruction(new LOAD (compiler.translate((RegisterOffset)reg),Register.R1));
            compiler.addInstruction(constructor.construct(Register.R1));
            compiler.addInstruction(new LEA(compiler.translate((RegisterOffset)reg),Register.R1));
        }
        else {
            throw new UnsupportedOperationException("Not supposed to be called"); 
        }
        if(expType.isFloat()&&!compiler.getPrintHex()) {
                compiler.addInstruction(new WFLOAT());
            }
        else if(expType.isFloat()&&compiler.getPrintHex())
                compiler.addInstruction(new WFLOATX());
            else if(expType.isInt()) {
                compiler.addInstruction(new WINT());
            }
            else 
            {
                throw new UnsupportedOperationException("Not supposed to be called");
            }
    }
}
