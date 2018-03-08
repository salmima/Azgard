package fc.aloesmanager;

import java.sql.*;

public class ResultatHematologie extends Resultat {

    /**
     * Constructeur
     */
    public ResultatHematologie(String CR, Date date, String obs, String id, PH ph){
        this.compteRendu = CR;
        this.date = date;
        this.observations = obs;      
        this.id = id;
        this.ph = ph;
    }

}