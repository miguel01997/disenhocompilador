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

    public GeneradorTablaASP() {
        noTerminales = new ArrayList();
        rightParts = new ArrayList();
        primeros = new ArrayList();
        siguientes = new ArrayList();
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

    public void generar() {

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
        genPrim.generar();
        primeros = genPrim.getPrimeros();

        //4- Generar los Siguiente de los no terminales
        GeneradorSiguiente genSgte = new GeneradorSiguiente();
        genSgte.setNoTerminales(genPrim.getNoTerminales());
        genSgte.setRightParts(genPrim.getRightParts());
        genSgte.setGramaticas(gramaticas);
        genSgte.setPrimeros(primeros);
        genSgte.generar();
        siguientes = genSgte.getSiguientes();
    }


    public static void main(String [] args) {
        new GeneradorTablaASP().generar();
    }
}
