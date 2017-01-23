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
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
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
            ExpDefinition superDef = classEnv.get(methodName.getName());
            if (superDef != null) {
                //in case method overrides an existing method, transfer index
                if (superDef.isMethod()) {
                    MethodDefinition superDef2 = (MethodDefinition)superDef;
                    def = new MethodDefinition(t,this.getLocation(),new Signature(),superDef2.getIndex());
                } else {
                    def = new MethodDefinition(t,this.getLocation(),new Signature(),index);
                    currentClass.incNumberOfMethods();
                    index = index + 1;
                }
            } else {
                def = new MethodDefinition(t,this.getLocation(),new Signature(),index);
                currentClass.incNumberOfMethods();
                index = index + 1;
            }
            EnvironmentExp methodEnv = new EnvironmentExp(classEnv);
            this.params.verifyListParam(compiler, currentClass,def,methodEnv);
            def.setLabel(new Label(this.methodName.getName().getName()+"_"+currentClass.getType().getName().getName()));
            this.methodName.setDefinition(def);
            classEnv.declare(methodName.getName(), def);
            currentClass.incNumberOfMethods();
        } catch (ContextualError e) {
            throw e;
        } catch (EnvironmentExp.DoubleDefException d) {
            throw new ContextualError("method already defined",this.methodName.getLocation());
        }
        //if a method is overriden, checks if the signatures are the same
        EnvironmentExp superEnv = currentClass.getSuperClass().getMembers();
        if (superEnv.get(methodName.getName())!=null) {
            if (superEnv.get(methodName.getName()).isMethod()) {
                MethodDefinition superDef = (MethodDefinition) superEnv.get(methodName.getName());
                if ((!def.getType().sameType(superDef.getType()))|| (superDef.getSignature().size() != def.getSignature().size())) {
                    throw new ContextualError("method overrides method with different signature",this.getLocation());
                } else {
                    for (int i = 0; i < def.getSignature().size();i++) {
                        if (!def.getSignature().paramNumber(i).sameType(superDef.getSignature().paramNumber(i))) {
                            throw new ContextualError("method overrides method with different signature",this.getLocation());
                        }
                    }
                }
            }
        }
    }

   
    
    
    
    @Override
    protected void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError{
        EnvironmentExp methodEnv = new EnvironmentExp(currentClass.getMembers());
        this.params.verifyListParam(compiler, currentClass, null, methodEnv);
        Type t = this.type.verifyType(compiler);
        this.body.verifyMethodBody(compiler, currentClass, methodEnv, t);
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
        type.iter(f);
        methodName.iter(f);
        params.iter(f);
        body.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s,prefix,false);
        methodName.prettyPrint(s,prefix,false);
        params.prettyPrint(s,prefix,false);
        body.prettyPrint(s,prefix,true);
        
    }
    @Override 
    public void codeGenBody(DecacCompiler compiler,ListDeclField fields){
        ClassDefinition cDef = compiler.getClassDefinition();
        compiler.setMethodName(this.methodName.getName().getName());
        compiler.addLabel(((MethodDefinition)cDef.getMembers().getDico().get(this.methodName.getName())).getLabel());
        compiler.resetCompiler(this.params.size(),fields);
        if(!type.getType().isVoid())
            compiler.isNotVoid();
        params.fillVarTabl(compiler);
        this.body.codeGenMethodBody(compiler);
    }
}
