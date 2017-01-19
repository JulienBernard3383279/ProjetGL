/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl58
 */
public class NullAddr extends DAddr{
    @Override
    public String toString() {
        return "#null";
    }
    public NullAddr () {
        
    }
    @Override
    public void free(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supposed to be call");
    }
}
