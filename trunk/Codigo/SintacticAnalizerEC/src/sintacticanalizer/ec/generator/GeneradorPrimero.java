/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sintacticanalizer.ec.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import sintacticanalizer.ec.components.PosicionMatrizProduccion;
import sintacticanalizer.ec.components.Primero;
import sintacticanalizer.ec.response.RespuestaPrimero;

/**
 *
 * @author Guido Casco
 */
public class GeneradorPrimero {

    private List noTerminales = null;
    private List rightParts = null;
    private List gramaticas = null;
    private List<Primero> primeros = null;
    private List<PosicionMatrizProduccion> posMatrizProdList = null;

    public GeneradorPrimero() {
        noTerminales = new ArrayList();
        rightParts = new ArrayList();
        gramaticas = new ArrayList();
        primeros = new ArrayList();
        posMatrizProdList = new ArrayList();
    }

    public RespuestaPrimero generar() {

        RespuestaPrimero rp = new RespuestaPrimero();

        //1- Generar los primeros de los no terminales
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
                            //Agregar posicion de la matriz
                            PosicionMatrizProduccion pos = new PosicionMatrizProduccion();
                            pos.setNoTerminal(leftPart);
                            pos.setTerminal("primero(" + terminalONoTerminal + ")");
                            pos.setProduccion(oracion);
                            posMatrizProdList.add(pos);

                            //Agregar conjunto de no terminales
                            unPrimero.agregarConjuntoPrimero("primero(" + terminalONoTerminal + ")");
                        } else {

                            //Agregar posicion de la matriz
                            PosicionMatrizProduccion pos = new PosicionMatrizProduccion();
                            pos.setNoTerminal(leftPart);
                            pos.setTerminal(terminalONoTerminal);
                            pos.setProduccion(oracion);
                            posMatrizProdList.add(pos);

                            //Agregar terminal producido
                            unPrimero.agregarConjuntoPrimero(terminalONoTerminal);
                        }

                    } else {
                        if (esNoTerminal(oracion)) {
                            //Agregar posicion de la matriz
                            PosicionMatrizProduccion pos = new PosicionMatrizProduccion();
                            pos.setNoTerminal(leftPart);
                            pos.setTerminal("primero(" + oracion + ")");
                            pos.setProduccion(oracion);
                            posMatrizProdList.add(pos);

                            //Agregar conjunto de no terminales
                            unPrimero.agregarConjuntoPrimero("primero(" + oracion + ")");
                        } else {

                            //Agregar posicion de la matriz
                            PosicionMatrizProduccion pos = new PosicionMatrizProduccion();
                            pos.setNoTerminal(leftPart);
                            pos.setTerminal(oracion);
                            pos.setProduccion(oracion);
                            posMatrizProdList.add(pos);

                            //Agregar terminal producido
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
                        //Agregar posicion de la matriz
                        PosicionMatrizProduccion pos = new PosicionMatrizProduccion();
                        pos.setNoTerminal(leftPart);
                        pos.setTerminal("primero(" + terminalONoTerminal + ")");
                        pos.setProduccion(rightPart);
                        posMatrizProdList.add(pos);

                        //Agregar conjunto de no terminales
                        unPrimero.agregarConjuntoPrimero("primero(" + terminalONoTerminal + ")");
                    } else {

                        //Agregar posicion de la matriz
                        PosicionMatrizProduccion pos = new PosicionMatrizProduccion();
                        pos.setNoTerminal(leftPart);
                        pos.setTerminal(terminalONoTerminal);
                        pos.setProduccion(rightPart);
                        posMatrizProdList.add(pos);

                        //Agregar terminal producido
                        unPrimero.agregarConjuntoPrimero(terminalONoTerminal);
                    }

                } else {
                    if (esNoTerminal(rightPart)) {
                        //Agregar posicion de la matriz
                        PosicionMatrizProduccion pos = new PosicionMatrizProduccion();
                        pos.setNoTerminal(leftPart);
                        pos.setTerminal("primero(" + rightPart + ")");
                        pos.setProduccion(rightPart);
                        posMatrizProdList.add(pos);

                        //Agregar conjunto de no terminales
                        unPrimero.agregarConjuntoPrimero("primero(" + rightPart + ")");
                    } else {
                        //Agregar posicion de la matriz
                        PosicionMatrizProduccion pos = new PosicionMatrizProduccion();
                        pos.setNoTerminal(leftPart);
                        pos.setTerminal(rightPart);
                        pos.setProduccion(rightPart);
                        posMatrizProdList.add(pos);

                        //Agregar terminal producido
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

        try {
            for (int i = 0; i < primeros.size(); i++) {
                Primero unPrimero = primeros.get(i);
                unPrimero.setConjuntoPrimero(reemplazarPrimeros(unPrimero));
            }
        } catch (StackOverflowError e) {

            rp.setError(true);
            rp.setMensaje("La Gramática es Recursiva por la Izquierda, verificación realizada en primero");

            return rp;
        }



        System.out.println("***Primero: Segunda Pasada***");
        imprimirConjuntoPrimeroDeLosNoTerminales();

        System.out.println("***Matriz: 1era. Pasada Primero: Con texto primero sin interpretar el vacio***");
        imprimirMatriz();

        //2- Reemplazar el texto primero en la matriz por los terminales
        List<PosicionMatrizProduccion> auxPosMatrizProdList = new ArrayList();
        for (int i = 0; i < posMatrizProdList.size(); i++) {
            PosicionMatrizProduccion pos = posMatrizProdList.get(i);

            String terminal = pos.getTerminal();
            int posicionEnTerminal = terminal.indexOf("primero");
            if (posicionEnTerminal >= 0) {
                int posicionParentesisDerecho = terminal.indexOf(")");
                String noTerminal = terminal.substring(8, posicionParentesisDerecho);
                auxPosMatrizProdList.addAll(getPosMatrizProdListFromPrimero(noTerminal, pos));
            } else {
                auxPosMatrizProdList.add(pos);
            }
        }

        posMatrizProdList = auxPosMatrizProdList;

        System.out.println("***Matriz: 2da. Pasada Primero: Sin primero pero sin interpretar vacio***");
        imprimirMatriz();

        //3- Verificar la ambiguedad
        boolean esAmbiguo = validarAmbiguedad();

        if(esAmbiguo == true) {

            rp.setError(true);
            rp.setMensaje("La Gramática es ambigua, verificación realizada en primero");

            return rp;

        } else {
            rp.setError(false);
            rp.setMensaje("La Gramática no es ambigua, verificación realizada en primero");

            return rp;
        }
    }

    private boolean validarAmbiguedad() {
        
        boolean result = false;
        
        for (int i = 0; i < primeros.size(); i++) {
            Primero unPrimero = primeros.get(i);

            String noTerminal = unPrimero.getNoTerminal();
            List conjuntoPrimero = unPrimero.getConjuntoPrimero();

            for (int j = 0; j < conjuntoPrimero.size()-1; j++) {
                String terminal1 = (String) conjuntoPrimero.get(j);

                for (int k = j+1; k < conjuntoPrimero.size(); k++) {
                    String terminal2 = (String) conjuntoPrimero.get(k);

                    if(terminal1.compareTo(terminal2) == 0) {
                        result = true;
                        break;
                    }

                }
            }
        }

        return result;
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

    private List<PosicionMatrizProduccion> getPosMatrizProdListFromPrimero(String noTerminal, PosicionMatrizProduccion pos) {
        List<PosicionMatrizProduccion> posList = new ArrayList();

        Primero primNoTerminal = getPrimeroByNoTerminal(noTerminal);

        List conjuntoPrimero = primNoTerminal.getConjuntoPrimero();

        for (int i = 0; i < conjuntoPrimero.size(); i++) {

            String terminal = (String) conjuntoPrimero.get(i);

            PosicionMatrizProduccion newPos = new PosicionMatrizProduccion();
            newPos.setNoTerminal(pos.getNoTerminal());
            newPos.setTerminal(terminal);
            newPos.setProduccion(pos.getProduccion());
            posList.add(newPos);
        }


        return posList;
    }

    private Primero getPrimeroByNoTerminal(String noTerminal) {

        Primero unPrimero = null;
        for (int i = 0; i < primeros.size(); i++) {

            unPrimero = primeros.get(i);

            if (unPrimero.getNoTerminal().compareTo(noTerminal) == 0) {
                break;
            }
        }

        return unPrimero;
    }

    private void imprimirMatriz() {
        for (int i = 0; i < posMatrizProdList.size(); i++) {
            PosicionMatrizProduccion pos = posMatrizProdList.get(i);

            System.out.println("No Terminal: " + pos.getNoTerminal());
            System.out.println("Terminal: " + pos.getTerminal());
            System.out.println("Produccion: " + pos.getNoTerminal() + ">" + pos.getProduccion());

        }
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
        for (int i = 0; i < unPrimero.getConjuntoPrimero().size(); i++) {

            String proposicion = (String) unPrimero.getConjuntoPrimero().get(i);
            if (proposicion.indexOf("primero") >= 0) {

                for (int j = 0; j < primeros.size(); j++) {

                    Primero primeroScan = primeros.get(j);
                    String primero = "primero(" + primeroScan.getNoTerminal() + ")";
                    if (primero.compareTo(proposicion) == 0) {
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

    public List<PosicionMatrizProduccion> getPosMatrizProdList() {
        return posMatrizProdList;
    }

    public void setPosMatrizProdList(List<PosicionMatrizProduccion> posMatrizProdList) {
        this.posMatrizProdList = posMatrizProdList;
    }
}
