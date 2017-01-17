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
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
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
    
    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass,int index) throws ContextualError{
        Type t;
        EnvironmentExp classEnv = currentClass.getMembers();
        MethodDefinition def;
        try {
            t = this.type.verifyType(compiler);
            def = new MethodDefinition(t,this.getLocation(),new Signature(),index);
            EnvironmentExp methodEnv = new EnvironmentExp(classEnv);
            this.params.verifyListParam(compiler, currentClass,def,methodEnv);
            this.methodName.setDefinition(def);
            classEnv.declare(methodName.getName(), def);
            this.body.verifyMethodBody(compiler,currentClass,def,methodEnv);
        } catch (ContextualError e) {
            throw e;
        } catch (EnvironmentExp.DoubleDefException d) {
            throw new ContextualError("method already defined",this.methodName.getLocation());
        }
        
        EnvironmentExp superEnv = currentClass.getSuperClass().getMembers();
        MethodDefinition superDef = (MethodDefinition) superEnv.get(methodName.getName());
        if (!superDef.getSignature().equals(def.getSignature())) {
            throw new ContextualError("method overrides method with different signature",this.getLocation());
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
