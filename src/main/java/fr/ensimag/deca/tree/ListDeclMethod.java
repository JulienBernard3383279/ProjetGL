/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author pierre
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod>{
    
    @Override
    public void decompile(IndentPrintStream s) {
        
    }
    
    protected void verifyListMethod(DecacCompiler compiler, ClassDefinition currentClass) {
        
    }
}
