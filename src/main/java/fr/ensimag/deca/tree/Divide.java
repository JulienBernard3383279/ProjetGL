package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;


/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }
    protected DVal codeGenPrint(DecacCompiler compiler) {
        int []regRead = compiler.openRead();
        this.codeGenInst(compiler);
        //chercher le type dans les definition
        if(regRead[0]!=-1)
            if(regRead[0]!=1)//avoid line LOAD R1,R1
                compiler.addInstruction(new LOAD(Register.getR(regRead[0]),Register.getR(1)));
        else if(regRead[0]==-1)
            throw new UnsupportedOperationException("not yet implemented");
        if(super.getType().isFloat()) {
            compiler.addInstruction(new WFLOAT());
        }
        else if(super.getType().isInt()) {
            compiler.addInstruction(new WINT());
        }
        else {
            throw new UnsupportedOperationException("Can't print object of type: "
                    +super.getType().getName().getName());
        }
        compiler.closeRead();
        compiler.closeRead();
        return new NullOperand();
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        if(super.getType().isFloat()) {
            codeGenFloat(compiler);
        }
        else if(super.getType().isInt()) {
            codeGenInt(compiler);
        }
        return new NullOperand();
    }
    private void codeGenInt(DecacCompiler compiler) {
        int []regRead1 = compiler.openRead();//lecture et écriture 
        int []regRead = compiler.openRead();
        super.getRightOperand().codeGenInst(compiler);
        int []regWrite = compiler.openWrite();//normally equals to regRead1
        super.getLeftOperand().codeGenInst(compiler);
        if(regRead1[0]!=-1) {
            //allocSucess 
            if(regRead[0]!=-1) {
                compiler.addInstruction(new QUO(Register.getR(regRead[0]),Register.getR(regWrite[0])));
            }
            else {
                throw new UnsupportedOperationException("not yet implemented");
            }
        }
        else {
            throw new UnsupportedOperationException("not yet implemented");
        }
        compiler.closeRead();
        compiler.closeWrite();
        compiler.closeRead();
    }
    private void codeGenFloat(DecacCompiler compiler) {
        int []regRead1 = compiler.openRead();//lecture et écriture 
        int []regRead = compiler.openRead();
        super.getRightOperand().codeGenInst(compiler);
        int []regWrite = compiler.openWrite();//normally equals to regRead1
        super.getLeftOperand().codeGenInst(compiler);
        if(regRead1[0]!=-1) {
            //allocSucess 
            if(regRead[0]!=-1) {
                compiler.addInstruction(new DIV(Register.getR(regRead[0]),Register.getR(regWrite[0])));
            }
            else {
                throw new UnsupportedOperationException("not yet implemented");
            }
        }
        else {
            throw new UnsupportedOperationException("not yet implemented");
        }
        compiler.closeRead();
        compiler.closeWrite();
        compiler.closeRead();
    }

}
