package fc.aloesmanager;

import java.sql.*;

public class PH extends PersonnelMedical {

    private boolean chefDeService;

    public PH() {
        this.nom = null;
        this.prenom = null;
        this.nRPPS = null;
        this.ntel = 0;
        this.service = null;
        this.specialite = null;
    }

    public PH(String nrpps, String nom, String prenom, int ntel, Service service, String specialite) {
        this.nom = nom;
        this.prenom = prenom;
        this.nRPPS = nrpps;
        this.ntel = ntel;
        this.service = service;
        this.specialite = specialite;
    }

    public String getNRPPS(){
        return this.nRPPS;
    }
    public void rechercherUnMedecin(String nom, String prenom) {
        PH ph = null;
        Connection con = null;
        PreparedStatement rechercheMedecin = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //-----------Connexion
        //Chargement du pilote
        try {
            Class.forName("com.mysql.jdbc.Driver"); //charge le pilote et crée une instance de cette classe
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Erreur: Class Not Found");
        }

        //-----------Etablissement de la connexion
        try {
            //il faut instancier un objet de la classe Connexion en précisant l'URL de la base
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

        //----------- Requêtes
        //Requête 1: informations du DMA
        try {
            rechercheMedecin = con.prepareStatement("SELECT * FROM PERSONNELMEDICAL WHERE lower(nom) =  ? and lower(prenom) = ? and typePersonnel='PH' ");
            rechercheMedecin.setString(1, nom.toLowerCase());
            rechercheMedecin.setString(2, prenom.toLowerCase());
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheMedecin.executeQuery();
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
        String r_n_rpps = null;
        int r_n_tel = 0;
        String r_service = null;
        Service service = null;
        String r_specialite = null;

        try {
            while (resultats_bd.next()) {
                //Informations du patient
                r_n_rpps = resultats_bd.getString("n_rpps");
                r_n_tel = resultats_bd.getInt("telephone_pro");
                r_service = resultats_bd.getString("service"); 
                r_specialite = resultats_bd.getString("specialite");
            }

            //Fermeture des résultats des requêtes
            resultats_bd.close();

            //Conversion du service type String en type Service
            service = Service.valueOf(r_service);

            //Création du PH
            ph = new PH(r_n_rpps, nom, prenom, r_n_tel, service, r_specialite);

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

    }

    public PH rechercherUnMedecin(String specialite) {
        PH ph = null;
        Connection con = null;
        PreparedStatement rechercheMedecin = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //-----------Connexion
        //Chargement du pilote
        try {
            Class.forName("com.mysql.jdbc.Driver"); //charge le pilote et crée une instance de cette classe
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Erreur: Class Not Found");
        }

        //-----------Etablissement de la connexion
        try {
            //il faut instancier un objet de la classe Connexion en précisant l'URL de la base
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

        //----------- Requêtes
        //Requête 1: informations du DMA
        try {
            rechercheMedecin = con.prepareStatement("SELECT * FROM PERSONNELMEDICAL WHERE specialite = ? and typePersonnel='PH' ");
            rechercheMedecin.setString(1, specialite);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheMedecin.executeQuery();
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
        String r_n_rpps = null;
        String r_nom = null;
        String r_prenom = null;
        int r_n_tel = 0;
        String r_service = null;

        try {
            while (resultats_bd.next()) {
                //Informations du patient
                r_n_rpps = resultats_bd.getString("n_rpps");
                r_nom = resultats_bd.getString("nom");
                r_prenom = resultats_bd.getString("prenom");
                r_n_tel = resultats_bd.getInt("telephone_pro");
                r_service = resultats_bd.getString("service"); 
            }

            //Fermeture des résultats des requêtes
            resultats_bd.close();

            //Conversion du service type String en type Service
            Service service = Service.valueOf(r_service);

            //Création du PH
            ph = new PH(r_n_rpps, r_nom, r_prenom, r_n_tel, service, specialite);

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

        return ph;
    }

    public PH rechercherUnMedecinRPPS(String nrpps) {
        PH ph = null;
        Connection con = null;
        PreparedStatement rechercheMedecin = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //-----------Connexion
        //Chargement du pilote
        try {
            Class.forName("com.mysql.jdbc.Driver"); //charge le pilote et crée une instance de cette classe
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Erreur: Class Not Found");
        }

        //-----------Etablissement de la connexion
        try {
            //il faut instancier un objet de la classe Connexion en précisant l'URL de la base
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

        //----------- Requêtes
        //Requête 1: informations du DMA
        try {
            rechercheMedecin = con.prepareStatement("SELECT * FROM PERSONNELMEDICAL where n_rpps = ? ");
            rechercheMedecin.setString(1, nrpps);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheMedecin.executeQuery();
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
        String r_nom = null;
        String r_prenom = null;
        int r_n_tel = 0;
        String r_service = null;
        String r_specialite = null;

        try {
            while (resultats_bd.next()) {
                //Informations du patient
                r_nom = resultats_bd.getString("nom");
                r_prenom = resultats_bd.getString("prenom");
                r_n_tel = resultats_bd.getInt("telephone_pro");
                r_service = resultats_bd.getString("service"); 
                r_specialite = resultats_bd.getString("specialite");
            }

            //Fermeture des résultats des requêtes
            resultats_bd.close();

            //Conversion du service type String en type Service
            Service service = Service.valueOf(r_service);

            //Création du PH
            ph = new PH(nrpps, r_nom, r_prenom, r_n_tel, service, r_specialite);

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

        return ph;
    }

    //TEST
    public static void main(String[] args) {
        PH ph1 = new PH();
        ph1.rechercherUnMedecin("House", "Gregory");
        ph1.rechercherUnMedecinRPPS("18945686235");

    }
}
