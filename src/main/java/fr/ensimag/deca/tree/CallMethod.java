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
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import java.io.PrintStream;

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

    public AbstractIdentifier getName() {
        return name;
    }

    public ListExpr getArgs() {
        return args;
    }

    public void setName(AbstractIdentifier name) {
        this.name = name;
    }

    public void setArgs(ListExpr args) {
        this.args = args;
    }
    
    
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
