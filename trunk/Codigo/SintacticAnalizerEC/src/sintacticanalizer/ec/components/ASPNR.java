package sintacticanalizer.ec.components;

import java.util.Vector;

/*
 * Analizador Sintactico Predictivo No Recursivo
 */
/**
 *
 * @author Alida
 */
public class ASPNR {

    private int ip;
    private String input;
    private java.util.Stack stack;
    private String M[][];
    private Vector fila,  col;

    public ASPNR(String[][] matriz, Vector fila, Vector col) {
        stack = new java.util.Stack();
        stack.push(fila.get(0).toString());
        M = matriz;
        this.fila = fila;
        this.col = col;
    }

    /** Esta función realiza el analisis sintactico predictivo no recursivo
     */
    public void analizar(String input) {
        String X;
        StringBuffer P;
        int r, c;
        ip = 0;
        this.input = input;
        System.out.println("ASPNR analizando " + input);
        System.out.println("S" + stack.toString() + ", I[" + input + "], P[]");
        do {
            X = ((String) stack.peek()).trim();
            P = new StringBuffer();
            if (findFila(X) == -1) { // X es terminal
                if (X.equals(input)) {
                    stack.pop();
                    P.append("terminal: ");
                    P.append(input);
                    ip++;
                    System.out.println("Stack\t" + stack.toString() + "\nInput\t" + input +
                            "\nProd\t" + P.toString());
                    break;
                } else if (X.equals(col.get(0).toString()) && isID(input)) {
                    stack.pop();
                    P.append("id: ");
                    P.append(input);
                    ip++;
                } else {
                    System.out.println("ASPNR: unmatched terminal at position " + ip);
                    break;
                }
            } else { // X es no-terminal
                r = findFila(X);
                c = findCol(input);
                if (r != -1 && c != -1) {
                    if (M[r][c] != null) {
                        stack.pop();
                        for (int s = M[r][c].length() - 2; s >= 0; s -= 2) {
                            stack.push(M[r][c].substring(s, s + 2).trim());
                        }
                        P.append(X);
                        P.append("->");
                        P.append(M[r][c]);
                    } else {
                        System.out.println("ASPNR: no production");
                        break;
                    }
                } else {
                    System.out.println("ASPNR: unrecognized char at position " + ip);
                    break;
                }
            }

            System.out.println("Stack\t" + stack.toString() + "\nInput\t" + input +
                    "\nProd\t" + P.toString());
        } while (!stack.empty());

    }

    /** Verifica si el parametro es un no terminal, de ser asi retorna la
     * posicion en el vector de no terminales, caso contrario retorna -1.
     */
    private int findFila(String nonTerminal) {

        return (fila.indexOf(nonTerminal));

    }

    /** Verifica si el parametro es un terminal, de ser asi retorna la
     * posicion en el vector de terminales, caso contrario retorna -1.
     */
    private int findCol(String terminal) {

        return (col.indexOf(terminal));
    }

    private boolean isID(String id) {
        if (id.charAt(0) >= 65 && id.charAt(0) <= 91 ||
                id.charAt(0) >= 97 && id.charAt(0) <= 123) {
            return true;
        }
        return false;
    }
}
