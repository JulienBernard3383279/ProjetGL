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
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import java.io.PrintStream;

/**
 *
 * @author bernajul
 */
public class Return extends AbstractInst {
    private AbstractExpr expr;
    
    public Return(AbstractExpr expr) {
        this.expr=expr;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        Type t;
        try {
            t = this.expr.verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }
        
        if (! t.sameType(returnType)) {
            throw new ContextualError("type of expression must match method type",this.expr.getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal value = this.expr.codeGen(compiler);
        if(value.isGPRegister())
            compiler.addInstruction(new LOAD(value,Register.R0));
        else if(value.isRegisterOffset()) 
            compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)value),Register.R0));
        compiler.addInstruction(new BRA(compiler.getEndMethodLabel()));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        expr.decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
