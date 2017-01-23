/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;
import java.util.*;
import fr.ensimag.deca.DecacCompiler;
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
                    if(!this.arr1.contains(abstVar))  //on copie le nom des variables initialisées                       
                       this.arr1.add(dec.getVarName());
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
                    this.arr2.add(id.getName().getName());
                }
                if(varBin.getLeftOperand() instanceof Identifier){
                    Identifier id=(Identifier) varBin.getRightOperand();
                    this.arr2.add(id.getName().getName());
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
                    this.arr2.add(expr);
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
     * mais pas dans la liste des instructions
     * @param list_var 
     */
    
    public void remove_var(ListDeclVar list_var){
        int j=0;
        Iterator<String> i=this.arr1.iterator();
        
        while(i.hasNext()){
            String name=i.next();
            if(this.arr2.contains(i)){
                j=this.arr2.indexOf(name);
                  list_var.getModifiableList().remove(j); //on enlève la variable inutile                           
                }                 
            }        
        }
    
        
    /**
     * execute les 3 méthodes perméttant d'enlever les variables inutiles de l'arbre
     * @param list_var
     * @param list_inst 
     */
    public void execute(ListDeclVar list_var,ListInst list_inst) {
        store_dec(list_var);
        store_var_inst(list_inst);
        remove_var(list_var);
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

   
    
    
}
