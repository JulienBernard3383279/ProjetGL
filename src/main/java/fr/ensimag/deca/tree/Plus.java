package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;


/**
 * @author gl58
 * @date 01/01/2017
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        DVal regRight = this.getRightOperand().codeGen(compiler);
        DVal regLeft  = this.getLeftOperand().codeGen(compiler);
        if(regRight.isGPRegister()) {
            if(regLeft.isGPRegister())
                compiler.addInstruction(new ADD(regLeft,(GPRegister)regRight));
            else if(regLeft.isRegisterOffset())
                compiler.addInstruction(new ADD(compiler.translate((RegisterOffset)regLeft),(GPRegister)regRight));
            else 
                throw new UnsupportedOperationException("Not supposed to be call");
            compiler.addInstruction(new LOAD (regRight,Register.R1));
            regRight.free(compiler);
            regLeft.free(compiler);
            if(super.getType().isFloat()) {
                compiler.addInstruction(new WFLOAT());
            }
            else if(super.getType().isInt()) {
                compiler.addInstruction(new WINT());
            }
        }
        else if(regLeft.isGPRegister()) {
            if(regRight.isGPRegister())
                compiler.addInstruction(new ADD(regRight,(GPRegister)regLeft));
            else if(regRight.isRegisterOffset())
                compiler.addInstruction(new ADD(compiler.translate((RegisterOffset)regRight),(GPRegister)regLeft));
            else 
                throw new UnsupportedOperationException("Not supposed to be call");
            compiler.addInstruction(new LOAD (regLeft,Register.R1));
            regRight.free(compiler);
            regLeft.free(compiler);
            if(super.getType().isFloat()) {
                compiler.addInstruction(new WFLOAT());
            }
            else if(super.getType().isInt()) {
                compiler.addInstruction(new WINT());
            }
        }
        else
            throw new UnsupportedOperationException("Not supposed to be call");
        return new NullOperand();
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal regRight = this.getRightOperand().codeGen(compiler);
        DVal regLeft  = this.getLeftOperand().codeGen(compiler);
        if(regRight.isGPRegister()) {
            if(regLeft.isGPRegister())
                compiler.addInstruction(new ADD(regLeft,(GPRegister)regRight));
            else if(regLeft.isRegisterOffset())
                compiler.addInstruction(new ADD(compiler.translate((RegisterOffset)regLeft),(GPRegister)regRight));
            else 
                throw new UnsupportedOperationException("Not supposed to be call");
            compiler.addInstruction(new LOAD (regRight,Register.R1));
            regLeft.free(compiler);
            return regRight;
        }
        else if(regLeft.isGPRegister()) {
            if(regRight.isGPRegister())
                compiler.addInstruction(new ADD(regRight,(GPRegister)regLeft));
            else if(regRight.isRegisterOffset())
                compiler.addInstruction(new ADD(compiler.translate((RegisterOffset)regRight),(GPRegister)regLeft));
            else 
                throw new UnsupportedOperationException("Not supposed to be call");
            compiler.addInstruction(new LOAD (regLeft,Register.R1));
            regRight.free(compiler);
            return regLeft;
        }
        else
            throw new UnsupportedOperationException("Not supposed to be call");
    }
}
