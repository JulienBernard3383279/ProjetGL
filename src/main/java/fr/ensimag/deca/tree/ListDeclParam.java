/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

/**
 *
 * @author pierre
 */
public class ListDeclParam extends TreeList<AbstractDeclParam>{
 
    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractDeclParam> it = this.iterator();
        while (it.hasNext()) {
            it.next().decompile(s);
            if (it.hasNext()) {
                s.print(", ");
            }
        }
    }
    
    protected void verifyListParam(DecacCompiler compiler, ClassDefinition currentClass, MethodDefinition currentMethod, EnvironmentExp localEnv) throws ContextualError {
        Iterator<AbstractDeclParam> it = this.iterator();
        while (it.hasNext()) {
            try {
                it.next().verifyDeclParam(compiler, currentClass, currentMethod,localEnv);
            } catch (ContextualError e) {
                throw e;
            }
        }
    }
}
