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
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import java.io.PrintStream;

/**
 *
 * @author bernajul
 */
public class CastExpr extends AbstractExpr{
    
    private AbstractIdentifier type;
    private AbstractExpr expr;
    
    public CastExpr(AbstractIdentifier type, AbstractExpr expr) {
        this.type=type;
        this.expr=expr;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t1, t2;
        try {
            t1 = type.verifyType(compiler);
            t2 = expr.verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }
        
        if (!t1.isClass() && !t2.isClass() && !t1.isFloat() && !t2.isInt()) {
            throw new ContextualError("incompatible types for cast",this.getLocation());
        } 
        if (t1.isClass() && !t2.isClass()) {
            throw new ContextualError("type cast to class",this.getLocation());
        }
        if (!t1.isClass() && t2.isClass()) {
            throw new ContextualError("class cast to type",this.getLocation());
        }
        if (t1.isClass() && t2.isClass()) {
            ClassType ct1 = (ClassType)t1;
            ClassType ct2 = (ClassType)t2;
            if (! ct1.getDefinition().isCastCompatible(ct2.getDefinition())) {
                throw new ContextualError("classes incompatible for cast",this.getLocation());
            }
        }
        this.setType(t1);
        return t1;
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s,prefix,false);
        expr.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }
    
}
