package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Plus;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.tree.Deadstore;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.AbstractInitialization;
import java.util.*;
import fr.ensimag.deca.tree.AbstractDeclVar;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.Tree;
import fr.ensimag.deca.tree.AbstractInst;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.DeclVar;
/**
 * Test for the Plus node using mockito, without using advanced features.
 * @see TestPlusAdvanced for more advanced examples.
 * @see TestPlusWithoutMock too see what would need to be written if the test
 * was done without using Mockito.
 *
 * @author Ensimag
 * @date 01/01/2017
 */
public class TestPlusPlain {
    final Type INT = new IntType(null);
    final Type FLOAT = new FloatType(null);

    @Test
    public void testType() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        AbstractExpr left = Mockito.mock(AbstractExpr.class);
        when(left.verifyExpr(compiler, null, null)).thenReturn(INT);
        AbstractExpr right = Mockito.mock(AbstractExpr.class);
        when(right.verifyExpr(compiler, null, null)).thenReturn(INT);
        Plus t = new Plus(left, right);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(left).verifyExpr(compiler, null, null);
        verify(right).verifyExpr(compiler, null, null);
    }
    
    @Test
    public void testDeadStore(){
            ListDeclVar list_var=new ListDeclVar();
            ListInst list_inst=new ListInst();
                    
            
            Deadstore dead=new Deadstore();
            
            SymbolTable table=new SymbolTable();
            Symbol symba=table.create("a");
            Symbol symbb=table.create("b");
            Symbol symbc=table.create("c");
            Symbol symbint=table.create("int");
            
            AbstractIdentifier type=new Identifier(symbint);
            AbstractIdentifier varName1=new Identifier(symba);
            AbstractIdentifier varName2=new Identifier(symbb);
            AbstractIdentifier varName3=new Identifier(symbc);
            AbstractInitialization initialization=null;
             
             
            DeclVar dec_a=new DeclVar(type,varName1,initialization); 
            DeclVar dec_b=new DeclVar(type,varName2,initialization);
            DeclVar dec_c=new DeclVar(type,varName3,initialization);
            
            Identifier ida=new Identifier((symba));
            Identifier idb=new Identifier(symbb);
            Identifier idc=new Identifier(symbc);
            
            list_var.add(dec_a);
            list_var.add(dec_b);
            list_var.add(dec_c);
            list_inst.add(ida);
            list_inst.add(idb);
           
            
            dead.store_dec(list_var);
            dead.store_var_inst(list_inst);
            dead.remove_var(list_var);
            
            Iterator<AbstractDeclVar> i=list_var.iterator();
            while(i.hasNext())
                System.out.println(i.next());
        }
}