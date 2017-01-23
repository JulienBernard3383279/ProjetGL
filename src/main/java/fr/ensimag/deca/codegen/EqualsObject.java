/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

/**
 *
 * @author gl58
 */
public class EqualsObject {
    public static void generateObjectMethod(DecacCompiler compiler) {
        compiler.addLabel(new Label("equals_object"));
        compiler.addInstruction(new TSTO(0));
        compiler.addInstruction(new LOAD(new RegisterOffset(-3,Register.SP),Register.R0));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.SP),Register.R1));
        compiler.addInstruction(new CMP(Register.R0,Register.R1));
        compiler.addInstruction(new SEQ(Register.R0));
        compiler.addInstruction(new RTS());
    }
}
