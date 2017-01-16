package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl58
 * @date 01/01/2017
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public boolean getChecks() {
        return checks;
    }

    public boolean getVerif() {
        return verif;
    }

    public boolean getParse() {
        return parse;
    }

    public int getNbRegisters() {
        return nbRegisters;
    }
    
    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private int nbRegisters = 15;
    private boolean waitingForNbRegisters=false;
    private boolean checks=true;
    private boolean parse=false;
    private boolean verif=false;
    
    public void parseArgs(String[] args) throws CLIException {        
        // Le comportement dans le cas d'une utilisation non conforme à la syntaxe est non spécifié.
        // Les noms de fichiers ne peuvent pas être des noms d'options.
        
        for (String str : args) {
            if (waitingForNbRegisters) {
                nbRegisters=Integer.parseInt(str);
                if (nbRegisters<4 || nbRegisters>16) {
                    throw new UnsupportedOperationException("The number of registers must be set between 4 and 16.");
                }
                waitingForNbRegisters=false;
            }
            switch(str) {
                case "-b" :
                    printBanner=true;
                    if (args.length!=1) {
                        throw new UnsupportedOperationException("The -b option is only to be used alone.");
                    }
                    break;
                case "-P" :
                    parallel=true;
                    break;
                case "-n" :
                    checks=false; //Supprime les tests de débordement mémoire (autres tests non faits de base)
                    break;
                case "-v" :
                    verif=true;
                    break;
                case "-p" :
                    parse=true;
                    break;
                case "-d" :
                    debug++;
                    break;
                case "-r" :
                    waitingForNbRegisters=true;
                    break;
                default:
                    sourceFiles.add(new File(str));
                    break;
            }
            if (verif && parse) {
                throw new UnsupportedOperationException("Options -p and -v are incompatible.");
            }
        }
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());
        
        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }        
    }

    protected void displayUsage() {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
