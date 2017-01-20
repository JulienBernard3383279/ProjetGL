/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.context;

import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl58
 */
public class MethodInformation {
    private Label l;
    private int index;
    public MethodInformation(Label l,int index) {
        this.l=l;
        this.index=index;
    }
    public int getIndex() {
        return index;
    }
    public Label getLabel() {
        return l;
    }
    @Override 
    public boolean equals(Object o) {
        if(o instanceof MethodInformation) {
            if(((MethodInformation)o).getIndex()==this.index)
                return true;
        }
        return false;
    }
}
