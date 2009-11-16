/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintacticanalizer.ec.response;

/**
 *
 * @author Guido Casco
 */
public class RespuestaASPNR {

    private boolean error = false;
    private String mensaje = "";

    

    public RespuestaASPNR(){
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }



}
