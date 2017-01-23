/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;

/**
 *
 * @author bernajul
 */
public class InstanceOf extends AbstractExpr {
    private AbstractExpr name;
    private AbstractIdentifier type;
    
    public InstanceOf(AbstractExpr expr, AbstractIdentifier type) {
        this.name=expr;
        this.type=type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        try {
            name.verifyExpr(compiler, localEnv, currentClass);
            Type ct = type.verifyType(compiler);
        } catch (ContextualError e) {
            throw e;
        }
        Type t = new BooleanType(compiler.getSymbols().create("boolean"));
        this.setType(t);
        return t;
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        return null;
        //Label instanceOfSucceeded = new Label("InstanceOf"+compiler.getInstanceOfCounter());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        name.decompile(s);
        s.print("instanceof");
        type.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        type.iter(f);
    }
    
}
