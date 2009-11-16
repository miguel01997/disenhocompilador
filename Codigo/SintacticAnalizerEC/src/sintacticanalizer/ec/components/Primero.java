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

    private String noTerminalFormateado = "";
    private String conjuntoPrimeroFormateado = "";

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

    public String getNoTerminalFormateado() {

        noTerminalFormateado = "primero(" + noTerminal + ")";

        return noTerminalFormateado;
    }

    public String getConjuntoPrimeroFormateado() {

        conjuntoPrimeroFormateado = "";

        if (conjuntoPrimero.size() > 0) {
            
            conjuntoPrimeroFormateado = conjuntoPrimeroFormateado + "{ ";
            
            for (int j = 0; j < conjuntoPrimero.size(); j++) {

                if (j < conjuntoPrimero.size() - 1) {
                    conjuntoPrimeroFormateado = conjuntoPrimeroFormateado + conjuntoPrimero.get(j) + ", ";
                } else {
                    conjuntoPrimeroFormateado = conjuntoPrimeroFormateado + conjuntoPrimero.get(j);
                }
            }

            conjuntoPrimeroFormateado = conjuntoPrimeroFormateado + " }";
        } else if (conjuntoPrimero.size() == 0) {
            conjuntoPrimeroFormateado = "";
        }


        return conjuntoPrimeroFormateado;
    }
}
