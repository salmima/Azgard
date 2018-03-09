package fc.aloesmanager;

import java.sql.*;

public class ResultatHematologie extends Resultat {

    /**
     * Constructeur
     */
    public ResultatHematologie(String CR, Date date, String obs, String id, PH ph, String service_demandeur){
        this.compteRendu = CR;
        this.date = date;
        this.observations = obs;      
        this.id = id;
        this.ph = ph;
       this.service_demandeur = service_demandeur;
    }

}
