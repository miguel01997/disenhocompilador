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
public class PosicionMatrizProduccion {
    private String noTerminal = "";
    private String terminal = "";
    private String produccion = "";

    public PosicionMatrizProduccion() {
        noTerminal = "";
        terminal = "";
        produccion = "";
    }

    /**
     * GETTER AND SETER ATRIBUTOS
     */
    
    public String getNoTerminal() {
        return noTerminal;
    }

    public void setNoTerminal(String noTerminal) {
        this.noTerminal = noTerminal;
    }

    public String getProduccion() {
        return produccion;
    }

    public void setProduccion(String produccion) {
        this.produccion = produccion;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}
