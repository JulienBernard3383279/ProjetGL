package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl58
 * @date 01/01/2017
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t;
        try {
            t = this.type.verifyType(compiler);
            VariableDefinition def = new VariableDefinition(t,this.type.getLocation());
            localEnv.declare(this.varName.getName(), def);
        } catch (ContextualError e) {
            throw e;
        } catch (DoubleDefException d) {
            throw new ContextualError("Variable deja defini",this.getLocation());
        }
        
        try {
            this.initialization.verifyInitialization(compiler, t, localEnv, currentClass);
        } catch (ContextualError i) {
            throw i;
        }
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
    @Override
    protected void codeGenVar(DecacCompiler compiler) {
        int [] regRead  =compiler.openRead();
        initialization.codeGen(compiler);
        if(regRead[0]!=-1) {
            //allocSucess
            //TODO ajouter la variable a la table des symboles
            compiler.addInstruction(new PUSH(Register.getR(regRead[0])));
        }
        else {
            //TODO Push value on top of stack 
        }
        compiler.closeRead();
    }
}
