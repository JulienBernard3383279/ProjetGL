package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SGT;
import fr.ensimag.ima.pseudocode.instructions.WINT;


/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }
    
    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        int []regRead = compiler.openRead();
        if(regRead[0]!=-1) {
            this.codeGenInst(compiler);
            compiler.addInstruction(new WINT());
        }
        else {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int []regRead1 = compiler.openRead();//lecture et écriture 
        int []regRead = compiler.openRead();
        super.getRightOperand().codeGenInst(compiler);
        int []regWrite = compiler.openWrite();//normally equals to regRead1
        super.getLeftOperand().codeGenInst(compiler);
        if(regRead1[0]!=-1) {
            //allocSucess 
            if(regRead[0]!=-1) {
                compiler.addInstruction(new CMP(Register.getR(regRead[0]),Register.getR(regWrite[0])));
                compiler.addInstruction(new SGT(Register.getR(regWrite[0])));
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
