/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;
import java.util.*;
import fr.ensimag.deca.Extension;
/**
 *cette classe sert pour l'extension deadstore
 * @author marchaem
 * idée : faire une passe pour stocker la liste des variables 
 * déclarée, ainsi que celles dans les instructions. les variables déclarées mais non utilisées sont 
 * supprimées
 */
public class DeadStore extends Extension{
    private ArrayList arr1;
    private ArrayList arr2;

         

    public DeadStore() {
        this.arr1 = new ArrayList<>();
        this.arr2 = new ArrayList<>();
    }

    public ArrayList getArr1() {
        return arr1;
    }

    public ArrayList getArr2() {
        return arr2;
    }

    public void setArr1(ArrayList arr1) {
        this.arr1 = arr1;
    }

    public void setArr2(ArrayList arr2) {
        this.arr2 = arr2;
    }

      
    /**
     * on va stocker dans une liste la liste des variables déclarées 
     * au début du programme
     * @param list_var
     */

    public void store_dec(ListDeclVar list_var){
        Iterator<AbstractDeclVar> i=list_var.iterator();        
        while(i.hasNext()){   
                AbstractDeclVar abstVar= i.next();
                if(abstVar instanceof DeclVar){
                    DeclVar dec=(DeclVar) abstVar;
                    if(!this.arr1.contains(dec))
                        System.out.println("dans store_dec\n");
                        this.arr1.add(dec.getVarName());//on copie le nom des variables initialisées en un seul exemplaire  
                }    
        }  
    }
        
    /**
     * on stocke la liste des arguments des instructions 
     * @param list_inst 
     */
    public void store_var_inst(ListInst list_inst){
        Iterator<AbstractInst> i=list_inst.iterator();       
        while(i.hasNext()){
            AbstractInst var=i.next();
            if(var instanceof AbstractBinaryExpr){
                AbstractBinaryExpr varBin=(AbstractBinaryExpr) var;
                if(varBin.getLeftOperand() instanceof Identifier){
                    Identifier id=(Identifier) varBin.getLeftOperand();
                    System.out.println("dans store_var_inst\n");
                    this.arr2.add(id.getName().getName());
                }
                else if(var instanceof AbstractExpr){
                    get_args(varBin.getLeftOperand());
                }
                    
                
                if(varBin.getRightOperand() instanceof Identifier){
                    Identifier id=(Identifier) varBin.getRightOperand();
                    this.arr2.add(id.getName().getName());
                }
                else if(var instanceof AbstractExpr){
                    get_args(varBin.getRightOperand());
                }
                             
            }
            else if(var instanceof AbstractUnaryExpr){
                AbstractUnaryExpr varUn=(AbstractUnaryExpr) var;
                    if(varUn.getOperand() instanceof Identifier){
                        Identifier id=(Identifier) varUn.getOperand();
                        this.arr2.add(id.getName().getName());
                    }                               
            }
            
            else if(var instanceof AbstractPrint){
                AbstractPrint varPrint=(AbstractPrint) var;
                Iterator<AbstractExpr> k=varPrint.getArguments().getList().iterator();
                while(i.hasNext()){   //on ajoute tous arguments du print invoqué dans arr2 du deadstore
                    AbstractExpr expr=k.next();
                    if(expr instanceof Identifier){
                        Identifier id=(Identifier) expr;
                        this.arr2.add(id.getName().getName());
                    }
                    else{
                        get_args(expr);
                    }
                    
                }   
            }
            
            else if(var instanceof CallMethod){
                CallMethod varMethod=(CallMethod) var;
                Iterator<AbstractExpr> it=varMethod.getArgs().getList().iterator();
                while(it.hasNext()){ //idem que pour abstractprint, avec les arguments de la méthode appelée
                    AbstractExpr expr=it.next();
                    if(expr instanceof Identifier){
                        Identifier id=(Identifier) expr;
                        this.arr2.add(id.getName().getName());
                    }                    
                }
            }
            else if(var instanceof Return){
                Return varReturn=(Return) var;
                AbstractExpr expr=varReturn.getExpr();
                if(expr instanceof Identifier){
                    Identifier id=(Identifier) expr;
                    this.arr2.add(id.getName().getName());
                }                                        
            }
            
            
        }
    }
    
    /**
     * retire les variables qui sont dans la liste des variables déclarées
     * mais pas dans la liste des instructions de façon récursive
     * @param expr
     */
    
    public void get_args(AbstractExpr expr){
        if(expr instanceof AbstractBinaryExpr){
            AbstractBinaryExpr binExpr=(AbstractBinaryExpr) expr;
            if(binExpr.getLeftOperand() instanceof Identifier){
                Identifier id=(Identifier) binExpr.getLeftOperand();
                this.arr2.add(id.getName().getName());
            }
            else{
                get_args(binExpr.getLeftOperand());
            }
            if(binExpr.getRightOperand() instanceof Identifier){
                Identifier id=(Identifier) binExpr.getRightOperand();
                this.arr2.add(id.getName());
            }
            else{
                get_args(binExpr.getRightOperand());
            }
        }
        else if(expr instanceof CallMethod){
             CallMethod varMethod=(CallMethod) expr;
                Iterator<AbstractExpr> it=varMethod.getArgs().getList().iterator();
                while(it.hasNext()){ //idem que pour abstractprint, avec les arguments de la méthode appelée
                    AbstractExpr abstExpr=it.next();
                    if(expr instanceof Identifier){
                        Identifier id=(Identifier) abstExpr;
                        this.arr2.add(id.getName().getName());
                    }           
                }
        }
        
       
                
        
    }
    public void remove_var(ListDeclVar list_var){
       int index=0;
       ListDeclVar list=new ListDeclVar();
       Iterator<AbstractDeclVar> i=list_var.iterator();
       while(i.hasNext()){
           AbstractDeclVar abstVar=i.next();
           if(abstVar instanceof DeclVar){
               DeclVar dec;
               dec = (DeclVar) abstVar;
               if(! this.arr2.contains(dec.getVarName().getName().getName())){
                   //index=list_var.getModifiableList().indexOf(dec);
                   System.out.println("la variable "+dec.getVarName().getName().getName()+" est inutilisée dans votre programme");
                   list.getModifiableList().add(dec);
               }
           }
       }
       Iterator<AbstractDeclVar> j=list.iterator();
       while(j.hasNext()){
         AbstractDeclVar abstVar2=j.next();
           if(abstVar2 instanceof DeclVar){
               DeclVar dec2;
               dec2 = (DeclVar) abstVar2;
               if(list_var.getModifiableList().contains(abstVar2)){
                   System.out.println("bon");
                   list_var.getModifiableList().remove(dec2);
               }
            }
        }
    }   
        
    /**
     * execute les 3 méthodes perméttant d'enlever les variables inutiles de l'arbre
     * @param list_var
     * @param list_inst 
     */
    public void execute(ListDeclVar list_var,ListInst list_inst) {
        System.out.println("dans execute");
        store_dec(list_var);
        store_var_inst(list_inst);
        remove_var(list_var);
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

   
    
    
}
