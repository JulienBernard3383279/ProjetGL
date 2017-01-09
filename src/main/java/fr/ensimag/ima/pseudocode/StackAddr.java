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
public class StackAddr extends DAddr {
    private int addr;
    public enum StackPos {
        SP,LB,GB
    }
    private StackPos pos;
    public StackAddr (int addresse,StackPos position) {
        this.addr=addresse;   
        this.pos=position;
    }
    public StackAddr(int addresse,String position) {
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
}
