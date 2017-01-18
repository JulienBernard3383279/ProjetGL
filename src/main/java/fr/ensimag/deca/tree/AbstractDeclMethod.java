/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

/**
 *
 * @author pierre
 */
public abstract class AbstractDeclMethod extends Tree{
    
    protected abstract void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass,int index) throws ContextualError;
    
    protected abstract void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;
       
}
