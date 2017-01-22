package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

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
        Type t1 = this.superClass.verifyType(compiler);
        if (! t1.isClass()) {
            throw new ContextualError("class extends type",this.getLocation());
        } else if (compiler.getEnvTypes().get(this.className.getName())!=null){
            throw new ContextualError("class already defined",this.getLocation());
        }
        ClassDefinition superDef = (ClassDefinition)compiler.getEnvTypes().get(this.superClass.getName());
        ClassType t = new ClassType(this.className.getName(),this.getLocation(),superDef);
        ClassDefinition def = t.getDefinition();
        compiler.getEnvTypes().put(this.className.getName(), def);
        this.className.setDefinition(def);
        this.className.setType(t);
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
    }
    
}
