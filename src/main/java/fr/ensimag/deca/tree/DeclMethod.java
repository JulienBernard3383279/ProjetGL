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
public class DeclMethod extends AbstractDeclMethod{
    
    public AbstractIdentifier type;
    public AbstractIdentifier methodName;
    public ListDeclParam params;
    public AbstractMethodBody body;
    
    //A changer lors de l'ajout du 4ème param
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam params,AbstractMethodBody body) {
        this.type=type;
        this.methodName=methodName;
        this.params=params;
        this.body=body;
    }
    
    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError{
        
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
