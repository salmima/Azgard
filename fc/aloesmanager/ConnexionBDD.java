/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Thao
 */
public class ConnexionBDD {

    /**
     * Connexion: chargement du pilote et établissement de la connexion
     */
    public static Connection obtenirConnection() {
        Connection con = null;

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

        return con;
    }
}
