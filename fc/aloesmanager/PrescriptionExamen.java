package fc.aloesmanager;

import java.util.ArrayList;
import java.util.Date;
import java.sql.*;

public class PrescriptionExamen {

    private Date date;
    Examen examen;
    private PH ph; //PH demandeur
    private String exigences_examen;
    private String id;
    private boolean done;
    //je sais pas si on remet la signature

     /**
     * Constructeur initialisant
     */
    public PrescriptionExamen() {
        this.date = null;
        this.examen =  null;
        this.ph =  null;
        this.exigences_examen =  null;
        this.id =  null;
        this.done = false;
    }
    
    /**
     * Constructeur
     */
    public PrescriptionExamen(Date date, Examen examen, PH ph, String exigences, String id, boolean done){
        this.date = date;
        this.examen = examen;
        this.ph = ph;
        this.exigences_examen = exigences;
        this.id = id;
        this.done= done;
    }
    
     /**
     * Retourne le PH
     */
    public PH getPH(){
        return this.ph;
    }
    
    
    /**
     * Ajout d'une prescription d'examen
     */
    public void creerUnePrescriptionExamen(Examen examen, String id, String n_rpps, String exigences_examen) { //id du patient, identifiant de la personne connectée
        //L'interface envoie des informations
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement creerPresExam = null;
        ResultSet resultats_bd = null;
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        String n_rpps = PH.returnNRPPS(identifiant);
        
        try {
            String requete = "INSERT INTO Prescription_examen VALUES(? , ? , ?, ?, ?, NULL)";
            creerPresExam = con.prepareStatement(requete);
            creerPresExam.setDate(1, sqlDate);
            creerPresExam.setString(2, examen.getLibelle());
            creerPresExam.setString(3, n_rpps);
            creerPresExam.setString(4, id);
            creerPresExam.setString(5, exigences_examen);
            int nbMaj = creerPresExam.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        PrescriptionExamen pres = new PrescriptionExamen();
//        pres.creerUnePrescriptionExamen(Examen.valueOf("radiologie"), "180000111", "GREGH", "test Prescription"); //ça marche
    }
}
