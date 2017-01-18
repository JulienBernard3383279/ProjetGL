package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl58
 * @date 01/01/2017
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractExpr> it = this.iterator();
        while (it.hasNext()) {
            it.next().decompile(s);
            if (it.hasNext()) {
                s.print(", ");
            }
        }
    }
}
