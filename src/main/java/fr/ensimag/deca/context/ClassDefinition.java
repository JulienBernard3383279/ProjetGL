package fr.ensimag.deca.context;
import fr.ensimag.deca.DecacCompiler;
import java.util.*;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.ListDeclField;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
/**
 * Definition of a class.
 *
 * @author gl58
 * @date 01/01/2017
 */
public class ClassDefinition extends TypeDefinition {
    
    private DeclClass decl;
    
    public void setDecl(DeclClass decl) {
        this.decl=decl;
    }
    
    public DeclClass getDecl() {
        return decl;
    }
    
    
    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass; 

    public EnvironmentExp getMembers() {
        return members;
    }

    Map<Symbol,FieldDefinition> symbolsToFieldDefinition = new HashMap<>();
    
    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
               
        members = new EnvironmentExp(parent);

        this.superClass = superClass;
                
    }
            
    private boolean writen=false;
    private ClassMethodSet MethodsSet;
    public ClassMethodSet write(DecacCompiler compiler) {
        if(!writen) {
            writen = true;
            
            //addInit(compiler);
            
            if(superClass!=null) 
                MethodsSet= new ClassMethodSet(this.superClass.write(compiler));
            else 
                MethodsSet = new ClassMethodSet(new NullAddr());
            for(ExpDefinition a : members.getDico().values()) {
                if(a.isMethod()) {
                    MethodDefinition method = (MethodDefinition)a;
                    MethodsSet.addMethod(new MethodInformation(method.getLabel(),
                            method.getIndex()));
                }
            }
            compiler.addComment("Construction de la table des méthodes de "
                    +this.getType().getName().getName());
            if(!this.getType().getName().getName().equals("Object"))
                compiler.addInstruction(new LEA(MethodsSet.getAddr(),Register.R0));
            else 
                compiler.addInstruction(new LOAD(MethodsSet.getAddr(),Register.R0));
            MethodsSet.setAddr(new RegisterOffset(compiler.getCurrentMethodNumber()+1,Register.GB));
            compiler.addInstruction(new STORE(Register.R0,MethodsSet.getAddr()));
            int i=1;
            for(MethodInformation a : MethodsSet.getMethods()) {
                i++;
                compiler.addInstruction(new LOAD(new LabelOperand(a.getLabel()),Register.R0));
                compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(compiler.getCurrentMethodNumber()+i,Register.GB)));
            }
            compiler.addMethod(i);
        }
        return MethodsSet;
    }
    
    public boolean isCastCompatible(ClassDefinition otherClass) {
        ClassDefinition curr = this;
        while (curr != null) {
            if (otherClass.getType().sameType(curr.getType())) {
                return true;
            }
            curr = curr.getSuperClass();
        }
        curr = otherClass;
        while (curr != null) {
            if (curr.getType().sameType(this.getType())) {
                return true;
            }
            curr = curr.getSuperClass();
        }
        return false;
    }
    
    /*
    public void addInit(DecacCompiler compiler) {
        
        init.A :
        LOAD #0, R0
        LOAD -2 (LB), R1
        STORE R0, 1 (R1)
        RTS
        
        Label init = new Label("init."+this.getType().getName().getName() );
        compiler.addLabel(init);
        compiler.addInstruction(new LOAD(new RegisterOffset(-(1+numberOfFields), Register.LB) , Register.R1));
        
        Set<Symbol> keySet=this.members.getDico().keySet();
        for (Symbol s : keySet ) {
            Definition resultDefinition = members.getDico().get(s);
            if (resultDefinition.isField()) {
                FieldDefinition field = (FieldDefinition) resultDefinition;
                Type itsType = field.getType();
                if ( itsType.isInt() ) {
                    compiler.addInstruction(new LOAD(0,Register.R0));
                }
                else if (itsType.isFloat() ) {
                    compiler.addInstruction(new LOAD(0,Register.R0));
                    compiler.addInstruction(new FLOAT(Register.R0,Register.R0));
                }
                else if (itsType.isBoolean() ) {
                    compiler.addInstruction(new LOAD(0,Register.R0));
                }
                else {
                    compiler.addInstruction(new LOAD(null,Register.R0));
                }
                compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(field.getIndex(),Register.R1)));
            }
            
        }
        compiler.addInstruction(new RTS());
        
        /* LOAD -2 (LB), R1 devrait être au début
        -2 = - (nb de champ, incluant ceux des superclasses + 1)
        
        puis boucle sur members.dico puis utiliser isField
        
        LOAD #0, R0
        STORE R0, 1 (R1)
        
        =>
        
        LOAD (la valeur qui va bien en fonction du type, R0
        STORE R0, index (R1)
        
        à la fin, RTS
        
        int => #0
        boolean => #0
        float => #0 puis FLOAT
        String : N'EXISTE PAS
        Autres => #null
        
        
    }
    */
}
