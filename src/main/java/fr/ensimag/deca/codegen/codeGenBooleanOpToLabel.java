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
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl58
 */
public class codeGenBooleanOpToLabel {
    private static boolean xor(boolean a,boolean b) {
        return (a&&b) || (!a&&!b);
    }
    public static void generate(DecacCompiler compiler,int value,Label lgo,boolean jump,DVal regRight,DVal regLeft) {
        Label lfin = new Label("FinAndOr"+compiler.newAndOr());
        if(regRight.isGPRegister()) {
            if(regLeft.isGPRegister()) { 
                compiler.addInstruction(new CMP(new ImmediateInteger(value),(GPRegister)regRight)) ;
                if(xor(jump,value==1))
                    compiler.addInstruction(new BEQ(lgo));
                else
                    compiler.addInstruction(new BEQ(lfin));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),(GPRegister)regLeft));
                if(xor(jump,value==1))
                    compiler.addInstruction(new BEQ(lgo));
                else {
                    compiler.addInstruction(new BEQ(lfin));
                    compiler.addInstruction(new BRA(lgo));
                    compiler.addLabel(lfin);
                }   
            }
            else if(regLeft.isRegisterOffset()) {
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regLeft),Register.R0));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),(GPRegister)regRight));
                if(xor(jump,value==1))
                    compiler.addInstruction(new BEQ(lgo));
                else
                    compiler.addInstruction(new BEQ(lfin));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),Register.R0));
                if(xor(jump,value==1))
                    compiler.addInstruction(new BEQ(lgo));
                else {
                    compiler.addInstruction(new BEQ(lfin));
                    compiler.addInstruction(new BRA(lgo));
                    compiler.addLabel(lfin);
                }
            }
            else 
                throw new UnsupportedOperationException("Not supposed to be called");  
        }
        else if(regRight.isRegisterOffset()){
            if(regLeft.isGPRegister()) {
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regRight),Register.R0));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),Register.R0));
                if(xor(jump,value==1))
                    compiler.addInstruction(new BEQ(lgo));
                else
                    compiler.addInstruction(new BEQ(lfin));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),(GPRegister)regLeft));
                if(xor(jump,value==1))
                    compiler.addInstruction(new BEQ(lgo));
                else {
                    compiler.addInstruction(new BEQ(lfin));
                    compiler.addInstruction(new BRA(lgo));
                    compiler.addLabel(lfin);
                }
            }
            else if(regLeft.isRegisterOffset()) {
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regLeft),Register.R0));
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regRight),Register.R1));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),Register.R1));
                if(xor(jump,value==1))
                    compiler.addInstruction(new BEQ(lgo));
                else
                    compiler.addInstruction(new BEQ(lfin));
                compiler.addInstruction(new CMP(new ImmediateInteger(value),Register.R0));
                if(xor(jump,value==1))
                    compiler.addInstruction(new BEQ(lgo));
                else {
                    compiler.addInstruction(new BEQ(lfin));
                    compiler.addInstruction(new BRA(lgo));
                    compiler.addLabel(lfin);
                }
            } 
            else 
                throw new UnsupportedOperationException("Not supposed to be called");
        }
        else 
            throw new UnsupportedOperationException("Not supposed to be called");
        regRight.free(compiler);
        regLeft.free(compiler);
    }
}
