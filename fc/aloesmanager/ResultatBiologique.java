package fc.aloesmanager;

import java.sql.*;

public class ResultatBiologique extends Resultat {

    /**
     * Constructeur
     */
    public ResultatBiologique(String CR, Date date, String obs, String id, PH ph){
        this.compteRendu = CR;
        this.date = date;
        this.observations = obs;      
        this.id = id;
        this.ph = ph;
    }

}
