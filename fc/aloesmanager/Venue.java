package fc.aloesmanager;

import java.sql.Date;
import java.sql.*;

public abstract class Venue {

    protected String numSejour;
    protected Date dateEntree;
    protected Date dateSortie;
    protected String lettreSortie;
    protected PH PHrespo;

    public void creerUneVenue(String IPP, String numSejour, Date dateEntree, PH PHrespo) {
        //L'interface envoie des informations
        //Ma méthode implique que l'interface a cherché le médecin et a retourné un PH (à adapter si ça ne vous convient pas)
        //La localisation du patient est mise à jour par le service clinique, une fois qu'ils on reçu le DMA, si c'est une hospitalisation

        Connection con = null;
        PreparedStatement creerHospi = null;
        ResultSet resultats_bd = null;

        //-----------Connexion
        //Chargement du pilote
        try {
            Class.forName("com.mysql.jdbc.Driver"); //charge le pilote et crée une instance de cette classe
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Erreur: Class Not Found");
        }

        //-----------Etablissement de la connexion
        try {
            String base1 = "SIH";
            String DBurl1 = "jdbc:mysql://localhost:3306/" + base1 + "?verifyServerCertificate=false&useSSL=true";
            con = DriverManager.getConnection(DBurl1, "root", "choco"); //remplacer le mot de passe
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
            String requete = "INSERT INTO VENUE VALUES(? , ? , NULL, NULL)";
            creerHospi = con.prepareStatement(requete);
            creerHospi.setString(1, numSejour);
            creerHospi.setDate(2, dateEntree);

            int nbMaj = creerHospi.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Ajout de la correspondance avec le patient
        try {
            String requete = "INSERT INTO CorrespondanceDMA_hospitalisation VALUES (?,?)";
            creerHospi = con.prepareStatement(requete);
            creerHospi.setString(1, IPP);
            creerHospi.setString(2, numSejour);

            int nbMaj = creerHospi.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Ajout du PH respo
        try {
            String requete = "INSERT INTO CorrespondancePH_Venue VALUES (?,?,?)";
            creerHospi = con.prepareStatement(requete);
            creerHospi.setString(1, IPP);
            creerHospi.setString(2, PHrespo.getNRPPS());
            creerHospi.setString(3, numSejour);

            int nbMaj = creerHospi.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
