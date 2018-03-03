package fc.aloesmanager;

import java.sql.*;

public class ResultatBiologique extends Resultat {

    /**
     * Constructeur
     */
    public ResultatBiologique(String CR, Date date, String obs, String id, String nrpps){
        this.compteRendu = CR;
        this.date = date;
        this.observations = obs;      
        this.id = id;
        this.nrpps = nrpps;
    }

}