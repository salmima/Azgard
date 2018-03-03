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

}
