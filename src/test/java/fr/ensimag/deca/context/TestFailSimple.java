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
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.Initialization;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.tree.TreeList;
import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.Main;
import fr.ensimag.deca.tree.Plus;
import fr.ensimag.deca.tree.Println;
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
public class TestFailSimple {
    
    DecacCompiler compiler;
    
    @Test
    public void testFailSimple() throws ContextualError{
        compiler = new DecacCompiler(null,null);
        compiler.initSymbolsAndEnvTypes(new SymbolTable());
        SymbolTable symbols = this.compiler.getSymbols(); 
        //create identifiers
        Symbol integer = symbols.create("int");
        Identifier Int = new Identifier(integer);
        Symbol a = symbols.create("a");
        Identifier A = new Identifier(a);
        Symbol b = symbols.create("b");
        Identifier B = new Identifier(b);
        IntLiteral one = new IntLiteral(1);
        //declare int a = 1;
        Initialization init = new Initialization(one);
        DeclVar dec = new DeclVar(Int,A,init);
        ListDeclVar l1 = new ListDeclVar();
        l1.add(dec);
        //assign a = b + 1;
        Plus add = new Plus(B,one);
        Assign assign = new Assign(A,add);
        ListInst l2 = new ListInst();
        l2.add(assign);
        //main
        Main main = new Main(l1, l2);
        ListDeclClass l= new ListDeclClass();
        Program prog = new Program(l, main);
        try {
            prog.verifyProgram(compiler);
            assertTrue(1 == 2);
        } catch (ContextualError e) {
            //test OK
        }
    }
}
