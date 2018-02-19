package fc.aloesmanager;

import java.sql.*;

public class Lit {

    private boolean occupe;
    private String numeroLit;

    public Lit() {
        this.occupe = false;
        this.numeroLit = null;
    }

    public Lit(boolean occupe, String numeroLit) {
        this.occupe = occupe;
        this.numeroLit = numeroLit;
    }

    /**
     * @return the occupe
     */
    public boolean isOccupe() {
        return occupe;
    }

    /**
     * @param occupe the occupe to set
     */
    public void setOccupe(boolean occupe) {
        this.occupe = occupe;
    }

    /**
     * @return the numeroLit
     */
    public String getNumeroLit() {
        return numeroLit;
    }

    /**
     * @param numeroLit the numeroLit to set
     */
    public void setNumeroLit(String numeroLit) {
        this.numeroLit = numeroLit;
    }
}
