/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sintacticanalizer.ec.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sintacticanalizer.ec.components.PosicionMatrizProduccion;
import sintacticanalizer.ec.components.Primero;
import sintacticanalizer.ec.components.Siguiente;

/**
 *
 * @author Guido Casco
 */
public class GeneradorTablaASP {

    private List noTerminales = null;
    private List rightParts = null;
    private List<Primero> primeros = null;
    private List<Siguiente> siguientes = null;
    private List<PosicionMatrizProduccion> posMatrizProdList = null;
    private List<PosicionMatrizProduccion> produccionesAmbiguasList = null;

    public GeneradorTablaASP() {
        noTerminales = new ArrayList();
        rightParts = new ArrayList();
        primeros = new ArrayList();
        siguientes = new ArrayList();
        posMatrizProdList = new ArrayList();
    }

    public List leerArchivo() throws IOException {
        List gramaticas = new ArrayList();

        String s;
        String dir = System.getProperty("user.dir");
        String nombreArchivo = dir + "\\src\\GRAMATICA.TXT";
        File file = new File(nombreArchivo);
        FileReader fileReader = new FileReader(file);
        BufferedReader in = new BufferedReader(fileReader);
        while ((s = in.readLine()) != null) {
            gramaticas.add("" + s);
        /*Debug: Lectura del archivo
        System.out.println(s);*/
        }

        return gramaticas;
    }

    public boolean generar() {

        //1- Leer el archivo y retornar una lista de las gramaticas
        List gramaticas = null;
        try {
            gramaticas = new GeneradorTablaASP().leerArchivo();
        } catch (IOException ex) {
            Logger.getLogger(GeneradorTablaASP.class.getName()).log(Level.SEVERE, null, ex);
        }

        //2- Obtener los no terminales
        for (int i = 0; i < gramaticas.size(); i++) {

            String unaGramatica = (String) gramaticas.get(i);
            StringTokenizer tokens = new StringTokenizer(unaGramatica, ">", false);

            String leftPart = tokens.nextToken();
            String rightPart = tokens.nextToken();

            noTerminales.add(leftPart);
            rightParts.add(rightPart);

        /*Debug: leftPart and rightPart
        System.out.println(leftPart + " " + rightPart);*/
        }

        //3- Generar los Primeros de los no terminales
        GeneradorPrimero genPrim = new GeneradorPrimero();
        genPrim.setNoTerminales(noTerminales);
        genPrim.setRightParts(rightParts);
        genPrim.setGramaticas(gramaticas);
        boolean esAmbiguoPrim = genPrim.generar();
        primeros = genPrim.getPrimeros();

        if (esAmbiguoPrim == true) {

            //4- Generar los Siguiente de los no terminales
            GeneradorSiguiente genSgte = new GeneradorSiguiente();
            genSgte.setNoTerminales(genPrim.getNoTerminales());
            genSgte.setRightParts(genPrim.getRightParts());
            genSgte.setGramaticas(gramaticas);
            genSgte.setPrimeros(primeros);
            genSgte.setPosMatrizProdList(genPrim.getPosMatrizProdList());

            boolean esAmbiguoSgte = false;
            try {
                genSgte.generar();
                siguientes = genSgte.getSiguientes();
            } catch (StackOverflowError e) {
                esAmbiguoSgte = true;
            }

            if (esAmbiguoSgte == false) {

                //5- Generar la tabla ASP
                this.setPosMatrizProdList(genSgte.getPosMatrizProdList());

                //6- Validar la tabla
                boolean esAmbiguoTabla = validarAmbiguedad();
                if (esAmbiguoTabla == true) {
                    System.out.println("La gramática es ambigua, verificación hecha en la Tabla ASP.");
                    return false;
                } else {
                    System.out.println("La gramática no es ambigua.");
                    return true;
                }

            } else {
                System.out.println("La gramática es ambigua, verificación hecha en el Siguiente.");
                return false;
            }


        } else {
            System.out.println("La gramática es ambigua, verificación hecha en Primero.");
            return false;
        }

    }

    private boolean validarAmbiguedad() {

        boolean result = false;

        for (int i = 0; i < posMatrizProdList.size() - 1; i++) {
            PosicionMatrizProduccion pos1 = posMatrizProdList.get(i);
            for (int j = i + 1; j < posMatrizProdList.size(); j++) {
                PosicionMatrizProduccion pos2 = posMatrizProdList.get(j);

                if (pos1.getTerminal().compareTo(pos2.getTerminal()) == 0 &&
                        pos1.getNoTerminal().compareTo(pos2.getNoTerminal()) == 0) {
                    result = true;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        boolean estado = new GeneradorTablaASP().generar();

        if (estado == true) {
            System.out.println("Se puede ejecutar el ANALIZADOR SINTACTICO");
        } else {
            System.out.println("NO se puede ejecutar el ANALIZADOR SINTACTICO");
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

    public List<PosicionMatrizProduccion> getPosMatrizProdList() {
        return posMatrizProdList;
    }

    public void setPosMatrizProdList(List<PosicionMatrizProduccion> posMatrizProdList) {
        this.posMatrizProdList = posMatrizProdList;
    }

    public List<Primero> getPrimeros() {
        return primeros;
    }

    public void setPrimeros(List<Primero> primeros) {
        this.primeros = primeros;
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

    public List<PosicionMatrizProduccion> getProduccionesAmbiguasList() {
        return produccionesAmbiguasList;
    }

    public void setProduccionesAmbiguasList(List<PosicionMatrizProduccion> produccionesAmbiguasList) {
        this.produccionesAmbiguasList = produccionesAmbiguasList;
    }
}
