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
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import java.io.PrintStream;

/**
 *
 * @author bernajul
 */
public class This extends AbstractExpr {

    public This() {}
    
    @Override
    Definition getDefinition() {
        return def;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if (currentClass == null ){
            throw new ContextualError("cannot call this in main",this.getLocation());
        }
        ClassType t = currentClass.getType();
        this.setType(t);
        this.def = currentClass;
        return t;
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        return new RegisterOffset(-2,Register.LB);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        s.print(prefix);
        s.println();
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //leaf node
    }
    
}
