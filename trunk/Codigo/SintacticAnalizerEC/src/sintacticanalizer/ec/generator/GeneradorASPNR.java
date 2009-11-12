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
import sintacticanalizer.ec.components.PosicionMatrizProduccion;

/**
 *
 * @author Alida
 * Clase principal en la cual se llamaran a los metodos para generar la tabla ASP
 * asi como al analizador sintactico.
 */
public class GeneradorASPNR {

    private GeneradorTablaASP generadorASP;
    private List<PosicionMatrizProduccion> tabla;
    private String matriz[][] = null;
    private Vector fila1 = new Vector();
    private Vector col1 = new Vector();
    String cadena[] = {"id", "+", "id", "*", "id", "$"};

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
    //      this.cadena = cad;

    }

    /** Carga la matriz ASP la cual sera entrada para el ASPNR*/
    public void cargarTabla() {
        fila1 = new Vector();
        col1 = new Vector();
        int a, b;
        matriz = new String[10][10];
        tabla = generadorASP.getPosMatrizProdList();
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
        } else
            System.out.println("La gramatica es ambigua, esto se detecto en la tabla, " +
                               "cuando dos producciones se iba a insertar en la misma posicion");
    }

    public static void main(String argv[]) throws IOException {
        new GeneradorASPNR().generar();


    }
}
