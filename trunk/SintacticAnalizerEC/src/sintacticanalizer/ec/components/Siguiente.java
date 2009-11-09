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
public class Siguiente {
    private String noTerminal = "";
    private List conjuntoSiguiente = null;

    public Siguiente() {
        noTerminal = "";
        conjuntoSiguiente = new ArrayList();
    }

    public void agregarConjuntoSiguiente(String cadena) {
        conjuntoSiguiente.add(cadena);
    }

    /**
     * GETTER AND SETER ATRIBUTOS
     */

    public void setConjuntoSiguiente(List conjuntoSiguiente) {
        this.conjuntoSiguiente = conjuntoSiguiente;
    }

    public void setNoTerminal(String noTerminal) {
        this.noTerminal = noTerminal;
    }

    public List getConjuntoSiguiente() {
        return conjuntoSiguiente;
    }

    public String getNoTerminal() {
        return noTerminal;
    }
}
