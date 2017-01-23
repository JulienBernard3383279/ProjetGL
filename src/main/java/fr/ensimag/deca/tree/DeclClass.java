package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl58
 * @date 01/01/2017
 */
public class DeclClass extends AbstractDeclClass {
    
    //modif
    public DeclClass(AbstractIdentifier className, 
            AbstractIdentifier superClass,
            ListDeclField field,
            ListDeclMethod methods) {
        this.className = className;
        this.superClass = superClass;
        this.field = field;
        this.methods = methods;
    }
    //fin modif
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        this.className.decompile(s);
        s.print(" extends ");
        this.superClass.decompile(s);
        s.println(" {"); s.indent();
        this.field.decompile(s);
        this.methods.decompile(s);
         s.unindent(); s.println(" }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        //checks superClass and className
        Type t1 = this.superClass.verifyType(compiler);
        if (! t1.isClass()) {
            throw new ContextualError("class extends type",this.getLocation());
        } else if (compiler.getEnvTypes().get(this.className.getName())!=null){
            throw new ContextualError("class already defined",this.getLocation());
        }
        //defines classDefinition and classType
        ClassDefinition superDef = (ClassDefinition)compiler.getEnvTypes().get(this.superClass.getName());
        ClassType t = new ClassType(this.className.getName(),this.getLocation(),superDef);
        ClassDefinition def = t.getDefinition();
        compiler.getEnvTypes().put(this.className.getName(), def);
        this.className.setDefinition(def);
        this.className.setType(t);
        
        //Not for verifying, but for the sake of binding the declaration to the definition
        this.className.getClassDefinition().setDecl(this);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        ClassDefinition def = (ClassDefinition)compiler.getEnvTypes().get(this.className.getName());
        this.field.verifyListField(compiler,def);
        this.methods.verifyListMethod(compiler, def);
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        
        ClassDefinition def = (ClassDefinition)compiler.getEnvTypes().get(this.className.getName());
        this.methods.verifyListBody(compiler,def);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s,prefix,false);
        superClass.prettyPrint(s,prefix,false);
        field.prettyPrint(s,prefix,false);
        methods.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
        superClass.iter(f);
        this.field.iter(f);
        this.methods.iter(f);
    }
    @Override 
    public void buildMethodTabl(DecacCompiler compiler) {
        ((ClassDefinition)compiler.getEnvTypes().get(this.className.getName())).write(compiler);
    }

    @Override
    public void generateMethodBody(DecacCompiler compiler) {
        compiler.currentClass(this.className.getClassDefinition());
        for(AbstractDeclMethod a : super.methods.getList()) {
            a.codeGenBody(compiler,field);
        }
        addInit(compiler);
        
    }
    
    public void fieldsInInit(DecacCompiler compiler) {
        
        if (superClass.getName().getName() != "Object") {
            superClass.getClassDefinition().getDecl().fieldsInInit(compiler);
        }
        
        Iterator<AbstractDeclField> it = field.iterator();
        
        while (it.hasNext()) {
            AbstractDeclField localDecl=it.next();
            localDecl.generateInit(compiler);
        }
    }
    
    public void addInit(DecacCompiler compiler) {
        ClassDefinition def = className.getClassDefinition();
        Label init = new Label("init."+def.getType().getName().getName() );
        compiler.addLabel(init);
        compiler.setSaveRegisterFlag( compiler.createFlag());
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB) , Register.getR(2)));
        fieldsInInit(compiler);
        int [] regUsedList = compiler.getUsedRegister();
        for(int i : regUsedList) {
            if(i!=-1) {
                compiler.addToFlag(compiler.getSaveRegisterFlag(),new PUSH(Register.getR(i)));
                compiler.incOverFlow();
            }
        }
        compiler.addToFlag(compiler.getSaveRegisterFlag(),new PUSH(Register.getR(2)));
        compiler.incOverFlow();
        int j;
        for(j=0;j<regUsedList.length;j++) {
            int i=regUsedList[regUsedList.length-1-j];
            if(i!=-1) {
                compiler.addInstruction(new POP(Register.getR(i)));
                compiler.decOverFlow();
            }
        }
        compiler.addInstruction(new POP(Register.getR(2)));
        compiler.decOverFlow();
        compiler.addInstruction(new RTS());
        compiler.writeFlag(compiler.getSaveRegisterFlag());
    }    

}
