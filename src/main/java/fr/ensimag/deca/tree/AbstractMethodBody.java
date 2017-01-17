/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;

/**
 *
 * @author pierre
 */
public abstract class AbstractMethodBody extends Tree{
    
    protected abstract void codeGenMethodBody(DecacCompiler compiler);
    
    protected abstract void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass, MethodDefinition currentMethod, EnvironmentExp localEnv);
}
