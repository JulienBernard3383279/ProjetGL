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
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import java.io.PrintStream;
import java.util.Iterator;

/**
 *
 * @author bernajul
 */
public class CallMethod extends AbstractExpr {
    
    private AbstractIdentifier name;
    private ListExpr args;
    
    public CallMethod(AbstractIdentifier name, ListExpr args) {
        this.name=name;
        this.args=args;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t;
        try {
            t = name.verifyExpr(compiler,currentClass.getMembers(),currentClass);
            if (currentClass.getMembers().get(name.getName())==null) {
                throw new ContextualError("no such method in class",this.name.getLocation());
            }
            if (! currentClass.getMembers().get(name.getName()).isMethod()) {
                throw new ContextualError("identifier is not a method",this.name.getLocation());
            }
            MethodDefinition def = currentClass.getMembers().get(name.getName()).asMethodDefinition("",this.getLocation());
            Signature sig = def.getSignature();
            if (sig.size()!=args.size()) {
                throw new ContextualError("number of parameters does not match signature",this.getLocation());
            }
            Iterator<AbstractExpr> it = this.args.iterator();
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
        this.setType(t);
        return t;
    }

    @Override
    protected DVal codeGen(DecacCompiler compiler) {
        if(compiler!=null)
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        else {
            compiler =new DecacCompiler(null,null);
            Label l = ((MethodDefinition)(compiler.getEnvTypes().get(name))).getLabel();
            compiler.addInstruction(new BSR(l));
            return null;
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        name.decompile(s);
        s.print("(");
        args.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s,prefix,false);
        args.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
