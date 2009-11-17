/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sintacticanalizer.ec.generator;

import sintacticanalizer.ec.components.ASPNR;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import sintacticanalizer.ec.components.AnalisisASPNR;
import sintacticanalizer.ec.components.PosicionMatrizProduccion;
import sintacticanalizer.ec.components.Regla;
import sintacticanalizer.ec.response.RespuestaASPNR;
import sintacticanalizer.ec.response.RespuestaTablaASP;

/**
 *
 * @author Alida
 * Clase principal en la cual se llamaran a los metodos para generar la tabla ASP
 * asi como al analizador sintactico.
 */
public class GeneradorASPNR {

    private GeneradorTablaASP generadorASP = null;
    private List<PosicionMatrizProduccion> tabla;
    private String matriz[][] = null;
    private Vector fila1 = new Vector();
    private Vector col1 = new Vector();
    private String cadena[] = {"id", "+", "id", "*", "id", "$"};
    private List cadenaEntrada = null;
    private List<AnalisisASPNR> analisis = null;

    public GeneradorASPNR() {

        generadorASP = new GeneradorTablaASP();


    }

    /** Función que realiza la lectura de la gramatica insertada por el usuario
     * y realiza la impresion de la misma en pantalla.
     */
    public List leerGramatica() throws IOException {
        List gramaticas = new ArrayList();

        String s;
        System.out.println("Ingrese la gramatica: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while ((s = in.readLine()).compareTo("null") != 0) {
            gramaticas.add("" + s);
        }
        System.out.println("Gramatica a ser analizada: ");

        for (int i = 0; i < gramaticas.size(); i++) {
            String unaGramatica = (String) gramaticas.get(i);
            StringTokenizer tokens = new StringTokenizer(unaGramatica, ">", false);
            String leftPart = tokens.nextToken();
            String rightPart = tokens.nextToken();

            System.out.println(leftPart + "->" + rightPart);
        }
        return gramaticas;
    }

    /** Función que realiza la lectura de la cadena a ser comparada,
     * insertada por el usuario y realiza la impresion de la misma en pantalla.
     */
    public void leerCadena() throws IOException {
        Vector cad = new Vector();
        String unaCadena = null;
        String s;
        System.out.println("Ingrese la gramatica: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while ((s = in.readLine()).compareTo("null") != 0) {
            cad.add("" + s);
        }


        for (int i = 0; i < cad.size(); i++) {
            unaCadena = unaCadena + (String) cad.get(i);
        }

        System.out.println("Cadena a ser analizada: " + unaCadena);
    }

    /** 
     * Carga la matriz ASP la cual sera entrada para el ASPNR
     */
    public void cargarTabla() {
        fila1 = new Vector();
        col1 = new Vector();
        int a, b;
        tabla = generadorASP.getPosMatrizProdList();

        //1- Se obtiene todos los terminales y noterminales de la gramatica
        for (int i = 0; i < tabla.size(); i++) {
            PosicionMatrizProduccion pos = tabla.get(i);
            String aux1 = pos.getNoTerminal();
            String aux2 = pos.getTerminal();

            if (!fila1.contains(aux1)) {
                fila1.add(aux1);
                a = fila1.indexOf(aux1);
            } else {
                a = fila1.indexOf(aux1);
            }

            if (!col1.contains(aux2)) {
                col1.add(aux2);
                b = col1.indexOf(aux2);
            } else {
                b = col1.indexOf(aux2);
            }
        }

        //2- Se dimensiona la matriz y se carga
        //en el contenido de la tabla en la matriz
        matriz = new String[fila1.size()][col1.size()];
        for (int i = 0; i < tabla.size(); i++) {
            PosicionMatrizProduccion pos = tabla.get(i);
            String aux1 = pos.getNoTerminal();
            String aux2 = pos.getTerminal();

            if (!fila1.contains(aux1)) {
                fila1.add(aux1);
                a = fila1.indexOf(aux1);
            } else {
                a = fila1.indexOf(aux1);
            }

            if (!col1.contains(aux2)) {
                col1.add(aux2);
                b = col1.indexOf(aux2);
            } else {
                b = col1.indexOf(aux2);
            }

            matriz[a][b] = pos.getProduccion();
        }

        System.out.println(" Vector fila: ");
        for (int j = 0; j < fila1.size(); j++) {
            System.out.print(fila1.get(j) + "\t");
        }
        System.out.println();
        System.out.println("\n Vector Columna: ");
        for (int j = 0; j < col1.size(); j++) {
            System.out.print(col1.get(j) + "\t");
        }
        System.out.println();
        System.out.println(" \n Matriz ASP: ");
        for (int k = 0; k < fila1.size(); k++) {
            for (int j = 0; j < col1.size(); j++) {
                System.out.print(matriz[k][j] + "\t");
            }
            System.out.println();
        }


    }

    /** Función que realiza la llamada a todos los metodos necesario para integrar
     * el analizador, tanto de lectura de gramaticas y cadenas, asi como al generador
     * de los conjuntos primero y sigte y de la tablaASP y por ultimo al ASPNR
     */
    public void generar() throws IOException {
        // leerGramatica();
        boolean estado = generadorASP.generar();

        if (estado == true) {
            cargarTabla();
            ASPNR asp = new ASPNR(matriz, fila1, col1);
            for (int i = 0; i < cadena.length; i++) {
                asp.analizar(cadena[i]);
            }
        } else {
            System.out.println("La gramatica es ambigua, esto se detecto en la tabla, " +
                    "cuando dos producciones se iba a insertar en la misma posicion");
        }
    }

    /**
     * Cargar Terminales y No terminales.
     */
    private void cargarTerminalesYNoTerminales() {
        fila1 = new Vector();
        col1 = new Vector();
        int a, b;
        tabla = generadorASP.getPosMatrizProdList();

        //1- Se obtiene todos los terminales y noterminales de la gramatica
        for (int i = 0; i < tabla.size(); i++) {
            PosicionMatrizProduccion pos = tabla.get(i);
            String aux1 = pos.getNoTerminal();
            String aux2 = pos.getTerminal();

            if (!fila1.contains(aux1)) {
                fila1.add(aux1);
                a = fila1.indexOf(aux1);
            } else {
                a = fila1.indexOf(aux1);
            }

            if (!col1.contains(aux2)) {
                col1.add(aux2);
                b = col1.indexOf(aux2);
            } else {
                b = col1.indexOf(aux2);
            }
        }
    }

    /**
     * Validar la cadena de entrada.
     */
    private boolean validarCadenaEntrada() {
        boolean result = false;

        int contadorValidos = 0;

        for (int i = 0; i < cadenaEntrada.size(); i++) {

            String unTerminalDeEntrada = (String) cadenaEntrada.get(i);

            for (int j = 0; j < col1.size(); j++) {

                String terminal = (String) col1.get(j);

                if (terminal.compareTo(unTerminalDeEntrada) == 0) {
                    result = true;
                    contadorValidos++;
                }

            }

            if (result == false) {
                return result;
            } else {
                result = false;
            }
        }

        if (contadorValidos == cadenaEntrada.size()) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    /** 
     * Carga la matriz ASP la cual sera entrada para el ASPNR
     */
    public void cargarMatriz() {

        int a, b;

        //2- Se dimensiona la matriz y se carga
        //en el contenido de la tabla en la matriz
        matriz = new String[fila1.size()][col1.size()];
        for (int i = 0; i < tabla.size(); i++) {
            PosicionMatrizProduccion pos = tabla.get(i);
            String aux1 = pos.getNoTerminal();
            String aux2 = pos.getTerminal();

            if (!fila1.contains(aux1)) {
                fila1.add(aux1);
                a = fila1.indexOf(aux1);
            } else {
                a = fila1.indexOf(aux1);
            }

            if (!col1.contains(aux2)) {
                col1.add(aux2);
                b = col1.indexOf(aux2);
            } else {
                b = col1.indexOf(aux2);
            }

            matriz[a][b] = pos.getProduccion();
        }

        System.out.println(" Vector fila: ");
        for (int j = 0; j < fila1.size(); j++) {
            System.out.print(fila1.get(j) + "\t");
        }
        System.out.println();
        System.out.println("\n Vector Columna: ");
        for (int j = 0; j < col1.size(); j++) {
            System.out.print(col1.get(j) + "\t");
        }
        System.out.println();
        System.out.println(" \n Matriz ASP: ");
        for (int k = 0; k < fila1.size(); k++) {
            for (int j = 0; j < col1.size(); j++) {
                System.out.print(matriz[k][j] + "\t");
            }
            System.out.println();
        }


    }

    /**
     * Función que realiza la llamada a todos los metodos necesario para integrar
     * el analizador, tanto de lectura de gramaticas y cadenas, asi como al generador
     * de los conjuntos primero y sigte y de la tablaASP y por ultimo al ASPNR
     */
    public RespuestaASPNR generar(List<Regla> reglas, RespuestaTablaASP estadoTablaASP) throws IOException {

        RespuestaASPNR estadoASPNR = new RespuestaASPNR();

        //1- Validar si la tabla no tuvo error
        if (estadoTablaASP.getError() == false) {

            //2- Cargar los Terminales y No terminales
            cargarTerminalesYNoTerminales();

            //3- Verificar la valides de la cadena de Entrada
            boolean esValida = validarCadenaEntrada();

            if (esValida == true) {

                //4- Cargar la Matriz
                cargarMatriz();

                //5- Analizar cada terminal de la cadena de entrada
                ASPNR asp = new ASPNR(matriz, fila1, col1);
                for (int i = 0; i < cadenaEntrada.size(); i++) {
                    asp.analizarTerminal(((String) cadenaEntrada.get(i)));
                }
                analisis = asp.getAnalisis();

                //6- Realizar la Derivación por la Izquierda.
//                asp.derivarPorIzquierda();


                estadoASPNR.setError(false);
                estadoASPNR.setMensaje("Análisis realizada satisfactoriamente!!!");

            } else {

                estadoASPNR.setError(true);
                estadoASPNR.setMensaje("La cadena de entrada es inválida!!!");

            }


        } else {
            
            estadoASPNR.setError(true);
            estadoASPNR.setMensaje(estadoTablaASP.getMensaje());

        }

        return estadoASPNR;
    }

    public static void main(String argv[]) throws IOException {
        new GeneradorASPNR().generar();


    }

    /**
     * GETTER AND SETER ATRIBUTOS
     */
    public List<PosicionMatrizProduccion> getTabla() {
        return tabla;
    }

    public void setTabla(List<PosicionMatrizProduccion> tabla) {
        this.tabla = tabla;
    }

    public GeneradorTablaASP getGeneradorASP() {
        return generadorASP;
    }

    public void setGeneradorASP(GeneradorTablaASP generadorASP) {
        this.generadorASP = generadorASP;
    }

    public List getCadenaEntrada() {
        return cadenaEntrada;
    }

    public void setCadenaEntrada(List cadenaEntrada) {
        this.cadenaEntrada = cadenaEntrada;
    }

    public List<AnalisisASPNR> getAnalisis() {
        return analisis;
    }

    public void setAnalisis(List<AnalisisASPNR> analisis) {
        this.analisis = analisis;
    }
}
