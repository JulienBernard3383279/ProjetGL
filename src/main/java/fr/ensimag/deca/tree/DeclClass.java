package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl58
 * @date 01/01/2017
 */
public class DeclClass extends AbstractDeclClass {

    @Override
    public void decompile(IndentPrintStream s) {
        
        
        s.print("class");
        s.print(" ");//sans-objet
        this.className.decompile(s);
        s.print("extends");
        s.print(" ");
        this.superClass.decompile(s);
        s.print(" {");
        
        
        
        
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        if (compiler.getEnvTypes().get(this.superClass.getName())==null) {
            throw new ContextualError("class extends undefined class",this.getLocation());
        } else if (! compiler.getEnvTypes().get(this.superClass.getName()).isClass()) {
            throw new ContextualError("class extends type",this.getLocation());
        } else if (compiler.getEnvTypes().get(this.className.getName())!=null){
            throw new ContextualError("class already defined",this.getLocation());
        }
        ClassDefinition superDef = (ClassDefinition)compiler.getEnvTypes().get(this.superClass.getName());
        ClassType t = new ClassType(this.className.getName(),this.getLocation(),superDef);
        ClassDefinition def = new ClassDefinition(t,this.getLocation(),superDef);
        compiler.getEnvTypes().put(this.className.getName(), def);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        ClassDefinition def = (ClassDefinition)compiler.getEnvTypes().get(this.className.getName());
        this.field.verifyListField(compiler,def);
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
