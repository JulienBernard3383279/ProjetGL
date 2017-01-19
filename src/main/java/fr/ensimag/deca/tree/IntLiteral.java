package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author gl58
 * @date 01/01/2017
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = new IntType(compiler.getSymbols().create("int"));
        this.setType(t);
        return t;
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
    protected DVal codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(value,Register.R1));
        compiler.addInstruction(new WINT());
        return new NullOperand();
    }
    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal reg = compiler.allocRegister();
        if(reg.isGPRegister()) {
            compiler.addInstruction(new LOAD(value,(GPRegister)reg));
        }
        else if(reg.isRegisterOffset()) {
            compiler.addInstruction(new LOAD(value,Register.R0));
            compiler.addInstruction(new STORE(Register.R0,compiler.translate((RegisterOffset)reg)));
            
        }
        else {
            throw new UnsupportedOperationException("Not supposed to be call");
        }
        return reg;
    }

}
