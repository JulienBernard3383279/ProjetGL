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
import fr.ensimag.ima.pseudocode.instructions.WINT;

/**
 *
 * @author guignomes
 */
public class codeGenBinaryInstructionDValToReg {
    public static void generatePrint (DecacCompiler compiler,
            Type expType,
            ConstructBinaryInstructionDValToReg constructor,
            DVal regRight,
            DVal regLeft) {
        if(regRight.isGPRegister()) {
            if(regLeft.isGPRegister())
                compiler.addInstruction(constructor.construct(regLeft,(GPRegister)regRight));
            else if(regLeft.isRegisterOffset())
                compiler.addInstruction(constructor.construct(compiler.translate((RegisterOffset)regLeft),(GPRegister)regRight));
            else 
                throw new UnsupportedOperationException("Not supposed to be called");
            compiler.addInstruction(new LOAD (regRight,Register.R1));
            regRight.free(compiler);
            regLeft.free(compiler);
            if(expType.isFloat()) {
                compiler.addInstruction(new WFLOAT());
            }
            else if(expType.isInt()) {
                compiler.addInstruction(new WINT());
            }
            else 
            {
                throw new UnsupportedOperationException("Not supposed to be called");
            }
        }
        else if(regLeft.isGPRegister()) {
            if(regRight.isGPRegister())
                compiler.addInstruction(constructor.construct(regRight,(GPRegister)regLeft));
            else if(regRight.isRegisterOffset())
                compiler.addInstruction(constructor.construct(compiler.translate((RegisterOffset)regRight),(GPRegister)regLeft));
            else 
                throw new UnsupportedOperationException("Not supposed to be called");
            compiler.addInstruction(new LOAD (regLeft,Register.R1));
            regRight.free(compiler);
            regLeft.free(compiler);
            if(expType.isFloat())
                compiler.addInstruction(new WFLOAT());
            else if(expType.isInt())
                compiler.addInstruction(new WINT());
            else 
                throw new UnsupportedOperationException("Not supposed to be called");
        }
        else if(regRight.isRegisterOffset()&&regLeft.isRegisterOffset()) {
            compiler.addInstruction(new LOAD (compiler.translate((RegisterOffset)regLeft),Register.R1));
            compiler.addInstruction(constructor.construct(compiler.translate((RegisterOffset)regRight), Register.R1));
            regRight.free(compiler);
            regLeft.free(compiler);
            if(expType.isFloat())
                compiler.addInstruction(new WFLOAT());
            else if(expType.isInt())
                compiler.addInstruction(new WINT());
            else 
                throw new UnsupportedOperationException("Not supposed to be called");
        }
        else 
            throw new UnsupportedOperationException("Not supposed to be called");
    }
    public static DVal generate (DecacCompiler compiler,
            Type expType,
            ConstructBinaryInstructionDValToReg constructor,
            DVal regRight,
            DVal regLeft) {
        if(regRight.isGPRegister()) {
            if(regLeft.isGPRegister())
                compiler.addInstruction(constructor.construct(regLeft,(GPRegister)regRight));
            else if(regLeft.isRegisterOffset())
                compiler.addInstruction(constructor.construct(compiler.translate((RegisterOffset)regLeft),(GPRegister)regRight));
            else 
                throw new UnsupportedOperationException("Not supposed to be called");
            regLeft.free(compiler);
            return regRight;
        }
        else if(regLeft.isGPRegister()) {
            if(regRight.isGPRegister())
                compiler.addInstruction(constructor.construct(regRight,(GPRegister)regLeft));
            else if(regRight.isRegisterOffset())
                compiler.addInstruction(constructor.construct(compiler.translate((RegisterOffset)regRight),(GPRegister)regLeft));
            else 
                throw new UnsupportedOperationException("Not supposed to be called");
            regRight.free(compiler);
            return regLeft;
        }
        else if(regRight.isRegisterOffset()&&regLeft.isRegisterOffset()) {
            compiler.addInstruction(new LOAD (compiler.translate((RegisterOffset)regLeft),Register.R0));
            compiler.addInstruction(constructor.construct(compiler.translate((RegisterOffset)regRight),Register.R0));
            regRight.free(compiler);
            compiler.addInstruction(new LEA(compiler.translate((RegisterOffset)regLeft),Register.R1));
            return regLeft;
        }
        else
            throw new UnsupportedOperationException("Not supposed to be called");
    }
}
