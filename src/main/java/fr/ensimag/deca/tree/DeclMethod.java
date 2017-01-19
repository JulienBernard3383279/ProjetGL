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
    
    //A changer lors de l'ajout du 4ème param
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam params,AbstractMethodBody body) {
        this.type=type;
        this.methodName=methodName;
        this.params=params;
        this.body=body;
    }
    
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
            currentClass.incNumberOfMethods();
            this.body.verifyMethodBody(compiler,currentClass,methodEnv,t);
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
        type.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        params.decompile(s);
        s.print(") "); s.indent();
        body.decompile(s);
        s.unindent(); s.println();
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s,prefix,false);
        methodName.prettyPrint(s,prefix,false);
        params.prettyPrint(s,prefix,false);
        body.prettyPrint(s,prefix,true);
        
    }
    @Override
    public void addMethodTabl(DecacCompiler compiler) {
        MethodDefinition def = (MethodDefinition)compiler.getEnvTypes().get(this.methodName.getName());
        //int stackPos = def.getIndex()+compiler.
    }
}
