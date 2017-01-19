/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author gl58
 */
public class TestStackPUSH {
    DecacCompiler compiler =new DecacCompiler(null,null);
    @Test
    public void testStackunStack() {
        compiler.setRegLim(2);
        DVal temp;
        temp = compiler.allocRegister();
        assertTrue(temp.isRegisterOffset());
        RegisterOffset reg = (RegisterOffset) temp;
        RegisterOffset reg1 = (RegisterOffset)compiler.allocRegister();
        assertEquals(reg.getOffset(),1);
        assertEquals(reg1.getOffset(),2);
        assertEquals(compiler.getOverFlow(),3);
        assertEquals(compiler.translate(reg1).getOffset(),0);
        assertEquals(compiler.getOverFlow(),3);
        assertEquals(compiler.translate(reg).getOffset(),-1);
        reg.free(compiler);
        assertEquals(compiler.getOverFlow(),3);
        reg = (RegisterOffset)compiler.allocRegister();
        assertEquals(reg.getOffset(),1);
        reg.free(compiler);
        reg1.free(compiler);
        reg.free(compiler);
        reg = (RegisterOffset)compiler.allocRegister();
        assertEquals(reg.getOffset(),1);
        
    }

}