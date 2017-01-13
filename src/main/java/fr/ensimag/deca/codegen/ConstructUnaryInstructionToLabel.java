/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl58
 */
public abstract class ConstructUnaryInstructionToLabel {
    public abstract BranchInstruction construct(Label op);
    public abstract BranchInstruction opposite(Label op);
}
