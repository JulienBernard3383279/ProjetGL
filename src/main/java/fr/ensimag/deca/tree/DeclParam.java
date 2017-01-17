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
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author pierre
 */
public class DeclParam extends AbstractDeclParam{
    
    public AbstractIdentifier type;
    public AbstractIdentifier paramName;
    
    @Override
    protected void verifyDeclParam(DecacCompiler compiler, ClassDefinition currentClass, MethodDefinition currentMethod, EnvironmentExp localEnv) throws ContextualError{
        Type t;
        try {
            t = this.type.verifyType(compiler);
            ParamDefinition def = new ParamDefinition(t,this.getLocation());
            currentMethod.getSignature().add(t);
            localEnv.declare(this.paramName.getName(),def);
        } catch (EnvironmentExp.DoubleDefException d) {
            throw new ContextualError("parameter already defined",this.paramName.getLocation());
        }
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
