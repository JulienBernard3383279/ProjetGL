/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ConstructBEQ;
import fr.ensimag.deca.codegen.ConstructCMP;
import fr.ensimag.deca.codegen.codeGenBinaryInstructionDValToReg;
import fr.ensimag.deca.codegen.codeGenUnaryInstructionToLabel;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;

/**
 *
 * @author bernajul
 */
public class InstanceOf extends AbstractExpr {
    private AbstractExpr name;
    private AbstractIdentifier type;
    
    public InstanceOf(AbstractExpr expr, AbstractIdentifier type) {
        this.name=expr;
        this.type=type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        try {
            name.verifyExpr(compiler, localEnv, currentClass);
            Type ct = type.verifyType(compiler);
        } catch (ContextualError e) {
            throw e;
        }
        Type t = new BooleanType(compiler.getSymbols().create("boolean"));
        this.setType(t);
        return t;
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        Label instanceOfBeginning = new Label("InstanceOfBegin"+compiler.getInstanceOfCounter());
        Label instanceOfSucceeded = new Label("InstanceOfSuccess"+compiler.getInstanceOfCounter());
        
        
        DVal reg = name.codeGen(compiler);
        
        if(reg.isGPRegister()) {
            compiler.addInstruction(new LOAD(new RegisterOffset(0,(GPRegister) reg), Register.R0));  
        }
        else if(reg.isRegisterOffset()) {
            compiler.addInstruction(new LOAD(compiler.translate( (RegisterOffset) reg),Register.R0));
            compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.R0), Register.R0));
        }
        
        DAddr addr = type.getClassDefinition().write(compiler).getAddr();
        
        compiler.addLabel(instanceOfBeginning);
        
        compiler.addInstruction(new CMP(addr,Register.R0));
        compiler.addInstruction(new SEQ(Register.R1));
        compiler.addInstruction(new CMP(new ImmediateInteger(1),Register.R1));
        compiler.addInstruction(new BEQ(instanceOfSucceeded));
        compiler.addInstruction(new LOAD(Register.R0,Register.R0));
        compiler.addInstruction(new CMP(new NullOperand(), Register.R0));
        compiler.addInstruction(new BEQ(instanceOfSucceeded));
        compiler.addInstruction(new BRA(instanceOfBeginning));
        
        compiler.addLabel(instanceOfSucceeded);
        
        if (reg.isGPRegister()) {
            compiler.addInstruction(new LOAD(Register.R1,(GPRegister) reg));
        }
        else if ( reg.isRegisterOffset() ) {
            compiler.addInstruction(new STORE(Register.R1,compiler.translate( (RegisterOffset) reg )));
        }
        
        return reg;
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, Label l, boolean jump) {
        DVal reg = this.codeGen(compiler);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1),Register.R0));
        codeGenBinaryInstructionDValToReg.generate(compiler,
                super.getType(),
                new ConstructCMP(),
                reg,
                Register.R0);
        codeGenUnaryInstructionToLabel.generate(compiler,
                l,
                jump,
                new ConstructBEQ());
        reg.free(compiler);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        name.decompile(s);
        s.print("instanceof");
        type.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        type.iter(f);
    }
    
}
