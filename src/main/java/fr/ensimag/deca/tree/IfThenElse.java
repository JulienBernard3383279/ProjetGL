package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl58
 * @date 01/01/2017
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    public void updateElseBranch(ListInst newElseBranch) {
        this.elseBranch=newElseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        try {
            this.condition.verifyCondition(compiler, localEnv, currentClass);
            this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
            this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        } catch (ContextualError e) {
            throw e;
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label debutElseLabel = new Label("debutElse"+compiler.getFiCounter());
        Label finElseLabel = new Label("finElse"+compiler.getElseCounter());
        this.condition.codeGenCond(compiler,debutElseLabel,false);
        this.thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(finElseLabel));
        compiler.addLabel(debutElseLabel);
        this.elseBranch.codeGenListInst(compiler);
        compiler.addLabel(finElseLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile(s);
        s.println(") {");s.indent();
        thenBranch.decompile(s);
        s.unindent();s.println("} else {");s.indent();
        elseBranch.decompile(s);
        s.unindent();s.print("} ");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
