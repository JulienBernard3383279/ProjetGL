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
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import java.io.PrintStream;
import java.util.Iterator;
import org.antlr.v4.runtime.Token;

/**
 *
 * @author bernajul
 */
public class DotMethod extends AbstractExpr {
    private AbstractExpr instance;
    private CallMethod method;
    
    public DotMethod(AbstractExpr expr, CallMethod method) {
        this.instance=expr;
        this.method=method;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError { 
        Type t;
        ClassType ct;
        MethodDefinition def;
        try {
            t = instance.verifyExpr(compiler, localEnv, currentClass);
            if (! t.isClass()) {
                throw new ContextualError("expression not instance of a class",this.instance.getLocation());
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
        compiler.addInstruction(new LOAD(((Identifier)this.instance).getAddr(compiler),Register.R0));
        compiler.addInstruction(new PUSH(Register.R0));
        DVal reg = method.codeGenDotted(compiler);
        return reg;
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(((Identifier)this.instance).getAddr(compiler),Register.R0));
        compiler.addInstruction(new PUSH(Register.R0));
        DVal reg = method.codeGenDotted(compiler);
        if(reg.isGPRegister()) {
            compiler.addInstruction(new LOAD(reg,Register.R1));
        }
        else if(reg.isRegisterOffset()) {
            compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)reg),Register.R1));
        }
        else 
            throw new UnsupportedOperationException("Not supposed to be called");
        if(this.getType().isInt()) {
            compiler.addInstruction(new WINT());
        }
        else if(this.getType().isFloat()) {
            compiler.addInstruction(new WFLOAT());
        }
        else 
            throw new UnsupportedOperationException("Not supposed to be called");
        return reg;
    }
    @Override 
    protected void codeGenCond(DecacCompiler compiler,Label l,boolean jump) {
        compiler.addInstruction(new LOAD(((Identifier)this.instance).getAddr(compiler),Register.R0));
        compiler.addInstruction(new PUSH(Register.R0));
        DVal reg = method.codeGenDotted(compiler);
        if(jump) {
            if(reg.isGPRegister()) {
                compiler.addInstruction(new CMP(1,(GPRegister)reg));
                compiler.addInstruction(new BEQ(l));
            }
        }
        else {
            compiler.addInstruction(new CMP(0,(GPRegister)reg));
            compiler.addInstruction(new BEQ(l));
        }
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
        instance.iter(f);
        method.iter(f);
    }
}