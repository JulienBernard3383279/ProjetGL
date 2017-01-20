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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        return null; //Pas encore implémenté
    }

}