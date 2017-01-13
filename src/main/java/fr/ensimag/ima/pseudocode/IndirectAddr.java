/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author guignomes
 */
public class IndirectAddr extends DAddr {
    private int addr;
    public enum StackPos {
        SP,LB,GB
    }
    private StackPos pos;
    public IndirectAddr (int addresse,StackPos position) {
        this.addr=addresse;   
        this.pos=position;
    }
    public IndirectAddr(int addresse,String position) {
        this.addr = addresse;
        if(position.equals("SP")) {
            this.pos=StackPos.SP;
        }
        else if(position.equals("LB")) {
            this.pos=StackPos.LB;
        }
        else if(position.equals("GB")) {
            this.pos=StackPos.GB;
        }
        else {
            throw new UnsupportedOperationException("Address format unknown: "
                    +position);
        }
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override 
    public void free(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supposed to be call");
    }
}
