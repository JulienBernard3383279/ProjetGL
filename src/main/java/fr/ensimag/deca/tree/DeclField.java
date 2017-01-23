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
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author gl58
 */
public class DeclField extends AbstractDeclField{
    
    public Visibility visib;
    public AbstractIdentifier type;
    public AbstractIdentifier fieldName;
    public AbstractInitialization init;
    
    public DeclField(Visibility visib, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization init) {
        this.visib=visib;
        this.type=type;
        this.fieldName=fieldName;
        this.init=init;
    }
    /**
     * Verification d'un attribut (Passe 2)
     * @param compiler 
     * @param currentClass
     * @param index
     */
    @Override
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass,int index) throws ContextualError {
        Type t;
        EnvironmentExp classEnv = currentClass.getMembers();
        FieldDefinition def;
        try {
            t = this.type.verifyType(compiler);
            ExpDefinition superDef = classEnv.get(fieldName.getName());
            if (superDef != null) {
                //in case field overrides another, the index must be transfered
                if (superDef.isField()) {
                    FieldDefinition superDef2 = (FieldDefinition)superDef;
                    def = new FieldDefinition(t,this.type.getLocation(),this.visib,currentClass,superDef2.getIndex());
                } else {
                    //otherwise, index has the value of the number of fields in the hierarchy
                    def = new FieldDefinition(t,this.type.getLocation(),this.visib,currentClass,index);
                    currentClass.incNumberOfFields();
                    //index = index + 1;
                }
            } else {
                def = new FieldDefinition(t,this.type.getLocation(),this.visib,currentClass,index);
                currentClass.incNumberOfFields();
                //index = index + 1;
            }
            this.fieldName.setDefinition(def);
            classEnv.declare(this.fieldName.getName(), def);
        } catch (ContextualError e) {
            throw e;
        } catch (EnvironmentExp.DoubleDefException d) {
            throw new ContextualError("field is already defined",this.getLocation());
        }
        
        if (t.isVoid()) {
                throw new ContextualError("field cannot be void",this.getLocation());
        }
        
        try {
            this.init.verifyInitialization(compiler, t, classEnv, currentClass);
        } catch (ContextualError i) {
            throw i;
        }
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        if (visib.equals(Visibility.PROTECTED)) {
            s.print("protected ");
        }
        type.decompile(s);
        s.print(" ");
        fieldName.decompile(s);
        init.decompile(s);
        s.print(";");
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
        init.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s,String prefix) {
        type.prettyPrint(s,prefix,false);
        fieldName.prettyPrint(s,prefix,false);
        init.prettyPrint(s,prefix,false);
    }
}
