/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.Visibility;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;

/**
 *
 * @author bernajul
 */
public class Dot extends AbstractLValue {
    
    private AbstractExpr left;
    private AbstractIdentifier right;
    
    public Dot(AbstractExpr expr, AbstractIdentifier name) {
        this.left=expr;
        this.right=name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t;
        try {
            t = left.verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        } 
        if (! t.isClass()) {
            throw new ContextualError("left operand is not an instance of class",this.left.getLocation());
        }
        ClassType ct = (ClassType) t;
        try {
            right.verifyExpr(compiler, ct.getDefinition().getMembers(),currentClass);
        } catch (ContextualError e) {
            
        }
        if (ct.getDefinition().getMembers().get(right.getName())==null){
            throw new ContextualError("no such field in class",this.right.getLocation());
        }
        if (! ct.getDefinition().getMembers().get(right.getName()).isField()) {
            throw new ContextualError("identifier is not a field",this.right.getLocation());
        }
        //checks if visibility allows for field to be called
        FieldDefinition fieldDef = ct.getDefinition().getMembers().get(right.getName()).asFieldDefinition("",this.getLocation());
        if ((fieldDef.getVisibility()==Visibility.PROTECTED) && (currentClass.getMembers().get(right.getName())==null)) {
            throw new ContextualError("field is protected",this.right.getLocation());
        }
        Type ft = fieldDef.getType();
        this.setType(ft);
        return ft;
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        DVal regLeft = this.left.codeGen(compiler);
        ClassDefinition currentType=((ClassType) this.left.getType() ).getDefinition();
        FieldDefinition offsetTemp=(FieldDefinition) ((ClassType) this.left.getType() ).
                                            getDefinition().getMembers().getDico().get(right.getName());
        while(currentType.getSuperClass()!=null&&offsetTemp==null){
            currentType=currentType.getSuperClass();
            offsetTemp =    (FieldDefinition) ( currentType ).getMembers().getDico().get(right.getName());
        }
        int offset = offsetTemp.getIndex(); 
        if(regLeft.isGPRegister()) {
            compiler.addInstruction(new LOAD(new RegisterOffset(offset,(GPRegister)regLeft),(GPRegister)regLeft));
        }
        else if(regLeft.isRegisterOffset()) {
            compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regLeft),Register.R0));
            compiler.addInstruction(new LOAD(new RegisterOffset(offset,Register.R0),Register.R0));
            compiler.addInstruction(new STORE(Register.R0,compiler.translate((RegisterOffset)regLeft)));
        }
        else 
            throw new UnsupportedOperationException("Should not be called");
        return regLeft;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        left.decompile(s);
        s.print(".");
        right.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        left.prettyPrint(s,prefix,false);
        right.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.left.iter(f);
        this.right.iter(f);
    }

    public DAddr getAddr(DecacCompiler compiler) {
        DVal regLeft = this.left.codeGen(compiler);
        int offset = ((FieldDefinition)((ClassDefinition)(this.left).getDefinition()).getMembers().getDico().get(right.getName())).getIndex();
        if(regLeft.isGPRegister()) {
            return new RegisterOffset(offset,(GPRegister)regLeft); 
        }
        else if(regLeft.isRegisterOffset()) {
            compiler.allocR2();
            compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)regLeft),Register.getR(2)));
            return new RegisterOffset(offset,Register.getR(2));
        }
        else 
          throw new UnsupportedOperationException("Should not be called");  
    }

}