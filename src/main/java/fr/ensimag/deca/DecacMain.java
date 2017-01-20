package fr.ensimag.deca;

import fr.ensimag.deca.tree.AbstractProgram;
import java.io.File;
import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl58
 * @date 01/01/2017
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        
        
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        
        
        if (options.getPrintBanner()) {
            System.out.println("Ceci est une bannière !");
            System.exit(0);
        }
        
        
        if(options.getOptim()){
            for (File source : options.getSourceFiles()) {
            DecacCompiler deczc=new DecacCompiler(options, source);
        }
        
        if (options.getSourceFiles().isEmpty()) {
            throw new UnsupportedOperationException("decac without argument not yet implemented");
        }
        
        /*
        if (options.getParse()) {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                AbstractProgram prog = compiler.doLexingAndParsing(source.getAbsolutePath(), System.err);
            }
        }
        */
        
        if (options.getParallel()) {
            for (File source : options.getSourceFiles()) {
                CompilerThread loopThread = new CompilerThread(options,source);
                loopThread.start();
            }
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
