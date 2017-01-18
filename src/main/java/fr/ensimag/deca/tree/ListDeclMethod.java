/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

/**
 *
 * @author pierre
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod>{
    
    @Override
    public void decompile(IndentPrintStream s) {
        
    }
    
    protected void verifyListMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError{
        Iterator<AbstractDeclMethod> it = this.iterator();
        int index = 0;
        while (it.hasNext()) {
            try {
                it.next().verifyDeclMethod(compiler, currentClass,index);
                index = index + 1;
            } catch (ContextualError e) {
                throw e;
            }
        }
    }
}
