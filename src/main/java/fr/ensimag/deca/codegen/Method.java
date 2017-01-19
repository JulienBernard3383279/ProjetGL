/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl58
 */
public class Method {
    private int tsto;
    private Label code; 
    public Method(Label l) {
        tsto=0;
        code=l;
    }
    public Method(String s) {
        tsto=0;
        code=new Label(s);
    }
    public void incTsto() {
        tsto++;
    }
    public int getTsto() {
        return this.tsto;
    }
    public Label getLabel() {
        return code;
    }
}
