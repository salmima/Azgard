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
     * Ajout d'une prescription d'examen
     */
    public void creerUnePrescriptionExamen(Examen examen, String id, String n_rpps, String exigences_examen) { //id du patient
        //L'interface envoie des informations
        Connection con = null;
        PreparedStatement creerPresExam = null;
        PreparedStatement chercherNRPPS = null;
        ResultSet resultats_bd = null;
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        //-----------Connexion
        //Chargement du pilote
        try {
            Class.forName("com.mysql.jdbc.Driver"); //charge le pilote et crée une instance de cette classe
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Erreur: Class Not Found");
        }

        //-----------Etablissement de la connexion
        try {
            String base1 = "azgardengineering_sih";
            String DBurl1 = "jdbc:mysql://mysql-azgardengineering.alwaysdata.net/" + base1 + "?verifyServerCertificate=false&useSSL=true";
            con = DriverManager.getConnection(DBurl1, "154118", "choco"); //remplacer le mot de passe
        } catch (java.sql.SQLException e) {
            do {
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

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
