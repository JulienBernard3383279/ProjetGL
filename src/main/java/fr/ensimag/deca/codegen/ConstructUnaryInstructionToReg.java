/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;

/**
 *
 * @author gl58
 */
public abstract class ConstructUnaryInstructionToReg {
    public abstract UnaryInstructionToReg construct(GPRegister op);
}
