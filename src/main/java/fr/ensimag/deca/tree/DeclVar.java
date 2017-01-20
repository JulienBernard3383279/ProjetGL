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
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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

    public AbstractInitialization getInitialization() {
        return initialization;
    }

    @Override
    public Location getLocation() {
        return super.getLocation(); //To change body of generated methods, choose Tools | Templates.
    }

    public AbstractIdentifier getType() {
        return type;
    }

    public AbstractIdentifier getVarName() {
        return varName;
    }
    
    

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
        if(compiler.getEnvTypes().get(this.type.getName()).isType()) {
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
                initVal.free(compiler);
            }

        }
        else if(compiler.getEnvTypes().get(this.type.getName()).isClass()) {
            ClassDefinition type = (ClassDefinition)compiler.getEnvTypes().get(this.type.getName());
            DAddr addr = compiler.allocateVar();
            DVal reg = compiler.allocRegister();
            if(reg.isGPRegister()) {
                compiler.addInstruction(new NEW(type.getNumberOfFields()+1,(GPRegister)reg));
                compiler.addInstruction(new BOV(compiler.getHeapOV()));
                compiler.addInstruction(new LEA(type.write(compiler).getAddr(),Register.R0));
                compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(0,(GPRegister)reg)));
                compiler.addInstruction(new PUSH((GPRegister)reg));
                //compiler.addInstruction(new BSR(type.getInitMethod()));
                compiler.addInstruction(new POP((GPRegister)reg));
                compiler.addInstruction(new STORE((GPRegister)reg,addr));
            }
            else if(reg.isRegisterOffset()) {
                //VERFIER QUE NumberOfFileds CONTIENT BIEN LE NOMBRE DE VARIABLE DANS LA CLASS  
                compiler.addInstruction(new NEW(type.getNumberOfFields()+1,Register.R0));
                compiler.addInstruction(new PUSH(Register.R0));
                compiler.addInstruction(new BOV(compiler.getHeapOV()));
                compiler.addInstruction(new LEA(type.write(compiler).getAddr(),Register.R0));
                compiler.addInstruction(new LOAD(compiler.translate((RegisterOffset)reg),Register.R1));
                compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(0,Register.R1)));
                //compiler.addInstruction(new BSR(type.getInitMethod()));
                compiler.addInstruction(new POP(Register.R0));
                compiler.addInstruction(new STORE(Register.R0,addr));
            }
            else 
                throw new UnsupportedOperationException("Not supposed to be called");
        }
    }
}
