/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import java.io.PrintStream;
import java.util.Iterator;

/**
 *
 * @author bernajul
 */
public class DotMethod extends AbstractExpr {
    private AbstractExpr instance;
    private CallMethod method;
    
    public DotMethod(AbstractExpr expr, AbstractIdentifier name, ListExpr list) {
        this.instance=expr;
        this.method=new CallMethod(name,list);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError { 
        Type t;
        ClassType ct;
        MethodDefinition def;
        try {
            t = instance.verifyExpr(compiler, localEnv, currentClass);
            if (! t.isClass()) {
                throw new ContextualError("expression is not instance of a class",this.instance.getLocation());
            }
            ct = (ClassType) t;
            t = method.verifyExpr(compiler, localEnv, ct.getDefinition());
        } catch (ContextualError e) {
            throw e;
        }    
        this.setType(t);
        return t; 
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        compiler.addInstruction(new LEA(((Identifier)this.instance).getAddr(compiler),Register.R0));
        compiler.addInstruction(new PUSH(Register.R0));
        DVal reg = method.codeGenDotted(compiler);
        return reg;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instance.decompile(s);
        s.print(".");
        method.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instance.prettyPrint(s,prefix,false);
        method.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}