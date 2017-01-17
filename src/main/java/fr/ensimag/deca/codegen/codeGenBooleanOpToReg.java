/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.SNE;

/**
 *
 * @author gl58
 */
public class codeGenBooleanOpToReg {
    public static DVal generate(DecacCompiler compiler,int value,DVal regRight,DVal regLeft) {
        
        Label l = new Label("FinAndOr"+compiler.newAndOr());
        if(regRight.isGPRegister()) {
            if(regLeft.isGPRegister()) {
                compiler.addInstruction(new CMP(new ImmediateInteger(value),(GPRegister)regRight));
                if(value==1)
                    compiler.addInstruction(new SEQ((GPRegister)regRight));
                else
                    compiler.addInstruction(new SNE((GPRegister)regRight));
                compiler.addInstruction(new BEQ(l));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),(GPRegister)regLeft));
                if(value==1)
                    compiler.addInstruction(new SEQ((GPRegister)regRight));
                else 
                    compiler.addInstruction(new SNE((GPRegister)regRight));
                compiler.addInstruction(new BEQ(l));
                compiler.addInstruction(new LOAD(new ImmediateInteger(1-value),(GPRegister)regRight));
                compiler.addLabel(l);
                regLeft.free(compiler);
                return regRight;
            }
            else if(regLeft.isRegisterOffset()) {
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regLeft),Register.R0));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),(GPRegister)regRight));
                if(value==1)
                    compiler.addInstruction(new SEQ((GPRegister)regRight));
                else
                    compiler.addInstruction(new SNE((GPRegister)regRight));
                compiler.addInstruction(new BEQ(l));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),Register.R0));
                if(value==1)
                    compiler.addInstruction(new SEQ((GPRegister)regRight));
                else 
                    compiler.addInstruction(new SNE((GPRegister)regRight));
                compiler.addInstruction(new BEQ(l));
                compiler.addInstruction(new LOAD(new ImmediateInteger(1-value),(GPRegister)regRight));
                compiler.addLabel(l);
                regLeft.free(compiler);
                return regRight;
            }
            else 
                throw new UnsupportedOperationException("Not supposed to be called");  
        }
        else if(regRight.isRegisterOffset()){
            if(regLeft.isGPRegister()) {
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regRight),Register.R0));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),(GPRegister)regLeft));
                if(value==1)
                    compiler.addInstruction(new SEQ((GPRegister)regLeft));
                else
                    compiler.addInstruction(new SNE((GPRegister)regLeft));
                compiler.addInstruction(new BEQ(l));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),Register.R0));
                if(value==1)
                    compiler.addInstruction(new SEQ((GPRegister)regLeft));
                else 
                    compiler.addInstruction(new SNE((GPRegister)regLeft));
                compiler.addInstruction(new BEQ(l));
                compiler.addInstruction(new LOAD(new ImmediateInteger(1-value),(GPRegister)regLeft));
                compiler.addLabel(l);
                regRight.free(compiler);
                return regLeft;
            }
            else if(regLeft.isRegisterOffset()) {
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regLeft),Register.R0));
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regRight),Register.R1));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),Register.R0));
                if(value==1)
                    compiler.addInstruction(new SEQ(Register.R1));
                else
                    compiler.addInstruction(new SNE(Register.R1));
                compiler.addInstruction(new BEQ(l));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),Register.R0));
                if(value==1)
                    compiler.addInstruction(new SEQ(Register.R1));
                else 
                    compiler.addInstruction(new SNE(Register.R1));
                compiler.addInstruction(new BEQ(l));
                compiler.addInstruction(new LOAD(new ImmediateInteger(1-value),Register.R1));
                compiler.addLabel(l);
                compiler.addInstruction(new LEA((RegisterOffset)regRight,Register.R1));
                regLeft.free(compiler);
                return regRight;
            } 
            else 
                throw new UnsupportedOperationException("Not supposed to be called");
        }
        else 
            throw new UnsupportedOperationException("Not supposed to be called");
    }
}
