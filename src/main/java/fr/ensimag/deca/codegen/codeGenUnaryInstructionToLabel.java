/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl58
 */
public class codeGenUnaryInstructionToLabel {
    public static void generate (DecacCompiler compiler,
            Label l,
            boolean jump, 
            ConstructUnaryInstructionToLabel constructor) {
        if(jump)
            compiler.addInstruction(constructor.construct(l));
        else 
            compiler.addInstruction(constructor.opposite(l));
    }
}
