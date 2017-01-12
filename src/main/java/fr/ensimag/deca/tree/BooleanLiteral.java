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
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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
        
        return new BooleanType(null);
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
            compiler.addInstruction(new PUSH(Register.R0));
            
        }
        else {
            throw new UnsupportedOperationException("Not supposed to be call");
        }
        return reg;
    }
}
