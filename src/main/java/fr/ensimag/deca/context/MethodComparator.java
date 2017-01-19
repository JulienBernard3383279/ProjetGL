/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.context;

import java.util.Comparator;

/**
 *
 * @author gl58
 */
public class MethodComparator implements Comparator<MethodInformation>{

    @Override
    public int compare(MethodInformation t, MethodInformation t1) {
        if(t.getIndex()>t1.getIndex()) 
            return 1;
        else 
            return 0;
    }
    
}
