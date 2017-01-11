/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;


public class LiteralInteger extends DVal{

    
    int val;
    public LiteralInteger(int value) {
        this.val=value;
    }
    public int getValue() {
        return this.val;
    }
    @Override
    public String toString() {
        return "#"+val; //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void free(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supposed to be call");
    }  
}
