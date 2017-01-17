/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author bernajul
 */
public class MethodAsmBody extends AbstractMethodBody {
    
    String text;
    
    public MethodAsmBody(String text) {
        this.text=text;
    }
    
    @Override 
    protected void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass, MethodDefinition currentMethod) {
        
    }
    
    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    }
}
