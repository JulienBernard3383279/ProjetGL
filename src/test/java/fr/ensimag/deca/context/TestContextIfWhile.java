/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractMain;
import fr.ensimag.deca.tree.AbstractPrint;
import fr.ensimag.deca.tree.Assign;
import fr.ensimag.deca.tree.BooleanLiteral;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.DeclVar;
import fr.ensimag.deca.tree.Equals;
import fr.ensimag.deca.tree.Greater;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.IfThenElse;
import fr.ensimag.deca.tree.Initialization;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.tree.TreeList;
import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.Lower;
import fr.ensimag.deca.tree.Main;
import fr.ensimag.deca.tree.Plus;
import fr.ensimag.deca.tree.Println;
import fr.ensimag.deca.tree.Program;
import fr.ensimag.deca.tree.StringLiteral;
import fr.ensimag.deca.tree.While;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author pierre
 */
public class TestContextIfWhile {
    
    DecacCompiler compiler; 

    @Test
    public void testContextIfWhile() throws ContextualError{
        compiler = new DecacCompiler(null,null);
        compiler.initSymbolsAndEnvTypes(new SymbolTable());
        SymbolTable symbols = compiler.getSymbols();
        //create idents
        Symbol integer = symbols.create("int");
        Identifier Int = new Identifier(integer);
        IntLiteral zero = new IntLiteral(0);
        IntLiteral one = new IntLiteral(1);
        IntLiteral two = new IntLiteral(2);
        IntLiteral three = new IntLiteral(3);
        IntLiteral five = new IntLiteral(5);
        Symbol i = symbols.create("i");
        Identifier I = new Identifier(i);
        //adds and assigns
        Plus add1 = new Plus(I,two);
        Plus add2 = new Plus(I,one);
        Assign ass1 = new Assign(I,add1);
        Assign ass2 = new Assign(I,add2);
        ListInst l1 = new ListInst();
        l1.add(ass1);
        ListInst l2 = new ListInst();
        l2.add(ass2);
        //conditions
        Greater gt = new Greater(I,three);
        Lower lt = new Lower(I,five);
        //if and while
        IfThenElse ite = new IfThenElse(gt,l1,l2);
        ListInst l = new ListInst();
        l.add(ite);
        While w = new While(lt,l);
        ListInst lf = new ListInst();
        lf.add(w);
        //declVar
        Initialization init = new Initialization(zero);
        DeclVar dec = new DeclVar(Int,I,init);
        ListDeclVar lv = new ListDeclVar();
        lv.add(dec);
        //main
        Main m = new Main(lv,lf);
        ListDeclClass lc = new ListDeclClass();
        Program prog = new Program(lc,m);
        
        //test
        prog.verifyProgram(compiler);
    }
    
    @Test
    public void testCondinf() throws ContextualError{
       compiler = new DecacCompiler(null,null);
        compiler.initSymbolsAndEnvTypes(new SymbolTable());
        SymbolTable symbols = compiler.getSymbols();
        //create idents
        Symbol integer = symbols.create("int");
        Identifier Int = new Identifier(integer);
        IntLiteral zero = new IntLiteral(0);
        IntLiteral one = new IntLiteral(1);
        IntLiteral two = new IntLiteral(2);
        IntLiteral three = new IntLiteral(3);
        IntLiteral five = new IntLiteral(5);
        Symbol i = symbols.create("i");
        Identifier I = new Identifier(i);
        //adds and assigns
        Plus add1 = new Plus(I,two);
        Plus add2 = new Plus(I,one);
        Assign ass1 = new Assign(I,add1);
        Assign ass2 = new Assign(I,add2);
        ListInst l1 = new ListInst();
        l1.add(ass1);
        ListInst l2 = new ListInst();
        l2.add(ass2);
        //conditions
        Greater gt = new Greater(I,three);
        Lower lt = new Lower(I,five);
        //if and while
        IfThenElse ite = new IfThenElse(lt,l1,l2);
        ListInst l = new ListInst();
        l.add(ite);
        While w = new While(gt,l);
        ListInst lf = new ListInst();
        lf.add(w);
        //declVar
        Initialization init = new Initialization(zero);
        DeclVar dec = new DeclVar(Int,I,init);
        ListDeclVar lv = new ListDeclVar();
        lv.add(dec);
        //main
        Main m = new Main(lv,lf);
        ListDeclClass lc = new ListDeclClass();
        Program prog = new Program(lc,m);
        
        //test
        prog.verifyProgram(compiler);
    }
    
     @Test
    public void testCondEquals() throws ContextualError{
        compiler = new DecacCompiler(null,null);
        compiler.initSymbolsAndEnvTypes(new SymbolTable());
        SymbolTable symbols = compiler.getSymbols();
        //create idents
        Symbol integer = symbols.create("int");
        Identifier Int = new Identifier(integer);
        IntLiteral zero = new IntLiteral(0);
        IntLiteral one = new IntLiteral(1);
        IntLiteral two = new IntLiteral(2);
        IntLiteral three = new IntLiteral(3);
        IntLiteral five = new IntLiteral(5);
        Symbol i = symbols.create("i");
        Identifier I = new Identifier(i);
        //adds and assigns
        Plus add1 = new Plus(I,two);
        Plus add2 = new Plus(I,one);
        Assign ass1 = new Assign(I,add1);
        Assign ass2 = new Assign(I,add2);
        ListInst l1 = new ListInst();
        l1.add(ass1);
        ListInst l2 = new ListInst();
        l2.add(ass2);
        //conditions
        
        //if and while
        Equals eq = new Equals(one,two);
        ListInst l = new ListInst();
        l.add(eq);
        While w = new While(eq,l);
        ListInst lf = new ListInst();
        lf.add(w);
        //declVar
        Initialization init = new Initialization(zero);
        DeclVar dec = new DeclVar(Int,I,init);
        ListDeclVar lv = new ListDeclVar();
        lv.add(dec);
        //main
        Main m = new Main(lv,lf);
        ListDeclClass lc = new ListDeclClass();
        Program prog = new Program(lc,m);
        
        //test
        prog.verifyProgram(compiler);
    }
    
    
    
}
