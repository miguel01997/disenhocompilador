/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sintacticanalizer.ec.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import sintacticanalizer.ec.components.Primero;
import sintacticanalizer.ec.components.Siguiente;

/**
 *
 * @author Id Teknology
 */
public class GeneradorSiguiente {

    private List noTerminales = null;
    private List rightParts = null;
    private List gramaticas = null;
    private List<Primero> primeros = null;
    private List<Siguiente> siguientes = null;

    public GeneradorSiguiente() {
        noTerminales = new ArrayList();
        rightParts = new ArrayList();
        gramaticas = new ArrayList();
        primeros = new ArrayList();
        siguientes = new ArrayList();
    }

    public void generar() {

        //1- Se construye la lista de siguientes sin el conjunto de siguiente.
        for (int i = 0; i < noTerminales.size(); i++) {

            String noTerminalEnEstudio = (String) noTerminales.get(i);

            Siguiente unSiguiente = new Siguiente();
            unSiguiente.setNoTerminal(noTerminalEnEstudio);

            siguientes.add(unSiguiente);
        }

        //2- Generar los siguientes de los no terminales y con el conjunto primero.
        //   a- En este caso se usara en los siguientes textos como siguiente(X)
        //      reemplazando temporalmente al grupo de terminales correspondientes.
        for (int j = 0; j < noTerminales.size(); j++) {
            String parteIzq = (String) noTerminales.get(j);
            String parteDer = (String) rightParts.get(j);

            Siguiente siguienteEnEstudio = siguientes.get(j);

            //1er. Caso: Poner $ en el simbolo inicial.
            if (j == 0) {
                siguienteEnEstudio.agregarConjuntoSiguiente("$");
            }


            int posOr = parteDer.indexOf("|");
            if (posOr >= 0) {

                StringTokenizer tokens = new StringTokenizer(parteDer, "|", false);

                while (tokens.hasMoreTokens()) {

                    String oracion = tokens.nextToken();

                    int posWhiteSpace = oracion.indexOf(" ");
                if (posWhiteSpace >= 0) {
                    StringTokenizer tokensO = new StringTokenizer(oracion, " ", false);

                    //2er. Caso: Existe A -> B Beta esto implica
                    //           que a sgte(B) se agrega Prim(Beta)
                    //           menos vacio "e".
                    if (tokensO.countTokens() == 2) {

                        //B y Beta son no terminales
                        String simbolo1 = tokensO.nextToken();
                        String simbolo2 = tokensO.nextToken();

                        if (esNoTerminal(simbolo1) == true &&
                                esNoTerminal(simbolo2) == true) {

                            //Se busca siguiente B en siguientes.
                            Siguiente siguienteB = null;
                            for (int l = 0; l < siguientes.size(); l++) {
                                Siguiente sgteBuscado = siguientes.get(l);

                                if (simbolo1.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                    siguienteB = sgteBuscado;
                                    break;
                                }
                            }

                            //Se busca primero Beta en primeros.
                            Primero primeroBeta = null;
                            for (int l = 0; l < primeros.size(); l++) {
                                Primero primBuscado = primeros.get(l);

                                if (simbolo2.compareTo(primBuscado.getNoTerminal()) == 0) {
                                    primeroBeta = primBuscado;
                                    break;
                                }
                            }

                            if (tieneVacio(primeroBeta) == true) {
                                siguienteB.agregarConjuntoSiguiente("siguiente(" + siguienteEnEstudio.getNoTerminal() + ")");
                            }

                            //Se copia todos los terminales del primero Beta a siguiente B
                            //sin el vacio "e"
                            for (int l = 0; l < primeroBeta.getConjuntoPrimero().size(); l++) {
                                String terminal = (String) primeroBeta.getConjuntoPrimero().get(l);
                                if (terminal.compareTo("e") != 0) {
                                    siguienteB.agregarConjuntoSiguiente(terminal);
                                }
                            }

                        //B es no terminal y Beta es terminal
                        } else if (esNoTerminal(simbolo1) == true &&
                                esNoTerminal(simbolo2) == false) {

                            //Se busca siguiente B en siguientes.
                            Siguiente siguienteB = null;
                            for (int l = 0; l < siguientes.size(); l++) {
                                Siguiente sgteBuscado = siguientes.get(l);

                                if (simbolo1.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                    siguienteB = sgteBuscado;
                                    break;
                                }
                            }

                            //Se agrega en siguienteB el primero(terminal)
                            //que es igual al mismo terminal.
                            siguienteB.agregarConjuntoSiguiente(simbolo2);

                        //3er. Caso: A -> alfa B, se tiene que copiar
                        //           todo siguiente(A) a siguiente(B)
                        } else if (esNoTerminal(simbolo1) == false &&
                                esNoTerminal(simbolo2) == true) {

                            //Se busca siguiente B en siguientes.
                            Siguiente siguienteB = null;
                            for (int l = 0; l < siguientes.size(); l++) {
                                Siguiente sgteBuscado = siguientes.get(l);

                                if (simbolo2.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                    siguienteB = sgteBuscado;
                                    break;
                                }
                            }

                            //Copiar todo lo que tiene siguiente(A) en
                            //siguiente(B)
                            siguienteB.agregarConjuntoSiguiente("siguiente(" + siguienteEnEstudio.getNoTerminal() + ")");

                        }

                    //2do. Caso: A -> alfa B Beta esto implica
                    //           que a sgte(B) se le agrega Prim(Beta)
                    //           menos vacio "e".
                    } else if (tokensO.countTokens() == 3) {

                        String simbolo1 = tokensO.nextToken();
                        String simbolo2 = tokensO.nextToken();
                        String simbolo3 = tokensO.nextToken();

                        //alfa es terminal, B es no terminal y Beta es no terminal
                        if (esNoTerminal(simbolo1) == false &&
                                esNoTerminal(simbolo2) == true) {


                            if (esNoTerminal(simbolo3) == false) {
                                //Se busca siguiente B en siguientes.
                                Siguiente siguienteB = null;
                                for (int l = 0; l < siguientes.size(); l++) {
                                    Siguiente sgteBuscado = siguientes.get(l);

                                    if (simbolo2.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                        siguienteB = sgteBuscado;
                                        break;
                                    }
                                }

                                //Se agrega en siguienteB el primero(terminal)
                                //que es igual al mismo terminal.
                                siguienteB.agregarConjuntoSiguiente(simbolo3);
                            } else {

                                //Se busca siguiente B en siguientes.
                                Siguiente siguienteB = null;
                                for (int l = 0; l < siguientes.size(); l++) {
                                    Siguiente sgteBuscado = siguientes.get(l);

                                    if (simbolo2.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                        siguienteB = sgteBuscado;
                                        break;
                                    }
                                }

                                //Se busca primero Beta en primeros.
                                Primero primeroBeta = null;
                                for (int l = 0; l < primeros.size(); l++) {
                                    Primero primBuscado = primeros.get(l);

                                    if (simbolo3.compareTo(primBuscado.getNoTerminal()) == 0) {
                                        primeroBeta = primBuscado;
                                        break;
                                    }
                                }

                                if (tieneVacio(primeroBeta) == true) {
                                    siguienteB.agregarConjuntoSiguiente("siguiente(" + siguienteEnEstudio.getNoTerminal() + ")");
                                }

                                //Se copia todos los terminales del primero Beta a siguiente B
                                //sin el vacio "e"
                                for (int l = 0; l < primeroBeta.getConjuntoPrimero().size(); l++) {
                                    String terminal = (String) primeroBeta.getConjuntoPrimero().get(l);
                                    if (terminal.compareTo("e") != 0) {
                                        siguienteB.agregarConjuntoSiguiente(terminal);
                                    }
                                }

                            }
                        //alfa es terminal, B es no terminal y Beta es terminal
                        } else if (esNoTerminal(simbolo1) == false &&
                                esNoTerminal(simbolo2) == true) {
                        }
                    }

                } else {
                    if (oracion.compareTo("e") != 0) {

                        //3er. Caso: Existe A -> B esto implica
                        //           todos los terminales en siguiente(A)
                        //           se copia en siguiente(B).

                        //B es no terminal
                        String simbolo1 = oracion;

                        if (esNoTerminal(simbolo1) == true) {

                            if (simbolo1.compareTo(siguienteEnEstudio.getNoTerminal()) == 0) {

                                //Se busca siguiente B en siguientes.
                                Siguiente siguienteB = null;
                                for (int l = 0; l < siguientes.size(); l++) {
                                    Siguiente sgteBuscado = siguientes.get(l);

                                    if (simbolo1.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                        siguienteB = sgteBuscado;
                                        break;
                                    }
                                }

                                //Se copia todo lo siguiente(A) en
                                //siguiente(B)
                                for (int l = 0; l < siguienteEnEstudio.getConjuntoSiguiente().size(); l++) {

                                    String terminal = (String) siguienteEnEstudio.getConjuntoSiguiente().get(l);

                                    if (terminal.compareTo("e") != 0) {
                                        siguienteB.agregarConjuntoSiguiente(terminal);
                                    }
                                }
                            }
                        }
                    }
                }
                }
            } else {
                String oracion = parteDer;

                int posWhiteSpace = oracion.indexOf(" ");
                if (posWhiteSpace >= 0) {
                    StringTokenizer tokensO = new StringTokenizer(oracion, " ", false);

                    //2er. Caso: Existe A -> B Beta esto implica
                    //           que a sgte(B) se agrega Prim(Beta)
                    //           menos vacio "e".
                    if (tokensO.countTokens() == 2) {

                        //B y Beta son no terminales
                        String simbolo1 = tokensO.nextToken();
                        String simbolo2 = tokensO.nextToken();

                        if (esNoTerminal(simbolo1) == true &&
                                esNoTerminal(simbolo2) == true) {

                            //Se busca siguiente B en siguientes.
                            Siguiente siguienteB = null;
                            for (int l = 0; l < siguientes.size(); l++) {
                                Siguiente sgteBuscado = siguientes.get(l);

                                if (simbolo1.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                    siguienteB = sgteBuscado;
                                    break;
                                }
                            }

                            //Se busca primero Beta en primeros.
                            Primero primeroBeta = null;
                            for (int l = 0; l < primeros.size(); l++) {
                                Primero primBuscado = primeros.get(l);

                                if (simbolo2.compareTo(primBuscado.getNoTerminal()) == 0) {
                                    primeroBeta = primBuscado;
                                    break;
                                }
                            }

                            if (tieneVacio(primeroBeta) == true) {
                                siguienteB.agregarConjuntoSiguiente("siguiente(" + siguienteEnEstudio.getNoTerminal() + ")");
                            }

                            //Se copia todos los terminales del primero Beta a siguiente B
                            //sin el vacio "e"
                            for (int l = 0; l < primeroBeta.getConjuntoPrimero().size(); l++) {
                                String terminal = (String) primeroBeta.getConjuntoPrimero().get(l);
                                if (terminal.compareTo("e") != 0) {
                                    siguienteB.agregarConjuntoSiguiente(terminal);
                                }
                            }

                        //B es no terminal y Beta es terminal
                        } else if (esNoTerminal(simbolo1) == true &&
                                esNoTerminal(simbolo2) == false) {

                            //Se busca siguiente B en siguientes.
                            Siguiente siguienteB = null;
                            for (int l = 0; l < siguientes.size(); l++) {
                                Siguiente sgteBuscado = siguientes.get(l);

                                if (simbolo1.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                    siguienteB = sgteBuscado;
                                    break;
                                }
                            }

                            //Se agrega en siguienteB el primero(terminal)
                            //que es igual al mismo terminal.
                            siguienteB.agregarConjuntoSiguiente(simbolo2);

                        //3er. Caso: A -> alfa B, se tiene que copiar
                        //           todo siguiente(A) a siguiente(B)
                        } else if (esNoTerminal(simbolo1) == false &&
                                esNoTerminal(simbolo2) == true) {

                            //Se busca siguiente B en siguientes.
                            Siguiente siguienteB = null;
                            for (int l = 0; l < siguientes.size(); l++) {
                                Siguiente sgteBuscado = siguientes.get(l);

                                if (simbolo2.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                    siguienteB = sgteBuscado;
                                    break;
                                }
                            }

                            //Copiar todo lo que tiene siguiente(A) en
                            //siguiente(B)
                            siguienteB.agregarConjuntoSiguiente("siguiente(" + siguienteEnEstudio.getNoTerminal() + ")");

                        }

                    //2do. Caso: A -> alfa B Beta esto implica
                    //           que a sgte(B) se le agrega Prim(Beta)
                    //           menos vacio "e".
                    } else if (tokensO.countTokens() == 3) {

                        String simbolo1 = tokensO.nextToken();
                        String simbolo2 = tokensO.nextToken();
                        String simbolo3 = tokensO.nextToken();

                        //alfa es terminal, B es no terminal y Beta es no terminal
                        if (esNoTerminal(simbolo1) == false &&
                                esNoTerminal(simbolo2) == true) {


                            if (esNoTerminal(simbolo3) == false) {
                                //Se busca siguiente B en siguientes.
                                Siguiente siguienteB = null;
                                for (int l = 0; l < siguientes.size(); l++) {
                                    Siguiente sgteBuscado = siguientes.get(l);

                                    if (simbolo2.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                        siguienteB = sgteBuscado;
                                        break;
                                    }
                                }

                                //Se agrega en siguienteB el primero(terminal)
                                //que es igual al mismo terminal.
                                siguienteB.agregarConjuntoSiguiente(simbolo3);
                            } else {

                                //Se busca siguiente B en siguientes.
                                Siguiente siguienteB = null;
                                for (int l = 0; l < siguientes.size(); l++) {
                                    Siguiente sgteBuscado = siguientes.get(l);

                                    if (simbolo2.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                        siguienteB = sgteBuscado;
                                        break;
                                    }
                                }

                                //Se busca primero Beta en primeros.
                                Primero primeroBeta = null;
                                for (int l = 0; l < primeros.size(); l++) {
                                    Primero primBuscado = primeros.get(l);

                                    if (simbolo3.compareTo(primBuscado.getNoTerminal()) == 0) {
                                        primeroBeta = primBuscado;
                                        break;
                                    }
                                }

                                if (tieneVacio(primeroBeta) == true) {
                                    siguienteB.agregarConjuntoSiguiente("siguiente(" + siguienteEnEstudio.getNoTerminal() + ")");
                                }

                                //Se copia todos los terminales del primero Beta a siguiente B
                                //sin el vacio "e"
                                for (int l = 0; l < primeroBeta.getConjuntoPrimero().size(); l++) {
                                    String terminal = (String) primeroBeta.getConjuntoPrimero().get(l);
                                    if (terminal.compareTo("e") != 0) {
                                        siguienteB.agregarConjuntoSiguiente(terminal);
                                    }
                                }

                            }
                        //alfa es terminal, B es no terminal y Beta es terminal
                        } else if (esNoTerminal(simbolo1) == false &&
                                esNoTerminal(simbolo2) == true) {
                        }
                    }

                } else {
                    if (oracion.compareTo("e") != 0) {

                        //3er. Caso: Existe A -> B esto implica
                        //           todos los terminales en siguiente(A)
                        //           se copia en siguiente(B).

                        //B es no terminal
                        String simbolo1 = oracion;

                        if (esNoTerminal(simbolo1) == true) {

                            if (simbolo1.compareTo(siguienteEnEstudio.getNoTerminal()) == 0) {

                                //Se busca siguiente B en siguientes.
                                Siguiente siguienteB = null;
                                for (int l = 0; l < siguientes.size(); l++) {
                                    Siguiente sgteBuscado = siguientes.get(l);

                                    if (simbolo1.compareTo(sgteBuscado.getNoTerminal()) == 0) {
                                        siguienteB = sgteBuscado;
                                        break;
                                    }
                                }

                                //Se copia todo lo siguiente(A) en
                                //siguiente(B)
                                for (int l = 0; l < siguienteEnEstudio.getConjuntoSiguiente().size(); l++) {

                                    String terminal = (String) siguienteEnEstudio.getConjuntoSiguiente().get(l);

                                    if (terminal.compareTo("e") != 0) {
                                        siguienteB.agregarConjuntoSiguiente(terminal);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("***Siguiente: Primera Pasada***");
        imprimirConjuntoSiguienteDeLosNoTerminales();
    }

    private boolean tieneVacio(Primero primeroBeta) {

        boolean result = false;

        for (int i = 0; i < primeroBeta.getConjuntoPrimero().size(); i++) {

            String terminal = (String) primeroBeta.getConjuntoPrimero().get(i);

            if (terminal.compareTo("e") == 0) {
                result = true;
                break;
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

    private void imprimirConjuntoSiguienteDeLosNoTerminales() {

        for (int i = 0; i < siguientes.size(); i++) {

            Siguiente unSiguiente = (Siguiente) siguientes.get(i);

            System.out.print("siguiente(" + unSiguiente.getNoTerminal() + ") = {");
            for (int j = 0; j < unSiguiente.getConjuntoSiguiente().size(); j++) {

                if (j < unSiguiente.getConjuntoSiguiente().size() - 1) {
                    System.out.print(unSiguiente.getConjuntoSiguiente().get(j) + ",");
                } else {
                    System.out.print(unSiguiente.getConjuntoSiguiente().get(j) + "}");
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

    public List<Siguiente> getSiguientes() {
        return siguientes;
    }

    public void setSiguientes(List<Siguiente> siguientes) {
        this.siguientes = siguientes;
    }

    public List getGramaticas() {
        return gramaticas;
    }

    public void setGramaticas(List gramaticas) {
        this.gramaticas = gramaticas;
    }

    public List<Primero> getPrimeros() {
        return primeros;
    }

    public void setPrimeros(List<Primero> primeros) {
        this.primeros = primeros;
    }
}
