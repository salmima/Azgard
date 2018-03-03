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
        PreparedStatement chercherNRPPS = null;
        ResultSet resultats_bd = null;
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

         //----------- Requête 1: recherche du n_rpps à partir de l'identifiant
        try {
            chercherNRPPS = con.prepareStatement("select * from correspondanceId natural join acces_serveur where identifiant_connexion =?");
            chercherNRPPS.setString(1, identifiant);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = chercherNRPPS.executeQuery();
        } catch (SQLException e) {
            do {
                System.out.println("Requête refusée");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //-----------parcours des données retournées
        //---Variables temporaires
        String n_rpps ="";
        try {
            while (resultats_bd.next()) {
                n_rpps = resultats_bd.getString("n_rpps");          
            }
            resultats_bd.close();
        } catch (SQLException e) {
            do {
                System.out.println("Accès aux résultats refusé");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }
       
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

