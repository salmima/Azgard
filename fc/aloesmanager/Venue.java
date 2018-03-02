package fc.aloesmanager;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDateTime;

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
            String requete = "INSERT INTO correspondanceDMA_hospitalisation VALUES (?,?)";
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

    public static String creerUnNumSejour() {
        //Ajout de l'année et du mois
        String numSejour = Integer.toString(LocalDateTime.now().getYear()).substring(2, 4);
        if (LocalDateTime.now().getMonthValue() < 10) {
            numSejour += "0" + Integer.toString(LocalDateTime.now().getMonthValue());
        } else {
            numSejour += Integer.toString(LocalDateTime.now().getMonthValue());
        }

        //Ajout du compteur
        if (Integer.parseInt(Venue.chercherDerniereVenue().substring(2,4)) != LocalDateTime.now().getMonthValue()){
            numSejour += "00001";
        }
        else {
            String last = Venue.chercherDerniereVenue().substring(4, 9); //on récupère la dernière venue
            String test = Integer.toString(Integer.parseInt(last.substring(1, last.length())) + 1); //on ajoute +1 à la dernière venue
            int compteur = Integer.parseInt(last.substring(1, last.length())) + 1; //on transforme cette valeur en int
            String nombreZerosAAjouter = Integer.toString(5 - test.length()); //on calcule le nombre de 0 à ajouter devant le chiffre ("leading zeros")

            numSejour += String.format("%0" + nombreZerosAAjouter + "d", compteur); //on ajoute le compteur au début du numéro de séjour
        }
        
        return numSejour;
    }

    public static String chercherDerniereVenue() {
        Connection con = null;
        PreparedStatement chercherNumSejour = null;
        String last = "";

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

        //Vérification que ce numéro d'IPP n'existe pas
        try {
            String requete = "select num_sejour from Venue where num_sejour in (select max(num_sejour) from Venue)";
            chercherNumSejour = con.prepareStatement(requete);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultats_bd = null;

        //-----------Accès à la base de données
        try {
            resultats_bd = chercherNumSejour.executeQuery();
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
        try {
            while (resultats_bd.next()) {
                last = resultats_bd.getString("num_sejour");
            }
            //Fermeture des résultats des requête
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

        return last;
    }

    public static void main(String[] args) {
        System.out.println(Venue.creerUnNumSejour()); //ça marche
    }

}
