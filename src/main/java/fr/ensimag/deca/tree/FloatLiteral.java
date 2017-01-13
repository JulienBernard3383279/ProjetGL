package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Single precision, floating-point literal
 *
 * @author gl58
 * @date 01/01/2017
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;

    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = new FloatType(compiler.getSymbols().create("float"));
        this.setType(t);
        return t;       
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toHexString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
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
        compiler.addInstruction(new LOAD(new ImmediateFloat(value),Register.R1));
        compiler.addInstruction(new FLOAT(Register.R1,Register.R1));
        compiler.addInstruction(new WFLOAT());
        return new NullOperand();
    }
    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal reg = compiler.allocRegister();
        if(reg.isGPRegister()) {
            compiler.addInstruction(new LOAD(new ImmediateFloat(value),(GPRegister)reg));
        }
        else if(reg.isRegisterOffset()) {
            compiler.addInstruction(new LOAD(new ImmediateFloat(value),Register.R0));
            compiler.addInstruction(new PUSH(Register.R0));
            
        }
        else {
            throw new UnsupportedOperationException("Not supposed to be call");
        }
        return reg;
    }

}