package fc.aloesmanager;

import java.util.Date;
import java.sql.*;

public abstract class Resultat {

    private String compteRendu;
    private Date date;
    private String observations;
    private String id;
    private String nrpps;

    /**
     * Retourne le compte-rendu
     */
    public String getCompteRendu(){
        return this.compteRendu;
    }
    
    /**
     * Retourne les dates
     */
    public Date getDate(){
        return this.date;
    }
    
    /**
     * Retourne les observations
     */
    public String getObservations(){
        return this.observations;
    }
    
    //pour obtenir le type de résultat, tester avec instanceOf ResultatBiologie, ResultatAnapathologie, ResultatImagerie
    
     /**
     * Retourne l'ID du patient
     */
    public String getId(){
        return this.observations;
    }
    
    /**
     * Retourne le numéro RPPS du personnel MT qui a publié le résultat
     */
    public String getNRPPS(){
        return this.nrpps;
    }
    
}
