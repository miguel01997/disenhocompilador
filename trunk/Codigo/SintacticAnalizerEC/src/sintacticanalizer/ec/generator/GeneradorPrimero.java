/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sintacticanalizer.ec.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import sintacticanalizer.ec.components.Primero;

/**
 *
 * @author Id Teknology
 */
public class GeneradorPrimero {

    private List noTerminales = null;
    private List rightParts = null;
    private List gramaticas = null;
    private List<Primero> primeros = null;

    public GeneradorPrimero() {
        noTerminales = new ArrayList();
        rightParts = new ArrayList();
        gramaticas = new ArrayList();
        primeros = new ArrayList();
    }

    public void generar() {

        //2- Generar los primeros de los no terminales
        //   a- 1era Pasada: Se obtendra el conjunto primero
        //      pero con los no terminales primero se pondra
        //      "primero(noTerminal)" y en la segunda pasada
        //      van a ser reemplazados por sus correspondientes
        //      terminales.
        for (int i = 0; i < noTerminales.size(); i++) {
            String leftPart = (String) noTerminales.get(i);
            String rightPart = (String) rightParts.get(i);

            Primero unPrimero = new Primero();
            unPrimero.setNoTerminal(leftPart);

            int posOr = rightPart.indexOf("|");
            if (posOr >= 0) {

                StringTokenizer tokens = new StringTokenizer(rightPart, "|", false);

                while (tokens.hasMoreTokens()) {

                    String oracion = tokens.nextToken();

                    int posWhiteSpace = oracion.indexOf(" ");
                    if (posWhiteSpace >= 0) {
                        StringTokenizer tokensO = new StringTokenizer(oracion, " ", false);

                        String terminalONoTerminal = tokensO.nextToken();

                        if (esNoTerminal(terminalONoTerminal) == true) {
                            unPrimero.agregarConjuntoPrimero("primero(" + terminalONoTerminal + ")");
                        } else {
                            unPrimero.agregarConjuntoPrimero(terminalONoTerminal);
                        }

                    } else {
                        if (esNoTerminal(oracion)) {
                            unPrimero.agregarConjuntoPrimero("primero(" + oracion + ")");
                        } else {
                            unPrimero.agregarConjuntoPrimero("" + oracion + "");
                        }
                    }
                }
            } else {
                int posWhiteSpace = rightPart.indexOf(" ");
                if (posWhiteSpace >= 0) {


                    StringTokenizer tokensO = new StringTokenizer(rightPart, " ", false);

                    String terminalONoTerminal = tokensO.nextToken();


                    if (esNoTerminal(terminalONoTerminal) == true) {
                        unPrimero.agregarConjuntoPrimero("primero(" + terminalONoTerminal + ")");
                    } else {
                        unPrimero.agregarConjuntoPrimero(terminalONoTerminal);
                    }

                } else {
                    if (esNoTerminal(rightPart)) {
                        unPrimero.agregarConjuntoPrimero("primero(" + rightPart + ")");
                    } else {
                        unPrimero.agregarConjuntoPrimero("" + rightPart + "");
                    }
                }
            }

            primeros.add(unPrimero);
        }
        System.out.println("***Primero: Primera Pasada***");
        imprimirConjuntoPrimeroDeLosNoTerminales();

        //   b- 2da Pasada: Se reemplazara de forma recursiva los textos
        //      primero(noTerminal) por su valor.
        for (int i=0; i < primeros.size(); i++) {
            Primero unPrimero = primeros.get(i);
            unPrimero.setConjuntoPrimero(reemplazarPrimeros(unPrimero));
        }

        System.out.println("***Primero: Segunda Pasada***");
        imprimirConjuntoPrimeroDeLosNoTerminales();

        //   c- 3era Pasada: Eliminar terminales y vacios duplicados
        for (int i=0; i < primeros.size(); i++) {
            Primero unPrimero = primeros.get(i);
            
            for(int j=0; j<unPrimero.getConjuntoPrimero().size()-1; j++) {
                String terminalEnEstudio = (String) unPrimero.getConjuntoPrimero().get(j);
                        
                for(int k=j+1; k<unPrimero.getConjuntoPrimero().size(); k++) {
                    
                    String terminalVerificado = (String) unPrimero.getConjuntoPrimero().get(k);
                    if(terminalEnEstudio.compareTo(terminalVerificado) == 0)
                        unPrimero.getConjuntoPrimero().set(k, "");
                }
            }
        }

        for (int i=0; i < primeros.size(); i++) {
            Primero unPrimero = primeros.get(i);
            List auxConjuntoTerminales = new ArrayList();

            for(int j=0; j<unPrimero.getConjuntoPrimero().size(); j++) {
                String terminalEnEstudio = (String) unPrimero.getConjuntoPrimero().get(j);

                if(terminalEnEstudio.compareTo("") != 0)
                    auxConjuntoTerminales.add(terminalEnEstudio);
            }

            unPrimero.setConjuntoPrimero(auxConjuntoTerminales);
        }


        System.out.println("***Primero: Tercera Pasada***");
        imprimirConjuntoPrimeroDeLosNoTerminales();
    }

    private boolean esNoTerminal(String terminalONoTerminal) {

        boolean result = false;

        for (int i = 0; i < noTerminales.size(); i++) {
            String noTerminal = (String) noTerminales.get(i);
            if (terminalONoTerminal.compareTo(noTerminal) == 0) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Reemplaza los textos primero(noTerminal) por su correspondiente
     * valor de terminales de forma recursiva.
     *
     * @param unPrimero Un Primero.
     * @return Una lista de elementos terminales con duplicados.
     */
    private List reemplazarPrimeros(Primero unPrimero) {

        List result = new ArrayList();
        for(int i=0; i<unPrimero.getConjuntoPrimero().size(); i++) {

            String proposicion = (String) unPrimero.getConjuntoPrimero().get(i);
            if(proposicion.indexOf("primero") >= 0) {

                for(int j=0; j<primeros.size(); j++) {

                    Primero primeroScan = primeros.get(j);
                    String primero = "primero("+primeroScan.getNoTerminal()+")";
                    if(primero.compareTo(proposicion) == 0) {
                         result.addAll(reemplazarPrimeros(primeroScan));
                    }
                }

            } else {
                result.add(proposicion);
            }
        }

        return result;

    }

    private void imprimirConjuntoPrimeroDeLosNoTerminales() {

        for (int i = 0; i < primeros.size(); i++) {

            Primero unPrimero = (Primero) primeros.get(i);

            System.out.print("primero(" + unPrimero.getNoTerminal() + ") = {");
            for (int j = 0; j < unPrimero.getConjuntoPrimero().size(); j++) {

                if (j < unPrimero.getConjuntoPrimero().size() - 1) {
                    System.out.print(unPrimero.getConjuntoPrimero().get(j) + ",");
                } else {
                    System.out.print(unPrimero.getConjuntoPrimero().get(j) + "}");
                }
            }
            System.out.println();
        }

    }

    /**
     * GETTER AND SETER ATRIBUTOS
     */
    public List getNoTerminales() {
        return noTerminales;
    }

    public void setNoTerminales(List noTerminales) {
        this.noTerminales = noTerminales;
    }

    public List getRightParts() {
        return rightParts;
    }

    public void setRightParts(List rightParts) {
        this.rightParts = rightParts;
    }

    public List<Primero> getPrimeros() {
        return primeros;
    }

    public void setPrimeros(List<Primero> primeros) {
        this.primeros = primeros;
    }

    public List getGramaticas() {
        return gramaticas;
    }

    public void setGramaticas(List gramaticas) {
        this.gramaticas = gramaticas;
    }


    
}
