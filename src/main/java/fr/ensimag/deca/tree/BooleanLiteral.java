package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;

/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = new BooleanType(compiler.getSymbols().create("boolean"));
        this.setType(t);
        return t;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }
    @Override 
    protected DVal codeGen(DecacCompiler compiler) {
        DVal reg = compiler.allocRegister();
        int data = value ? 1 : 0;
        if(reg.isGPRegister()) {
            compiler.addInstruction(new LOAD(data,(GPRegister)reg));
        }
        else if(reg.isRegisterOffset()) {
            compiler.addInstruction(new LOAD(data,Register.R0));
            compiler.addInstruction(new STORE(Register.R0,compiler.translate((RegisterOffset)reg)));
            
        }
        else {
            throw new UnsupportedOperationException("Not supposed to be call");
        }
        return reg;
    }
    @Override
    protected void codeGenCond(DecacCompiler compiler,Label l,boolean jump) {
        if((value&&jump)||!value&&!jump) {
            compiler.addInstruction(new BRA(l));
        }
    }
}
