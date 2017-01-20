package fr.ensimag.ima.pseudocode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Abstract representation of an IMA program, i.e. set of Lines.
 *
 * @author Ensimag
 * @date 01/01/2017
 */
public class IMAProgram {
    private final LinkedList<AbstractLine> lines = new LinkedList<AbstractLine>();
    private Map<Integer,LinkedList<AbstractLine>> flagsContent  = new HashMap<Integer,LinkedList<AbstractLine>>();
    public void add(AbstractLine line) {
        lines.add(line);
    }

    public void addComment(String s) {
        lines.add(new Line(s,true));
    }

    public void addLabel(Label l) {
        lines.add(new Line(l));
    }

    public void addInstruction(Instruction i) {
        lines.add(new Line(i));
    }

    public void addInstruction(Instruction i, String s) {
        lines.add(new Line(null, i, s));
    }
    public void addASMCode(String s) {
        lines.add(new Line(s,false));
    }

    /**
     * Append the content of program p to the current program. The new program
     * and p may or may not share content with this program, so p should not be
     * used anymore after calling this function.
     */
    public void append(IMAProgram p) {
        lines.addAll(p.lines);
    }
    
    /**
     * Add a line at the front of the program.
     */
    public void addFirst(Line l) {
        lines.addFirst(l);
    }

    /**
     * Display the program in a textual form readable by IMA to stream s.
     */
    public void display(PrintStream s) {
        for (AbstractLine l: lines) {
            l.display(s);
        }
    }

    /**
     * Return the program in a textual form readable by IMA as a String.
     */
    public String display() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream s = new PrintStream(out);
        display(s);
        return out.toString();
    }

    public void addFirst(Instruction i) {
        addFirst(new Line(i));
    }
    
    public void addFirst(Instruction i, String comment) {
        addFirst(new Line(null, i, comment));
    }
    public int addFlag() {
        flagsContent.put(lines.size(),new LinkedList<AbstractLine>());
        return lines.size();
    }
    public void addToFlag(int flag, Instruction i) {
        flagsContent.get(flag).add(new Line(i));
    }
    public void writeFlagToProgram(int flag) {
        lines.addAll(flag, flagsContent.get(flag));
    }
}
