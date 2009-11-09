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
public class Primero {

    private String noTerminal = "";
    private List conjuntoPrimero = null;

    public Primero() {
        noTerminal = "";
        conjuntoPrimero = new ArrayList();
    }

    public void agregarConjuntoPrimero(String cadena) {
        conjuntoPrimero.add(cadena);
    }

    /**
     * GETTER AND SETER ATRIBUTOS
     */

    public void setConjuntoPrimero(List conjuntoPrimero) {
        this.conjuntoPrimero = conjuntoPrimero;
    }

    public void setNoTerminal(String noTerminal) {
        this.noTerminal = noTerminal;
    }

    public List getConjuntoPrimero() {
        return conjuntoPrimero;
    }

    public String getNoTerminal() {
        return noTerminal;
    }
}
