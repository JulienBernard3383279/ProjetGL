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
import java.io.PrintStream;

/**
 *
 * @author pierre
 */
public class DeclParam extends AbstractDeclParam{
    
    public AbstractIdentifier type;
    public AbstractIdentifier paramName;
    
    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
        this.type=type;
        this.paramName=paramName;
    }
    @Override
    protected void verifyDeclParam(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError{
        
    }
    
    @Override 
    public void decompile(IndentPrintStream s) {
        
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s,prefix,false);
        paramName.prettyPrint(s,prefix,true);
    }
}
