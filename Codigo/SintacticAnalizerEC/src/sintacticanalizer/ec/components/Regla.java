/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintacticanalizer.ec.components;

/**
 *
 * @author Id Teknology
 */
public class Regla {

    private long id;
    private String leftPart = "";
    private String rightPart = "";

    public String getLeftPart() {
        return leftPart;
    }

    public void setLeftPart(String leftPart) {
        this.leftPart = leftPart;
    }

    public String getRightPart() {
        return rightPart;
    }

    public void setRightPart(String rightPart) {
        this.rightPart = rightPart;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    

}
