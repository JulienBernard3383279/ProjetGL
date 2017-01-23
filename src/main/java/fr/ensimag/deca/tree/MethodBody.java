/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;

/**
 *
 * @author pierre
 */
public class MethodBody extends AbstractMethodBody{
    ListDeclVar decls;
    ListInst insts;
    
    public MethodBody(ListDeclVar decls, ListInst insts) {
        this.decls=decls;
        this.insts=insts;
    }
    
    @Override 
    protected void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass, EnvironmentExp localEnv, Type returnType) throws ContextualError{
        try {
            this.decls.verifyListDeclVariable(compiler, localEnv, currentClass);
            this.insts.verifyListInst(compiler, localEnv, currentClass, returnType);
        } catch (ContextualError e) {
            throw e;
        }
    }
    
    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        Label l = new Label("fin."+compiler.getMethodName());
        compiler.setEndMethodLabel(l);
        TSTO tsto_inst=new TSTO(0);
        compiler.addInstruction(tsto_inst);
        compiler.addInstruction(new BOV(compiler.getStackOV()));
        compiler.setSaveRegisterFlag( compiler.createFlag());
        compiler.allocR2();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB),Register.getR(2)));
        for(AbstractInst a : insts.getList()) {
            a.codeGenInst(compiler);
        }
        int [] regUsedList = compiler.getUsedRegister();
        for(int i : regUsedList) {
            if(i!=-1) {
                compiler.addToFlag(compiler.getSaveRegisterFlag(),new PUSH(Register.getR(i)));
                compiler.incOverFlow();
            }
        }
        int j;
        for(j=0;j<regUsedList.length;j++) {
            int i=regUsedList[regUsedList.length-1-j];
            if(i!=-1) {
                compiler.addInstruction(new POP(Register.getR(i)));
                compiler.decOverFlow();
            }
        }
        if(compiler.hasReturn()) {
            compiler.addInstruction(new BRA(l));
            compiler.addInstruction(new WSTR("Erreur : sortie de la methode A.getX sans return"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
        compiler.addLabel(l);
        compiler.addInstruction(new RTS());
        compiler.writeFlag(compiler.getSaveRegisterFlag());
        tsto_inst.setValue(compiler.argTSTO());
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        decls.decompile(s);
        insts.decompile(s);
        s.print("}");
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        decls.iter(f);
        insts.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        decls.prettyPrint(s,prefix,false);
        insts.prettyPrint(s,prefix,true);
    }
}
