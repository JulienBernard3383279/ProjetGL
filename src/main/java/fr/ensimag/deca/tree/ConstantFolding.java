/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;
import java.util.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.*;
import fr.ensimag.deca.Extension;


/**
 *
 * @author carret
 */

public class ConstantFolding extends Extension{
    
    /*private Type resultat;

    public Type getResultat() {
        return resultat;
    }
    
    
    
    public void calcConst(ListInst listInst){
            Iterator<AbstractInst> i= listInst.iterator();
            AbstractExpr expr;
            Boolean typeVar;
            this.resultat=null;
            while(i.hasNext()){
                AbstractInst inst = i.next();
                if(inst instanceof AbstractBinaryExpr){
                        if(inst instanceof Assign){
                            typeVar=((Assign) inst).getLeftOperand().getType().isInt();
                            if(typeVar){
                                
                            }
                            typeVar=((Assign) inst).getLeftOperand().getType().isBoolean();
                            if(typeVar){
                                
                            }
                            typeVar=((Assign) inst).getLeftOperand().getType().isFloat();
                            if(typeVar){
                                
                            }
                            expr=((Assign) inst).getRightOperand();
                            if(expr instanceof AbstractBinaryExpr){
                                    
                            }
                        }
                }
            }
            
    }
    
    
   public Type boucle(AbstractExpr expr, String operateur){
       AbstractExpr expr1, expr2;
       String ope;
       Type resTemp;
       if(expr instanceof AbstractBinaryExpr){
           expr1=((AbstractBinaryExpr) expr).getLeftOperand();
           expr2= ((AbstractBinaryExpr) expr).getRightOperand();
           if (expr1 instanceof Identifier || expr2 instanceof Identifier){
               return null;
           }
           if(expr2  instanceof AbstractBinaryExpr){
               ope=((AbstractBinaryExpr) expr2).getOperatorName();
               boucle(expr2, ope);
           }
           
       }
       if (expr2 instanceof IntLiteral){
           
       }
       if(operateur=="+"){
           resTemp=expr2.getValue()+resTemp;
       }
       else if(operateur=="-"){
           resTemp=resTemp-expr2.getValue();
       }
       else if(operateur=="*"){
           resTemp=expr2.getValue()*resTemp;
       }
       else if(operateur=="/"){
           resTemp=resTemp/expr2.getValue();
       }
       return resultat;
   }*/
    
}
