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
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author pierre
 */
public class DeclField extends AbstractDeclField{
    
    public Visibility visib;
    public AbstractIdentifier type;
    public AbstractIdentifier fieldName;
    public AbstractInitialization init;
    
    /**
     * Verification d'un attribut (Passe 2)
     * @param compiler 
     * @param currentClass
     * @param index
     */
    @Override
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass,int index) throws ContextualError {
        Type t;
        EnvironmentExp localEnv = currentClass.getMembers();
        try {
            t = this.type.verifyType(compiler);
            FieldDefinition def = new FieldDefinition(t,this.type.getLocation(),this.visib,currentClass,index);
            this.fieldName.setDefinition(def);
            localEnv.declare(this.fieldName.getName(), def);
        } catch (ContextualError e) {
            throw e;
        } catch (EnvironmentExp.DoubleDefException d) {
            throw new ContextualError("field is already defined",this.getLocation());
        }
        
        if (t.isVoid()) {
                throw new ContextualError("field cannot be void",this.getLocation());
        }
        
        try {
            this.init.verifyInitialization(compiler, t, localEnv, currentClass);
        } catch (ContextualError i) {
            throw i;
        }
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s,String prefix) {
        
    }
}
