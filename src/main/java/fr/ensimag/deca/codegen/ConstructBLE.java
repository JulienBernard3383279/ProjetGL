/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BGT;

/**
 *
 * @author gl58
 */
public class ConstructBLE extends ConstructUnaryInstructionToLabel {
    @Override
    public BranchInstruction construct(Label op) {
        return new BLE(op);
    }
    
    @Override
    public BranchInstruction opposite(Label op) {
        return new BGT(op);
    }
}
