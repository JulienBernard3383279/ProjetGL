package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }
    
    

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t;
        try {
            t = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            //check for int to float conversion (thus the verifyRValue)
            this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, t);
        } catch (ContextualError e) {
            throw e;
        }
        this.setType(t);
        return t;
    }


    @Override
    public String getOperatorName() {
        return "=";
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Should not be called");
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal reg = this.getRightOperand().codeGen(compiler);
        DAddr addr  = this.getLeftOperand().getAddr(compiler);
        if(reg.isGPRegister()) {
            /*if(this.getRightOperand().getType().isFloat()&&this.getLeftOperand().getType().isInt()) {
                compiler.addInstruction(new );
            }*/
            compiler.addInstruction(new STORE((GPRegister)reg,addr));  
        }
        else if(reg.isRegisterOffset()) {
            compiler.addInstruction(new LEA(compiler.translate((RegisterOffset)reg),Register.R0));
            compiler.addInstruction(new STORE(Register.R0,addr)) ;
        }
        else 
            throw new UnsupportedOperationException("Should not be called");
        reg.free(compiler);
        return addr;
    }

}
