/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;
import fr.ensimag.ima.pseudocode.instructions.SNE;

/**
 *
 * @author gl58
 */
public class ConstructSNE extends ConstructUnaryInstructionToReg{
    @Override
    public UnaryInstructionToReg construct(GPRegister op) {
        return new SNE(op);
    }
    
}