/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintacticanalizer.ec.utils;

/**
 * Informacion de la columna por la cual esta ordenada la informacion
 * 
 * @author Martin Jara
 */
public class SortInfo {
    private String sortColumn;
    private String sortType;       //Ascendente (asc) o Descendente (desc)

    public SortInfo() {
        this.sortType = "desc";
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        if (this.sortType.equals("asc")) {
            this.sortType = "desc";
        } else {
            this.sortType = "asc";
        }
    }
}
