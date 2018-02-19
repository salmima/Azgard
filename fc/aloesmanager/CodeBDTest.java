/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Thao
 */
public class CodeBDTest {
    
    public void ConnexionBD() {
        Connection con = null;
        Statement stmt = null; //on peut en créer un seul pour tout le monde
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête
        String requete_selec = "SELECT * FROM client"; //remplacer la requête

        //Connexion
        try {
            //Chargement du pilote
            Class.forName("com.mysql.jdbc.Driver"); //charge le pilote et crée une instance de cette classe

            //Etablissement de la connexion
            //il faut instancier un objet de la classe Connexion en précisant l'URL de la base
            String DBurl = "jdbc:odbc:nomDB"; //changer le nom de la bse
            con = DriverManager.getConnection(DBurl, "root", "motdepasse"); //remplacer le mot de passe

            //Accès à la base de données
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Erreur: Class Not Found");
        } catch (java.sql.SQLException e) {
            do {
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //Requête de sélection
        try {
            stmt = con.createStatement();
            resultats_bd = stmt.executeQuery(requete_selec);
        } catch (SQLException e) {
            do {
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //Requête de mise à jour
        String requete_update = "INSERT INTO client VALUES (3,'client 3','prenom 3')"; //remplacer la requête
        try {
            stmt = con.createStatement();
            int nbMaj = stmt.executeUpdate(requete_update);
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //lors de la création d'une table avec cette méthode, la méthode retourne 0 

        //parcours des données retournées
        try {
            ResultSetMetaData rsmd = resultats_bd.getMetaData();
            int nbCols = rsmd.getColumnCount();
            while (resultats_bd.next()) {
                for (int i = 1; i <= nbCols; i++) {
                    System.out.print(resultats_bd.getString(i) + " "); //méthode getString(nom colonne ou numéro colonne DU RESULT SET)
                }
                System.out.println();
            }
            resultats_bd.close();
        } catch (SQLException e) {
            do {
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

       
        try {
            //Prepared statement: permet de définir une requête générique où on change les paramètres
            PreparedStatement recherchePersonne = con.prepareStatement("SELECT * FROM personnes WHERE nom_personne = ?"); //exemple. On met "?" pour les paramètres qui changent.
            recherchePersonne.setString(1, "nom3"); //exemple
            resultats_bd = recherchePersonne.executeQuery();
            //Statement qui précise qu'on ne peut pas changer la base de données
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //questions: installer pilote, trouver URL de la base de données
}
