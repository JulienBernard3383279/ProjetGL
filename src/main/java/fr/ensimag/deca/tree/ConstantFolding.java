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
    
    private Type resultat;

    public Type getResultat() {
        return resultat;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    public void optTreeConst(ListInst listInst){
            Iterator<AbstractInst> i= listInst.iterator();
            AbstractExpr exprOp, exprCons;
            Boolean typeVar;
            String ope;
            int taille;
            int intRes=0;
            ArrayList<String> tabOperation = new ArrayList<>();
            ArrayList<AbstractExpr> tabCons = new ArrayList<>();
            while(i.hasNext()){
                AbstractInst inst = i.next();
                if(inst instanceof AbstractBinaryExpr){
                        if(inst instanceof Assign){
                            typeVar=((Assign) inst).getLeftOperand().getType().isInt();
                            if(typeVar){
                               
                                exprOp=((Assign) inst).getRightOperand();
                               
                                if(exprOp instanceof AbstractBinaryExpr){
                                    ope=((AbstractBinaryExpr) exprOp).getOperatorName();
                                    tabOperation.add(ope);
                                    exprCons=((AbstractBinaryExpr) exprOp).getRightOperand();
                                    if(exprCons instanceof Identifier){
                                    }
                                    tabCons.add(exprCons);
                                }
                                else{
                                    System.out.println("erreur");
                                }
                                
                                while(((AbstractBinaryExpr) exprOp).getLeftOperand() instanceof AbstractOpArith){
                                    ope=((AbstractBinaryExpr) exprOp).getOperatorName();
                                    tabOperation.add(ope);
                                    exprCons=((AbstractBinaryExpr) exprOp).getRightOperand();
                                    tabCons.add(exprCons);
                                }
                                
                                exprCons=((AbstractBinaryExpr) exprOp).getLeftOperand();
                                tabCons.add(exprCons);
                                exprCons=((AbstractBinaryExpr) exprOp).getRightOperand();
                                tabCons.add(exprCons);
                                
                                taille=tabOperation.size();
                                ope=tabOperation.remove(taille);
                               
                                if(ope=="+"){
                                taille=tabCons.size();
                                intRes=((IntLiteral)(tabCons.remove(taille))).getValue()+((IntLiteral)(tabCons.remove(taille-1))).getValue();
                                }
                                else if(ope=="-"){
                                taille=tabCons.size();
                                intRes=-((IntLiteral)(tabCons.remove(taille))).getValue()+((IntLiteral)(tabCons.remove(taille-1))).getValue();
                                }
                                else if(ope=="*"){
                                taille=tabCons.size();
                                intRes=((IntLiteral)(tabCons.remove(taille))).getValue()*((IntLiteral)(tabCons.remove(taille-1))).getValue();
                                }
                                else if(ope=="/"){
                                taille=tabCons.size();
                                intRes=((IntLiteral)(tabCons.remove(taille-1))).getValue()*((IntLiteral)(tabCons.remove(taille))).getValue();
                                }
                                
                                while(!tabOperation.isEmpty()){
                                    taille=tabOperation.size();
                                    ope=tabOperation.remove(taille);
                                    if(ope=="+"){
                                        taille=tabCons.size();
                                        intRes=intRes+((IntLiteral)(tabCons.remove(taille))).getValue();
                                    }
                                    else if(ope=="-"){
                                        taille=tabCons.size();
                                        intRes=-intRes+((IntLiteral)(tabCons.remove(taille))).getValue();
                                    }
                                    else if(ope=="*"){
                                        taille=tabCons.size();
                                        intRes=intRes*((IntLiteral)(tabCons.remove(taille))).getValue();
                                    }
                                    else if(ope=="/"){
                                        taille=tabCons.size();
                                        intRes=((IntLiteral)(tabCons.remove(taille))).getValue()/intRes;
                                    }
                                }
                            }
                            
                            
                            
                          
                            typeVar=((Assign) inst).getLeftOperand().getType().isFloat();
                            if(typeVar){
                                exprOp=((Assign) inst).getRightOperand();
                                if(exprOp instanceof AbstractBinaryExpr){
                                    ope=((AbstractBinaryExpr) exprOp).getOperatorName();
                                    intCalc(exprOp, ope);
                                }
                            }
                            exprOp=((Assign) inst).getRightOperand();
                            if(exprOp instanceof AbstractBinaryExpr){
                                    
                            }
                        }
                }
            }
            
    }
    
   
    public int intCalc(AbstractExpr expr, String operateur){
        AbstractExpr expr1, expr2;
        String ope;
        int res=0;
        if(expr instanceof AbstractBinaryExpr){
            expr1=((AbstractBinaryExpr) expr).getLeftOperand();
            expr2= ((AbstractBinaryExpr) expr).getRightOperand();
            if (expr1 instanceof Identifier || expr2 instanceof Identifier){
               return 0;
           }

            else if(expr2 instanceof IntLiteral){
                if(operateur=="+"){
                    res=res+((IntLiteral)expr2).getValue();
                }
                else if(operateur=="-"){
                    res=res-((IntLiteral)expr2).getValue();
                }
                else if(operateur=="*"){
                    res=res*((IntLiteral)expr2).getValue();
                }
                else if(operateur=="/"){
                    res=res/((IntLiteral)expr2).getValue();
                }
                
            }
            else if(expr2 instanceof AbstractBinaryExpr) {
                ope=((AbstractBinaryExpr) expr2).getOperatorName();
                res=intCalc(expr2, ope);
            }
        }
        return res;
    }
    
    
    public float floatCalc(AbstractExpr expr, String operateur){
        AbstractExpr expr1, expr2;
        String ope;
        float res=0;
        if(expr instanceof AbstractBinaryExpr){
            expr1=((AbstractBinaryExpr) expr).getLeftOperand();
            expr2= ((AbstractBinaryExpr) expr).getRightOperand();
            if (expr1 instanceof Identifier || expr2 instanceof Identifier){
               return 0;
           }
            else if(expr2 instanceof FloatLiteral){
                if(operateur=="+"){
                    res=res+((FloatLiteral)expr2).getValue();
                }
                else if(operateur=="-"){
                    res=res-((FloatLiteral)expr2).getValue();
                }
                else if(operateur=="*"){
                    res=res*((FloatLiteral)expr2).getValue();
                }
                else if(operateur=="/"){
                    res=res/((FloatLiteral)expr2).getValue();
                }
                
            }
            else if(expr2 instanceof AbstractBinaryExpr) {
                ope=((AbstractBinaryExpr) expr2).getOperatorName();
                res=intCalc(expr2, ope);
            }
        }
        return res;
    }   
}

