package fc.aloesmanager;

import java.util.Date;
import java.sql.*;

public class PrescriptionMedicamenteuse {

    private Date date;
    private PH ph; //PH demandeur
    private String listeMedic;
    private String id;

     /**
     * Constructeur initialisant
     */
    public PrescriptionMedicamenteuse() {
        this.date = null;
        this.ph = null;
        this.listeMedic = null;
        this.id = null;
    }
    
    
     /**
     * Constructeur
     */
    public PrescriptionMedicamenteuse(Date date, PH ph, String listeMedic, String id) {
        this.date = date;
        this.ph = ph;
        this.listeMedic = listeMedic;
        this.id = id;
    }
    
     /**
     * Ajout d'une prescription médicamenteuse
     */
    public void creerUnePrescriptionMedicamenteuse(String listeMedic, String id, String identifiant) { //id du patient, identifiant de la personne connectée
        //L'interface envoie des informations
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement creerPresMedic = null;
        ResultSet resultats_bd = null;
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        String n_rpps = PH.returnNRPPS(identifiant);

        try {
            String requete = "INSERT INTO Prescription_medic VALUES(? , ? , ?, ?)";
            creerPresMedic = con.prepareStatement(requete);
            creerPresMedic.setDate(1, sqlDate);
            creerPresMedic.setString(2, listeMedic);
            creerPresMedic.setString(3, n_rpps);
            creerPresMedic.setString(4, id);
            int nbMaj = creerPresMedic.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
       //TEST
        public static void main(String[] args) {
        PrescriptionMedicamenteuse pres = new PrescriptionMedicamenteuse();
//        pres.creerUnePrescriptionMedicamenteuse("liste de médic test","180000111", "GREGH"); //ça marche
    }
}

