package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca Identifier
 *
 * @author gl58
 * @date 01/01/2017
 */
public class Identifier extends AbstractIdentifier {
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (localEnv.get(this.getName())==null) {
            throw new ContextualError("identifier not defined",this.getLocation());
        }
        this.setDefinition(localEnv.get(name));
        Type t = localEnv.get(this.getName()).getType();
        this.setType(t);
        return t;
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        if (compiler.getEnvTypes().get(this.getName()) == null) {
            throw new ContextualError("type undefined",this.getLocation());
        }
        this.setDefinition(compiler.getEnvTypes().get(name));
        Type t = compiler.getEnvTypes().get(this.getName()).getType();
        this.setType(t);
        return t;
    }
    
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

    /**
     *
     * @param compiler
     * @return address of the Identifier
     */
    @Override
    public DAddr getAddr(DecacCompiler compiler) {
        if(compiler.varExist(this.name.getName()))
            return compiler.getVarData(this.name.getName()).getOperand();
        else {
            if(!compiler.isInMethod())
                throw new UnsupportedOperationException("Shouldn't be called");
            else 
                return new RegisterOffset(((FieldDefinition)compiler.getClassDefinition().getMembers().get(this.name)).getIndex(),Register.getR(2)); 
        }
    }
    @Override
    protected DVal codeGenPrint(DecacCompiler compiler) {
        DAddr addr = this.getAddr(compiler);
        compiler.addInstruction(new LOAD(addr,Register.R1));
        if(super.getType().isInt())
            compiler.addInstruction(new WINT());
        else if(super.getType().isFloat()&&!compiler.getPrintHex()) 
            compiler.addInstruction(new WFLOAT());
        else if(super.getType().isFloat()&&compiler.getPrintHex())
            compiler.addInstruction(new WFLOATX());
        else 
            throw new UnsupportedOperationException("Shouldn't be called");
        return new NullOperand();
    }
    @Override 
    protected DVal codeGen(DecacCompiler compiler) {
        DVal reg = compiler.allocRegister();
        if(reg.isGPRegister()) {
            compiler.addInstruction(new LOAD(this.getAddr(compiler),(GPRegister)reg));
        }
        else if(reg.isRegisterOffset()){
            compiler.addInstruction(new LOAD(this.getAddr(compiler),Register.R0));
            compiler.addInstruction(new STORE(Register.R0,compiler.translate((RegisterOffset)reg)));
        }
        else 
            throw new UnsupportedOperationException("Shouldn't be called!");
        return reg;
    }
    @Override
    protected void codeGenCond(DecacCompiler compiler,Label l,boolean jump) {
        if(!super.getType().isBoolean()) {
            throw new UnsupportedOperationException("Can't call condition on int or float!");
        }
        else {
            compiler.addInstruction(new LOAD(this.getAddr(compiler),Register.R0));
            compiler.addInstruction(new CMP(new ImmediateInteger(1),Register.R0));
            if(jump){
                compiler.addInstruction(new BEQ(l));
            }
            else {
                compiler.addInstruction(new BNE(l));
            }
        }
    }
}
