package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;


/**
 * @author gl58
 * @date 01/01/2017
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        int []regRead = compiler.openRead();
        this.codeGenInst(compiler);
        //chercher le type dans les definition
        if(regRead[0]!=-1)
            if(regRead[0]!=1)//avoid line LOAD R1,R1
                compiler.addInstruction(new LOAD(Register.getR(regRead[0]),Register.getR(1)));
        else if(regRead[0]==-1)
            throw new UnsupportedOperationException("not yet implemented");
        //TODO choose if display float or int 
        //if(float)
        compiler.addInstruction(new WFLOAT());
        //if(int)
        compiler.addInstruction(new WINT());
        compiler.closeRead();
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //TODO ietyftg
        int []regRead1 = compiler.openRead();//lecture et écriture 
        int []regRead = compiler.openRead();
        super.getRightOperand().codeGenInst(compiler);
        int []regWrite = compiler.openWrite();//normally equals to regRead1
        super.getLeftOperand().codeGenInst(compiler);
        if(regRead1[0]!=-1) {
            //allocSucess 
            if(regRead[0]!=-1) {
                compiler.addInstruction(new SUB(Register.getR(regRead[0]),Register.getR(regWrite[0])));
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
