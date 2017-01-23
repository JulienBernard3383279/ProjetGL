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
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;
import java.util.Iterator;

/**
 *
 * @author bernajul
 */
public class CallMethod extends AbstractExpr {
    
    private AbstractIdentifier name;
    private ListExpr args;
    
    public CallMethod(AbstractIdentifier name, ListExpr args) {
        this.name=name;
        this.args=args;
    }


    public AbstractIdentifier getName() {
        return name;
    }

    public ListExpr getArgs() {
        return args;
    }

    public void setName(AbstractIdentifier name) {
        this.name = name;
    }

    public void setArgs(ListExpr args) {
        this.args = args;
    }
    
    

    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t;
        Type tbis;
        try {
            if (currentClass == null) {
                throw new ContextualError("direct method call in main",this.name.getLocation());
            }
            EnvironmentExp classEnv = currentClass.getMembers();
            t = name.verifyExpr(compiler,classEnv,currentClass);
            //check that identifier is method
            if (! classEnv.get(name.getName()).isMethod()) {
                throw new ContextualError("identifier is not a method",this.name.getLocation());
            }
            MethodDefinition def = classEnv.get(name.getName()).asMethodDefinition("",this.name.getLocation());
            Signature sig = def.getSignature();
            if (sig.size()!=args.size()) {
                throw new ContextualError("number of parameters does not match signature",this.name.getLocation());
            }
            Iterator<AbstractExpr> it = this.args.iterator();
            int index = 0;
            while (it.hasNext()) {
                AbstractExpr e = it.next();
                tbis = e.verifyExpr(compiler,localEnv,currentClass);
                //special case where the types are classes (subclasses can be called when a superclass is expected)
                if (tbis.isClass()){
                    if (!sig.paramNumber(index).isClass()) {
                        throw new ContextualError("parameter type does not match signature",e.getLocation());
                    } else {
                        ClassType ct = (ClassType) tbis;
                        if (!ct.isChild(sig.paramNumber(index))) {
                            throw new ContextualError("class type in call not subclass of class type in signature",e.getLocation());
                        }
                    }
                } else {
                    //possible int to float conversion
                    e = e.verifyRValue(compiler, localEnv, currentClass,sig.paramNumber(index));
                }
                index = index + 1; 
            }
        } catch (ContextualError e) {
            throw e;
        }
        this.setType(t);
        return t;
    }
    protected void codeGenCond(DecacCompiler compiler,Label l,boolean jump) {
        DVal reg = this.codeGen(compiler);
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
    protected DVal codeGenDotted(DecacCompiler compiler) {
        for(AbstractExpr a : this.args.getList()) {
            DVal reg = a.codeGen(compiler);
            compiler.incOverFlow();
            if(reg.isGPRegister()) {
                compiler.addInstruction(new PUSH((GPRegister)reg));
            }
            else if(reg.isRegisterOffset()) {
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)reg),Register.R0));
                compiler.addInstruction(new PUSH(Register.R0));
            }
            else {
                compiler.addInstruction(new LOAD(reg,Register.R0));
                compiler.addInstruction(new PUSH(Register.R0));
            }
        }
        compiler.addInstruction(new BSR(this.name.getMethodDefinition().getLabel()));
        DVal reg =null;
        if(!this.getType().isVoid()){
            reg = compiler.allocRegister();
            if(reg.isGPRegister()) {
                compiler.addInstruction(new LOAD(Register.R0,(GPRegister)reg));
            }
            else if(reg.isRegisterOffset()) {
                compiler.addInstruction(new STORE(Register.R0,compiler.translate((RegisterOffset)reg)));
            }
            else 
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
        for(AbstractExpr a : this.args.getList()) {
            compiler.addInstruction(new POP(Register.R0));
            compiler.decOverFlow();
        }
        return reg;
    }
    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(new NullOperand(),Register.R0));
        compiler.addInstruction(new PUSH(Register.R0));
        for(AbstractExpr a : this.args.getList()) {
            DVal reg = a.codeGen(compiler);
            if(reg.isGPRegister()) {
                compiler.addInstruction(new PUSH((GPRegister)reg));
            }
            else if(reg.isRegisterOffset()) {
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)reg),Register.R0));
                compiler.addInstruction(new PUSH(Register.R0));
            }
            else {
                compiler.addInstruction(new LOAD(reg,Register.R0));
                compiler.addInstruction(new PUSH(Register.R0));
            }
        }
        compiler.addInstruction(new BSR(this.name.getMethodDefinition().getLabel()));
        DVal reg =null;
        if(!this.getType().isVoid()){
            reg = compiler.allocRegister();
            if(reg.isGPRegister()) {
                compiler.addInstruction(new LOAD(Register.R0,(GPRegister)reg));
            }
            else if(reg.isRegisterOffset()) {
                compiler.addInstruction(new STORE(Register.R0,compiler.translate((RegisterOffset)reg)));
            }
            else 
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
        for(AbstractExpr a : this.args.getList()) {
            compiler.addInstruction(new POP(Register.R0));
        }
        return reg;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        name.decompile(s);
        s.print("(");
        args.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s,prefix,false);
        args.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        args.iter(f);
    }
}
