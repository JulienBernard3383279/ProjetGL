/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;
import java.util.*;
import fr.ensimag.deca.Extension;


/**
 *
 * @author carret
 */

public class ConstantFolding extends Extension{
    
    private AbstractExpr resultat;

    public AbstractExpr getResultat() {
        return resultat;
    }
    
    
    
    public void calcConst(ListInst listInst){
            Iterator<AbstractInst> i= listInst.iterator();
            AbstractExpr expr;
            this.resultat=null;
            while(i.hasNext()){
                AbstractInst inst = i.next();
                if(inst instanceof AbstractBinaryExpr){
                        if(inst instanceof Assign){
                            expr=((Assign) inst).getRightOperand();
                            if(expr instanceof AbstractBinaryExpr){
                                    
                            }
                        }
                }
            }
            
    }
    
    
   public AbstractExpr boucle(AbstractExpr expr){
       AbstractExpr expr1, expr2;
       if(expr instanceof AbstractBinaryExpr){
           expr1=((AbstractBinaryExpr) expr).getLeftOperand();
           expr2= ((AbstractBinaryExpr) expr).getRightOperand();
           if(expr2  instanceof AbstractBinaryExpr){
               resultat=boucle(expr2);
           }
           
       }
       return resultat;
   }
    
}
