package fc.aloesmanager;

import java.util.Date;
import java.sql.*;

public class PrescriptionMedicamenteuse {

    private Date date;
    private PH ph;
    private String listeMedic;
    private String id;

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
    public void creerUnePrescriptionMedicamenteuse(String listeMedic, String id, String n_rpps) { //id du patient
        //L'interface envoie des informations
        Connection con = null;
        PreparedStatement creerPresMedic = null;
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
}

