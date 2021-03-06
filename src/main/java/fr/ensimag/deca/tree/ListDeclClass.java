package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author gl58
 * @date 01/01/2017
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        Iterator<AbstractDeclClass> it = this.iterator();
        try {
            while (it.hasNext()) {
                it.next().verifyClass(compiler);
            }
            this.verifyListClassMembers(compiler);
        } catch (ContextualError e) {
            throw e;
        }
        // LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        Iterator<AbstractDeclClass> it = this.iterator();
        try {
            while (it.hasNext()) {
                it.next().verifyClassMembers(compiler);
            }
            this.verifyListClassBody(compiler);
        } catch (ContextualError e) {
            throw e;
        }
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        Iterator<AbstractDeclClass> it = this.iterator();
        try {
            while (it.hasNext()) {
                it.next().verifyClassBody(compiler);
            }
        } catch (ContextualError e) {
            throw e;
        }
    }


}
