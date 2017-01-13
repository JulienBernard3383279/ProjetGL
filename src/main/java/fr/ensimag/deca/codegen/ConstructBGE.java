/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 *
 * @author gl58
 */
public class ConstructBGE extends ConstructUnaryInstructionToLabel {
    @Override
    public BranchInstruction construct(Label op) {
        return new BGE(op);
    }
    
    @Override
    public BranchInstruction opposite(Label op) {
        return new BLT(op);
    }
}
