/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;
import java.util.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.CallMethod;

/**
 *cette classe sert pour l'extension deadstore
 * @author marchaem
 * idée : faire une passe pour stocker la liste des variables 
 * déclarée, ainsi que celles dans les instructions. les variables déclarées mais non utilisées sont 
 * supprimées
 */
public class Deadstore {
    private ArrayList arr1;
    private ArrayList arr2;
        

    public Deadstore() {
        this.arr1 = new ArrayList<Identifier>();
        this.arr2 = new ArrayList<Identifier>();
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
     * au début du proramme
     * 
     */

    public void store_dec(ListDeclVar list_var){
        Iterator<AbstractDeclVar> i=list_var.iterator();   
        while(i.hasNext()){   
                AbstractDeclVar var= i.next();
                if(!this.arr1.contains(var))  //on copie un seul
                    this.arr1.add(var);
        }
   
    }
        
    /**
     * on stocke la liste des instructions 
     * @param list_inst 
     */
    public void store_var_inst(ListInst list_inst){
        Iterator<AbstractInst> i=list_inst.iterator();       
        while(i.hasNext()){
            AbstractInst var=i.next();
            if(var instanceof AbstractBinaryExpr){
                AbstractBinaryExpr varBin=(AbstractBinaryExpr) var;
                this.arr2.add(varBin.getLeftOperand());   //ajoute des abstractExpr
                this.arr2.add(varBin.getRightOperand());  //idem             
            }
            else if(var instanceof AbstractUnaryExpr){
                AbstractUnaryExpr varUn=(AbstractUnaryExpr) var;
                this.arr2.add(varUn.getOperand());    //ajoute un abstractExpr
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
                while(it.hasNext()){ //idem que pour abstractprint
                    AbstractExpr expr=it.next();
                    this.arr2.add(expr);
                    
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
        Iterator<AbstractDeclVar> i=this.arr1.iterator();
        while(i.hasNext()){
            AbstractDeclVar dec=i.next();
            if(! this.arr2.contains(dec))
                j=list_var.getList().indexOf(dec);
                list_var.getModifiableList().remove(j);
        }
    }
        
    
    
    
    
}
