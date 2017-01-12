/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author guignomes
 */
public class ConstructREM extends ConstructBinaryInstructionDValToReg{

    @Override
    public BinaryInstructionDValToReg construct(DVal op1,GPRegister op2) {
        return new REM(op1,op2);
    }
    
}
