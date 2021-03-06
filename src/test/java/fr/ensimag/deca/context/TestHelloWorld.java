package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractMain;
import fr.ensimag.deca.tree.AbstractPrint;
import fr.ensimag.deca.tree.BooleanLiteral;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.tree.TreeList;
import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.Main;
import fr.ensimag.deca.tree.Plus;
import fr.ensimag.deca.tree.Println;
import fr.ensimag.deca.tree.Print;
import fr.ensimag.deca.tree.Program;
import fr.ensimag.deca.tree.StringLiteral;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pierre
 */
public class TestHelloWorld {
    
    DecacCompiler compiler;
    
    @Test
    public void testHelloWorld() throws ContextualError {
        compiler = new DecacCompiler(null,null);
        compiler.initSymbolsAndEnvTypes(new SymbolTable());
        AbstractExpr hello = new StringLiteral("Hello, world!");
        ListExpr arg = new ListExpr();
        arg.add(hello);
        AbstractPrint println = new Println(false,arg);
        ListInst insts = new ListInst();
        insts.add(println);
        ListDeclVar vars = new ListDeclVar();
        AbstractMain main = new Main(vars,insts);
        ListDeclClass classes = new ListDeclClass();
        Program prog = new Program(classes,main);
        prog.verifyProgram(compiler);
        
    
    }
   
    
    @Test
    public void testPrintlnx() throws ContextualError {
        compiler = new DecacCompiler(null,null);
        AbstractExpr hello = new StringLiteral("Hello, world!");
        ListExpr arg = new ListExpr();
        arg.add(hello);
        AbstractPrint println = new Println(true,arg);
        ListInst insts = new ListInst();
        insts.add(println);
        ListDeclVar vars = new ListDeclVar();
        AbstractMain main = new Main(vars,insts);
        ListDeclClass classes = new ListDeclClass();
        Program prog = new Program(classes,main);
        prog.verifyProgram(compiler);
        
    
    }
    
     @Test
    public void testPrint() throws ContextualError{
        compiler = new DecacCompiler(null,null);
        AbstractExpr hello = new StringLiteral("Hello, world!");
        ListExpr arg = new ListExpr();
        arg.add(hello);
        AbstractPrint println = new Print(false,arg);
        ListInst insts = new ListInst();
        insts.add(println);
        ListDeclVar vars = new ListDeclVar();
        AbstractMain main = new Main(vars,insts);
        ListDeclClass classes = new ListDeclClass();
        Program prog = new Program(classes,main);
        prog.verifyProgram(compiler); 
    
    }
    
    @Test
    public void testPrintx() throws ContextualError{
        compiler = new DecacCompiler(null,null);
        AbstractExpr hello = new IntLiteral(1+1);
        ListExpr arg = new ListExpr();
        arg.add(hello);
        AbstractPrint println = new Print(true,arg);
        ListInst insts = new ListInst();
        insts.add(println);
        ListDeclVar vars = new ListDeclVar();
        AbstractMain main = new Main(vars,insts);
        ListDeclClass classes = new ListDeclClass();
        Program prog = new Program(classes,main);
        prog.verifyProgram(compiler); 
    
    }
        
    
    
    
}
