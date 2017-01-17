package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.context.TypeDefinition;
/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl58
 * @date 01/01/2017
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {     
        Type t = new FloatType(compiler.getSymbols().create("float"));    
        this.setType(t);
        return t;        
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }
    @Override 
    protected DVal codeGen(DecacCompiler compiler) {
        DVal reg = super.getOperand().codeGen(compiler);
        if(reg.isGPRegister()) {
            compiler.addInstruction(new FLOAT(reg,(GPRegister)reg));
            return reg;
        }
        else if(reg.isRegisterOffset()) {
            compiler.addInstruction(new FLOAT(reg,Register.R0));
            compiler.addInstruction(new STORE(Register.R0,compiler.translate((RegisterOffset)reg)));
            return reg;
        }
        else 
            throw new UnsupportedOperationException("Not supposed to be called");       
    }
}
