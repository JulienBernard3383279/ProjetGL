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
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;

/**
 *
 * @author bernajul
 */
public class New extends AbstractExpr {
    
    private AbstractIdentifier ident;
    
    public New(AbstractIdentifier ident) {
        this.ident=ident;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t;
        try {
            t = ident.verifyType(compiler);
        } catch (ContextualError e) {
            throw e;
        }
        if (! t.isClass()) {
            throw new ContextualError("identifier is not a class",this.ident.getLocation());
        }
        this.setType(t);
        return t;
    }
    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        ClassDefinition type = (ClassDefinition)compiler.getEnvTypes().get(this.getType().getName());
        DVal reg = compiler.allocRegister();
        if(reg.isGPRegister()) {
            compiler.addInstruction(new NEW(type.getNumberOfFields()+1,(GPRegister)reg));
            compiler.addInstruction(new BOV(compiler.getHeapOV()));
            compiler.addInstruction(new LEA(type.write(compiler).getAddr(),Register.R0));
            compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(0,(GPRegister)reg)));
            compiler.addInstruction(new PUSH((GPRegister)reg));
            compiler.addInstruction(new BSR(new Label("init."+ident.getName().getName())));
            compiler.addInstruction(new POP((GPRegister)reg));
        }
        else if(reg.isRegisterOffset()) {
            //VERFIER QUE NumberOfFileds CONTIENT BIEN LE NOMBRE DE VARIABLE DANS LA CLASS  
            compiler.addInstruction(new NEW(type.getNumberOfFields()+1,Register.R0));
            compiler.addInstruction(new PUSH(Register.R0));
            compiler.addInstruction(new BOV(compiler.getHeapOV()));
            compiler.addInstruction(new LEA(type.write(compiler).getAddr(),Register.R0));
            compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)reg),Register.R1));
            compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(0,Register.R1)));
            compiler.addInstruction(new BSR(new Label("init."+ident.getName().getName())));
            compiler.addInstruction(new POP(Register.R0));
            compiler.addInstruction(new STORE(Register.R0,compiler.translate((RegisterOffset)reg)));
        }
        else 
            throw new UnsupportedOperationException("Not supposed to be called");
        return reg;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        ident.decompile(s);
        s.print("()");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        ident.iter(f);
    }
    
}
