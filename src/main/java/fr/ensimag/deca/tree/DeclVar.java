package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
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
            this.varName.setDefinition(def);
            localEnv.declare(this.varName.getName(), def);
        } catch (ContextualError e) {
            throw e;
        } catch (DoubleDefException d) {
            throw new ContextualError("variable is already defined",this.getLocation());
        }
        
        if (t.isVoid()) {
                throw new ContextualError("variable cannot be void",this.getLocation());
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
        VariableDefinition customDefinition=new VariableDefinition( this.type.getDefinition().getType(), this.getLocation() );
        DAddr resultAllocate = compiler.allocateVar();
        customDefinition.setOperand(resultAllocate);
        compiler.addVarToTable(this.varName.getName().getName(),customDefinition);
        if (this.initialization.isInitialization()) {
            DVal initVal = this.initialization.codeGen(compiler);
            if(initVal.isGPRegister()) {
                compiler.addInstruction(new STORE((GPRegister)initVal,resultAllocate)); 
            }
            else if(initVal.isRegisterOffset()) {
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)initVal),Register.R0));
                compiler.addInstruction(new STORE(Register.R0,resultAllocate));
            }
            else {
                throw new UnsupportedOperationException("Not supposed to be called"); 
            }
        }               
    }
}
