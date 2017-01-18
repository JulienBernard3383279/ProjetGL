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
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import java.io.PrintStream;
import java.util.Iterator;

/**
 *
 * @author bernajul
 */
public class DotMethod extends AbstractExpr {
    private AbstractExpr instance;
    private AbstractIdentifier name;
    private ListExpr params;
    
    public DotMethod(AbstractExpr expr, AbstractIdentifier name, ListExpr list) {
        this.instance=expr;
        this.name=name;
        this.params=list;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t;
        ClassType ct;
        try {
            t = instance.verifyExpr(compiler, localEnv, currentClass);
            if (! t.isClass()) {
                throw new ContextualError("expression is not instance of a class",this.instance.getLocation());
            }
            ct = (ClassType) t;
            if (ct.getDefinition().getMembers().get(name.getName())==null) {
                throw new ContextualError("no such method in class",this.name.getLocation());
            }
            if (! ct.getDefinition().getMembers().get(name.getName()).isMethod()) {
                throw new ContextualError("identifier is not a method",this.name.getLocation());
            }
            MethodDefinition def = ct.getDefinition().getMembers().get(name.getName()).asMethodDefinition("",this.getLocation());
            Signature sig = def.getSignature();
            if (sig.size()!=params.size()) {
                throw new ContextualError("number of parameters does not match signature",this.getLocation());
            }
            Iterator<AbstractExpr> it = this.params.iterator();
            int index = 0;
            while (it.hasNext()) {
                AbstractExpr e = it.next();
                t = e.verifyExpr(compiler,localEnv,currentClass);
                if (! t.sameType(sig.paramNumber(index))) {
                    throw new ContextualError("parameter type does not match signature",e.getLocation());
                }
                index = index + 1;
            }
        } catch (ContextualError e) {
            throw e;
        }
        this.setType(ct);
        return ct;
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instance.decompile(s);
        s.print(".");
        name.decompile(s);
        s.print("(");
        params.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instance.prettyPrint(s,prefix,false);
        name.prettyPrint(s,prefix,false);
        params.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}