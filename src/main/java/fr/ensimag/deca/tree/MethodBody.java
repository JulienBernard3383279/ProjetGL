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
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author pierre
 */
public class MethodBody extends AbstractMethodBody{
    ListDeclVar decls;
    ListInst insts;
    
    public MethodBody(ListDeclVar decls, ListInst insts) {
        this.decls=decls;
        this.insts=insts;
    }
    
    @Override 
    protected void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass, EnvironmentExp localEnv, Type returnType) throws ContextualError{
        try {
            this.decls.verifyListDeclVariable(compiler, localEnv, currentClass);
            this.insts.verifyListInst(compiler, localEnv, currentClass, returnType);
        } catch (ContextualError e) {
            throw e;
        }
    }
    
    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        decls.decompile(s);
        insts.decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        decls.prettyPrint(s,prefix,false);
        insts.prettyPrint(s,prefix,true);
    }
}
