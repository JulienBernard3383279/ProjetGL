/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.context;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author gl58
 */
public class ClassMethodSet {
    private DAddr addr;
    private Set<MethodInformation> methods = new TreeSet<MethodInformation>(new MethodComparator());
    public ClassMethodSet(ClassMethodSet superClassSet) {
        methods.addAll(superClassSet.getMethods());
        this.addr=superClassSet.getAddr();
    }
    public ClassMethodSet(DAddr addr) {//constructeur pour Object uniquement
        this.addr=addr;
    }
    public Set<MethodInformation> getMethods() {
        return methods;
    }
    public DAddr getAddr() {
        return addr;
    }
    public void addMethod(MethodInformation m) {
        this.methods.add(m);
    }

    void setAddr(RegisterOffset addr) {
        this.addr=addr;
    }
    
}
