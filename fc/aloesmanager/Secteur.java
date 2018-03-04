package fc.aloesmanager;

import java.util.ArrayList;
import java.sql.*;

public class Secteur {

    private String specialite;
    private int etage;
    public String nom;
    private ArrayList<Lit> listeLits;

    /**
     * Constructeur qui initialise les attributs
     */
    public Secteur() {
        this.specialite = null;
        this.etage = 0;
        this.nom = null;
        this.listeLits = new ArrayList();
    }

    /**
     * Constructeur partiel
     */
    public Secteur(String specialite, int etage, String nom) {
        this.specialite = specialite;
        this.etage = etage;
        this.nom = nom;
        this.listeLits = new ArrayList(); //j'ai un problème dans la lecture des résultats de la BD pour créer des secteurs avec leurs listes de lit. Solution: faire une liste de secteurs possibles
    }

     /**
     * Constructeur complet
     */
    public Secteur(String specialite, int etage, String nom, ArrayList<Lit> listeLits) {
        this.specialite = specialite;
        this.etage = etage;
        this.nom = nom;
        this.listeLits = listeLits;
    }

     /**
     * Retourne le nom du secteur
     */
    public String getNom() {
        return nom;
    }
    /**
     * Retourne l'étage du secteur
     */
    public int getEtage(){
        return etage;
    }

     /**
     * Ajout d'un lit dans un secteur
     */
    public void ajouterLit(Lit lit) {
        listeLits.add(lit);
    }

     /**
     * Récupère les informations du secteur lié à un lit
     */
    public void informationsSecteur(Lit lit) { //ne marche que si le lit existe bien évidemment
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheSecteur = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //----------- Requêtes
        //Requête 1: informations du DMA
        try {
            rechercheSecteur = con.prepareStatement("SELECT * FROM Secteur join Lit on (nom_secteur = nom) WHERE num_lit = ?");
            rechercheSecteur.setString(1, lit.getNumeroLit());
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheSecteur.executeQuery();
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
                //Informations du patient
                this.specialite = resultats_bd.getString("specialite");
                this.nom = resultats_bd.getString("nom");
                this.etage = resultats_bd.getInt("etage");
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
        
    }

    //TEST
    public static void main(String[] args) {
        //Test unitaire de la fonction
        Lit lit = new Lit(false, "101P");
        Secteur secteur = new Secteur();
        secteur.informationsSecteur(lit);
        
        //Test d'intégration avec la classe DMA
        DossierMedicoAdministratif dma = new DossierMedicoAdministratif();
        
        Date date = new Date(55, 8, 28);
        System.out.println(dma.localiserUnPatient("Gates", "Bill", date).getNumeroLit()); //ça marche
        secteur.informationsSecteur(dma.localiserUnPatient("Gates", "Bill", date)); //ça marche

    }
}
