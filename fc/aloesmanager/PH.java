package fc.aloesmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

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

    /**
     * Renvoie le numéro RPPS
     */
    public String getNRPPS() {
        return this.nRPPS;
    }
      /**
     * Renvoie le nom du PH 
     */
    public String getNom() {
        return this.nom;
    }
     /**
     * Renvoie le service du PH
     */
    public Service getService() {
        return this.service;
    }
    
    /**
     * Renvoie le prénom du PH
     */
    public String getPrenom() {
        return this.prenom;
    }
    
    /**
     * Renvoie la spécialité du PH
     */
    public String getSpecialite() {
        return this.specialite;
    }

    /**
     * Recherche d'un médecin à partir d'un nom et prénom
     */
    public static PH rechercherUnMedecin(String nom, String prenom) {
        PH ph = null;
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheMedecin = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête


        //----------- Requêtes
        //Requête 1: informations du DMA
        try {
            rechercheMedecin = con.prepareStatement("SELECT * FROM personnelMedical WHERE lower(nom) =  ? and lower(prenom) = ? and typePersonnel='PH' ");
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
        return ph;
    }

    /**
     * Recherche d'un médecin à partir d'une certaine spécialité
     */
    public static ArrayList<PH> rechercherUnMedecin(String specialite) {
        ArrayList<PH> ph = new ArrayList();
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheMedecin = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //----------- Requêtes
        try {
            rechercheMedecin = con.prepareStatement("SELECT * FROM personnelMedical WHERE lower(specialite) = ? and typePersonnel='PH' ");
            rechercheMedecin.setString(1, specialite.toLowerCase());
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
                //Informations du médecin
                r_n_rpps = resultats_bd.getString("n_rpps");
                r_nom = resultats_bd.getString("nom");
                r_prenom = resultats_bd.getString("prenom");
                r_n_tel = resultats_bd.getInt("telephone_pro");
                r_service = resultats_bd.getString("service");

                //Conversion du service type String en type Service
                Service service = Service.valueOf(r_service);

                //Création du PH
                ph.add(new PH(r_n_rpps, r_nom, r_prenom, r_n_tel, service, specialite));

            }

            //Fermeture des résultats des requêtes
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

        return ph;
    }

    /**
     * Recherche d'un médecin à partir de son numéro RPPS
     */
    public PH rechercherUnMedecinRPPS(String nrpps) {
        PH ph = null;
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheMedecin = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //----------- Requêtes
        try {
            rechercheMedecin = con.prepareStatement("SELECT * FROM personnelMedical where n_rpps = ? ");
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

    /**
    *Recherche d'un NRPPS à partir d'un identifiant de connexion 
    */
     public static String returnNRPPS(String identifiant){
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement chercherNRPPS = null;
        ResultSet resultats_bd = null;
        String n_rpps="";
        
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
        try {
            while (resultats_bd.next()) {
                n_rpps = resultats_bd.getString("n_rpps");
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
        
        return n_rpps;
    }
    
    
    /**
     * Mise en forme des informations du médecin à afficher dans l'interface
     */
    public Vector<String> afficherMedecin() {
        Vector<String> rowData = new Vector();
        String data = "";
        data += this.nom.substring(0, 1).toUpperCase() + this.nom.substring(1);
        data += "\t" + this.prenom.substring(0, 1).toUpperCase() + this.prenom.substring(1);
        data += "\t" + this.specialite.substring(0, 1).toUpperCase() + this.specialite.substring(1);
        data += "\t" + this.service.getLibelle().substring(0, 1).toUpperCase() + this.service.getLibelle().substring(1);
        rowData.add(data);
        return rowData;
    }

    //TEST
    public static void main(String[] args) {
        PH ph1 = new PH();
        ph1.rechercherUnMedecin("House", "Gregory");
        ph1.rechercherUnMedecinRPPS("pneumologie");
        ph1.rechercherUnMedecinRPPS("18945686235");

    }
}

