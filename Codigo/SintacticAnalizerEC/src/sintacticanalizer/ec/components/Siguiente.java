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

    private String noTerminalFormateado = "";
    private String conjuntoSiguienteFormateado = "";

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

    public String getNoTerminalFormateado() {

        noTerminalFormateado = "siguiente(" + noTerminal + ")";

        return noTerminalFormateado;
    }

    public String getConjuntoSiguienteFormateado() {

        conjuntoSiguienteFormateado = "";

        if (conjuntoSiguiente.size() > 0) {

            conjuntoSiguienteFormateado = conjuntoSiguienteFormateado + "{ ";

            for (int j = 0; j < conjuntoSiguiente.size(); j++) {

                if (j < conjuntoSiguiente.size() - 1) {
                    conjuntoSiguienteFormateado = conjuntoSiguienteFormateado + conjuntoSiguiente.get(j) + ", ";
                } else {
                    conjuntoSiguienteFormateado = conjuntoSiguienteFormateado + conjuntoSiguiente.get(j);
                }
            }

            conjuntoSiguienteFormateado = conjuntoSiguienteFormateado + " }";
        } else if (conjuntoSiguiente.size() == 0) {
            conjuntoSiguienteFormateado = "";
        }


        return conjuntoSiguienteFormateado;
    }
}
