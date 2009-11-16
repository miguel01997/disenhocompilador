/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintacticanalizer.ec.components;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Guido Casco
 */
public class AnalisisASPNR {
    private String stack = "";
    private String input = "";
    private String output = "";
    
    
    public AnalisisASPNR() {
    }



    /**
     * GETTER AND SETER ATRIBUTOS
     */
    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    
}
