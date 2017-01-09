/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.ensimag.ima.pseudocode;

/**
 *
 * @author guignomes
 */
public class LiteralInteger extends DVal{

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    int val;
    public LiteralInteger(int value) {
        this.val=value;
    }
    public int getValue() {
        return this.val;
    }
    
}
