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
public class ListDeclField extends TreeList<AbstractDeclField>{
    
    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractDeclField> it = this.iterator();
        while (it.hasNext()) {
            it.next().decompile(s);
            s.println();
        }
    }
    
    /**
     * Verification des attributs d'une classe (Passe 2)
     * @param compiler 
     */
    public void verifyListField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError{
        int n = currentClass.getSuperClass().getNumberOfFields() + 1;
        Iterator<AbstractDeclField> it = this.iterator();
        while (it.hasNext()) {
            try {
                it.next().verifyDeclField(compiler, currentClass,n);
                n = n + 1;
            } catch (ContextualError e) {
                throw e;
            }
        }
        currentClass.setNumberOfFields(n-1);
    }
    
    protected void verifyListInit(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        Iterator<AbstractDeclField> it = this.iterator();
        while (it.hasNext()) {
            try {
                it.next().verifyFieldInit(compiler, currentClass);
            } catch (ContextualError e) {
                throw e;
            }
        }
    }
}
