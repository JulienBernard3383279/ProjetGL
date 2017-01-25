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
import fr.ensimag.deca.tree.*;

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
    
    public void execute(ListInst listInst){
        optTreeConst(listInst);
    }
    
        
    public void optTreeConst(ListInst listInst){
            Iterator<AbstractInst> i= listInst.iterator();
            AbstractExpr exprOp, exprCons;
            Boolean typeVar;
            Boolean ident;
            String ope;
            int taille;
            int intRes=0;
            float floatRes=0;
            ArrayList<String> tabOperation = new ArrayList<>();
            ArrayList<AbstractExpr> tabCons = new ArrayList<>();
            
            while(i.hasNext()){
                ident=false;
                AbstractInst inst = i.next();
                if(inst instanceof AbstractBinaryExpr){
                    AbstractBinaryExpr abstInst=(AbstractBinaryExpr) inst;
                    if(inst instanceof Assign){
                        exprOp=((Assign) inst).getRightOperand();
                           
                        while(exprOp instanceof AbstractBinaryExpr){
                            exprCons=((AbstractBinaryExpr) exprOp).getRightOperand();
                            exprOp=((AbstractBinaryExpr) exprOp).getLeftOperand();
                            if(exprCons instanceof AbstractLValue || exprOp instanceof AbstractLValue
                                    || exprCons instanceof AbstractReadExpr || exprOp instanceof AbstractReadExpr
                                    || exprCons instanceof CallMethod || exprOp instanceof CallMethod
                                    ||exprCons instanceof This || exprOp instanceof This
                                    || exprCons instanceof DotMethod || exprOp instanceof DotMethod
                                    || exprCons instanceof Modulo){
                                ident=true;
                            }                                
                        }
                            
                        typeVar=((Assign) inst).getLeftOperand().getType().isInt();
                        if(typeVar && ident==false){
                             
                            exprOp=((Assign) inst).getRightOperand();
                            
                            if(exprOp instanceof AbstractBinaryExpr){
                                ope=((AbstractBinaryExpr) exprOp).getOperatorName();
                                tabOperation.add(ope);
                                exprCons=((AbstractBinaryExpr) exprOp).getRightOperand();
                                tabCons.add(exprCons);
                                
                                                                                                    
                                while(((AbstractBinaryExpr) exprOp).getLeftOperand() instanceof AbstractOpArith){
                                    exprOp=((AbstractBinaryExpr) exprOp).getLeftOperand();
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
                             
                                if("+".equals(ope)){
                                    taille=tabCons.size();
                                    intRes=((IntLiteral)(tabCons.remove(taille))).getValue()+((IntLiteral)(tabCons.remove(taille-1))).getValue();
                                }
                                else if("-".equals(ope)){
                                    taille=tabCons.size();
                                    intRes=-((IntLiteral)(tabCons.remove(taille))).getValue()+((IntLiteral)(tabCons.remove(taille-1))).getValue();
                                }
                                else if("*".equals(ope)){
                                    taille=tabCons.size();
                                    intRes=((IntLiteral)(tabCons.remove(taille))).getValue()*((IntLiteral)(tabCons.remove(taille-1))).getValue();
                                }
                                else if("/".equals(ope)){
                                    taille=tabCons.size();
                                    intRes=((IntLiteral)(tabCons.remove(taille-1))).getValue()*((IntLiteral)(tabCons.remove(taille))).getValue();
                                }
                                                                    
                                while(!tabOperation.isEmpty()){
                                    taille=tabOperation.size();
                                    ope=tabOperation.remove(taille);
                                    if("+".equals(ope)){
                                        taille=tabCons.size();
                                        intRes=intRes+((IntLiteral)(tabCons.remove(taille))).getValue();
                                    }
                                    else if("-".equals(ope)){
                                        taille=tabCons.size();
                                        intRes=-intRes+((IntLiteral)(tabCons.remove(taille))).getValue();
                                    }
                                    else if("*".equals(ope)){
                                        taille=tabCons.size();
                                        intRes=intRes*((IntLiteral)(tabCons.remove(taille))).getValue();
                                    }
                                    else if("/".equals(ope)){
                                        taille=tabCons.size();
                                        intRes=((IntLiteral)(tabCons.remove(taille))).getValue()/intRes;
                                    }    
                                }
                                
                            abstInst.setRightOperand(new IntLiteral(intRes));
                            }
                                            
                        }
                            
                                                                              
                        typeVar=((Assign) inst).getLeftOperand().getType().isFloat();
                        if(typeVar && ident==false){
                             
                            exprOp=((Assign) inst).getRightOperand();
                            
                            if(exprOp instanceof AbstractBinaryExpr){
                                    
                                ope=((AbstractBinaryExpr) exprOp).getOperatorName();
                                tabOperation.add(ope);
                                exprCons=((AbstractBinaryExpr) exprOp).getRightOperand();
                                tabCons.add(exprCons);
                                    
                                while(((AbstractBinaryExpr) exprOp).getLeftOperand() instanceof AbstractOpArith){
                                    exprOp=((AbstractBinaryExpr)exprOp).getLeftOperand();
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
                               
                                if("+".equals(ope)){
                                    taille=tabCons.size();
                                    floatRes=((FloatLiteral)(tabCons.remove(taille))).getValue()+((FloatLiteral)(tabCons.remove(taille-1))).getValue();
                                }
                                else if("-".equals(ope)){
                                    taille=tabCons.size();
                                    floatRes=-((FloatLiteral)(tabCons.remove(taille))).getValue()+((FloatLiteral)(tabCons.remove(taille-1))).getValue();
                                }
                                else if("*".equals(ope)){
                                    taille=tabCons.size();
                                    floatRes=((FloatLiteral)(tabCons.remove(taille))).getValue()*((FloatLiteral)(tabCons.remove(taille-1))).getValue();
                                }
                                else if("/".equals(ope)){
                                    taille=tabCons.size();
                                    floatRes=((FloatLiteral)(tabCons.remove(taille-1))).getValue()*((FloatLiteral)(tabCons.remove(taille))).getValue();
                                }
                                            
                                while(!tabOperation.isEmpty()){
                                    taille=tabOperation.size();
                                    ope=tabOperation.remove(taille);
                                    if("+".equals(ope)){
                                        taille=tabCons.size();
                                        floatRes=floatRes+((FloatLiteral)(tabCons.remove(taille))).getValue();
                                    }
                                    else if("-".equals(ope)){
                                        taille=tabCons.size();
                                        floatRes=-floatRes+((FloatLiteral)(tabCons.remove(taille))).getValue();
                                    }
                                    else if("*".equals(ope)){
                                        taille=tabCons.size();
                                        floatRes=floatRes*((FloatLiteral)(tabCons.remove(taille))).getValue();
                                    }
                                    else if("/".equals(ope)){
                                        taille=tabCons.size();
                                        floatRes=((FloatLiteral)(tabCons.remove(taille))).getValue()/floatRes;
                                    }
                                }
                                
                                abstInst.setRightOperand(new FloatLiteral(floatRes));
                            }           
                        }        
                    }    
                }
            }           
    }
}  
    

