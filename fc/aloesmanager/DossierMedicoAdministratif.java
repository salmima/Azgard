package fc.aloesmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Random;

public class DossierMedicoAdministratif {

    private String IPP;
    private String nom;
    private String prenom;
    private String idSIR;
    private String sexe;
    private Date dateNaissance;
    private String nom_proche;
    private String n_tel_proche;
    private String n_tel;
    private String adresse;
    private String groupeSanguin;
    private MedecinTraitant medTraitant;
    private ArrayList<Venue> listeVenue;
    private ArrayList<Examen> listeExamen;

    /**
     * Constructeur initialisant
     */
    public DossierMedicoAdministratif() {
        this.IPP = null;
        this.nom = null;
        this.prenom = null;
        this.n_tel = null;
        this.sexe = null;
        this.dateNaissance = null;
        this.adresse = null;
        this.n_tel_proche = null;
        this.nom_proche = null;
        this.groupeSanguin = null;
        this.listeVenue = new ArrayList();
        this.listeExamen = new ArrayList<Examen>();
    }

    /**
     * Constructeur complet
     */
    public DossierMedicoAdministratif(String IPP, String nom, String prenom, String n_tel, String sexe, Date dateNaissance, String adresse, String groupeSanguin, MedecinTraitant medTt, String n_tel_proche, String nom_proche) {
        this.IPP = IPP;
        this.nom = nom;
        this.prenom = prenom;
        this.n_tel = n_tel;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.groupeSanguin = groupeSanguin;
        this.n_tel_proche = n_tel_proche;
        this.nom_proche = nom_proche;
        this.medTraitant = medTt;
        this.listeVenue = new ArrayList();
        this.listeExamen = new ArrayList<Examen>();
    }

    /**
     * Ajout d'une venue
     */
    public void ajouterUneVenue(Venue venue) {
        listeVenue.add(venue);
    }

    /**
     * Retourne l'IPP
     */
    public String getIPP() {
        return this.IPP;
    }
    /**
     * Retourne le nom
     */
     public String getNom() {
        return this.nom;
    }
    /**
     * Retourne le prénom
     */
     public String getPrenom() {
        return this.prenom;
    }
     /**
     * Retourne le médecin traitant
     */
    public MedecinTraitant getMedecinTraitant() {
        return this.medTraitant;
    }
    
    /**
     * Retourne la nature de l'examen
     */
    public void ajouterExamen(Examen examen) {
        this.listeExamen.add(examen);
    }

    /**
     * Retourne la date de naissance
     */
    public Date getDateN() {
        return this.dateNaissance;
    }
    /**
     * Retourne le sexe du patient
     */
    public String getSexe() {
        return this.sexe;
    }
    
    /**
     * Retourne le n° de tél du patient
     */
    public String getTel() {
        return this.n_tel;
    }
    
    /**
     * Retourne l'adresse
     */
    public String getAdresse() {
        return this.adresse;
    }

    /**
     * Retourne le nom et prénom du proche
     */
    public String getNomProche() {
        return this.nom_proche;
    }
    
    /**
     * Retourne le téléphone du proche
     */
    public String getTelProche() {
        return this.n_tel_proche;
    }
    
    /**
     * Retourne la liste des venues
     */
    public ArrayList<Venue> getListeVenue() {
        return this.listeVenue;
    }
    
    /**
     * Retourne l'id SIR
     */
    public String getIdSIR() {
        return this.idSIR;
    }
    
    /**
     * Recherche d'un DMA à partir d'un IPP
     */
    public void rechercherUnDMA(String ipp) { 
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheIPP = null;
        PreparedStatement rechercheIDSIR = null;
        PreparedStatement rechercheMedTt = null;
        PreparedStatement rechercheVenue = null;
        PreparedStatement rechercheExamen = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête
        ResultSet resultats_bd2 = null;
        ResultSet resultats_bd3 = null;
        ResultSet resultats_bd4 = null;
        ResultSet resultats_bd5 = null;

        //----------- Requêtes
        //Requête 1: informations du DMA
        try {
            rechercheIPP = con.prepareStatement("SELECT * FROM DMA WHERE deces = 0 and IPP =  ?");
            rechercheIPP.setString(1, ipp);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //Requête 2: récupérer l'idSIR s'il existe
        try {
            rechercheIDSIR = con.prepareStatement("SELECT idSIR FROM CorrespondanceDMA_SIR where IPP =  ?");
            rechercheIDSIR.setString(1, ipp);
        } catch (Exception e) {
            System.out.println("Erreur de requête 2");
        }

        //Requête 3: récupérer le médecin traitant s'il existe
        try {
            rechercheMedTt = con.prepareStatement("SELECT * FROM CorrespondanceMedecinExterne natural join MedecinExterne where IPP =  ?"); //à débugger
            rechercheMedTt.setString(1, ipp);
        } catch (Exception e) {
            System.out.println("Erreur de requête 3");
        }

        //Requête 4: récupérer les venues
        try {
            rechercheVenue = con.prepareStatement("SELECT * FROM (Venue natural join CorrespondancePH_Venue natural join LocalisationPatient) where id =  ?"); //à débugger
            rechercheVenue.setString(1, ipp);
        } catch (Exception e) {
            System.out.println("Erreur de requête 4");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheIPP.executeQuery();
            resultats_bd2 = rechercheIDSIR.executeQuery();
            resultats_bd3 = rechercheMedTt.executeQuery();
            resultats_bd4 = rechercheVenue.executeQuery();
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
        MedecinTraitant r_medTraitant = null;
        String r_medAdresse = null;
        String r_medNom = null;
        String r_medPrenom = null;
        String r_medNtel = null;
        int r_medCodePostal = 0;

        try {
            while (resultats_bd.next()) {
                //Informations du patient
                this.IPP = resultats_bd.getString("IPP");
                this.nom = resultats_bd.getString("nom");
                this.prenom = resultats_bd.getString("prenom");
                this.sexe = resultats_bd.getString("sexe");
                this.dateNaissance = resultats_bd.getDate("dateNaissance");
                this.nom_proche = resultats_bd.getString("nom_proche");
                this.n_tel_proche = resultats_bd.getString("tel_proche");
                this.n_tel = resultats_bd.getString("telephone_personnel");
                this.adresse = resultats_bd.getString("adresse");
                this.groupeSanguin = resultats_bd.getString("groupe_sanguin");
            }
            while (resultats_bd2.next()) {
                this.idSIR = resultats_bd2.getString("idSIR");
            }
            while (resultats_bd3.next()) {
                //Informations du médecin
                r_medAdresse = resultats_bd3.getString("adresse");
                r_medNom = resultats_bd3.getString("nom");
                r_medPrenom = resultats_bd3.getString("prenom");
                r_medNtel = resultats_bd3.getString("telephone_pro");
                r_medCodePostal = resultats_bd3.getInt("codePostal");
            }

            //Fermeture des résultats des requêtes
            resultats_bd.close();
            resultats_bd2.close();
            resultats_bd3.close();

            //Création de l'instance du médecin traitant
            if (r_medNom != null && r_medPrenom != null && r_medCodePostal != 0 && r_medNtel != null && r_medAdresse != null) {
                this.medTraitant = new MedecinTraitant(r_medAdresse, r_medNom, r_medPrenom, r_medNtel, r_medCodePostal);
            } else {
                this.medTraitant = null;
            }

            while (resultats_bd4.next()) {
                //Liste des venues
                String r_numSejour;
                Date r_dateEntree;
                Date r_dateSortie;
                String r_lettreSortie;
                String nrpps;
                PH r_PHrespo;
                String r_num_lit;

                //On récupère la première venue
                r_numSejour = resultats_bd4.getString("num_sejour");
                r_dateEntree = resultats_bd4.getDate("date_entree");
                r_dateSortie = resultats_bd4.getDate("date_sortie");
                r_lettreSortie = resultats_bd4.getString("ref_doc_lettre_sortie");
                nrpps = resultats_bd4.getString("n_rpps");
                r_num_lit = resultats_bd4.getString("num_lit");

                //On cherche le PH
                r_PHrespo = new PH();
                r_PHrespo = r_PHrespo.rechercherUnMedecinRPPS(nrpps);

                //On crée le lit 
                Lit lit = new Lit(true, r_num_lit);

                //On crée une venue
                if (r_num_lit != null) {
                    Hospitalisation h1 = new Hospitalisation(r_numSejour, r_dateEntree, r_dateSortie, r_lettreSortie, r_PHrespo, lit);
                    this.ajouterUneVenue(h1);
                } else {
                    Consultation c1 = new Consultation(r_numSejour, r_dateEntree, r_dateSortie, r_lettreSortie, r_PHrespo);
                    this.ajouterUneVenue(c1);
                }
            }

            resultats_bd4.close();

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
        
         //----------- Requête 5: recherche des examens 
        try {
            rechercheExamen = con.prepareStatement("select * from Resultat where id = ?");
            rechercheExamen.setString(1, IPP);
        } catch (Exception e) {
            System.out.println("Erreur de requête 2");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd5 = rechercheExamen.executeQuery();
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

        //------------------ Ajout de la nature des examens ------------------
        //-----------parcours des données retournées
        //---Variables temporaires
        String r_typeExamen = "";

        try {
            while (resultats_bd5.next()) {
                r_typeExamen = resultats_bd5.getString("type_examen");
            }
            resultats_bd5.close();

            try {
                //On convertit r_examen pour avoir une instance d'Examen
                Examen examen = Examen.valueOf(r_typeExamen);

                if (examen != null) {
                    //On l'ajoute à la liste
                    this.ajouterExamen(examen);
                }
            } catch (Exception e) {
                System.out.println("");
            }

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

     /**
     * Recherche d'un DMA en fonction du nom, prénom et date de naissance du patient
     */
    public void rechercherUnDMA(String nom, String prenom, Date dateN) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheIPP = null;
        PreparedStatement rechercheIDSIR = null;
        PreparedStatement rechercheMedTt = null;
        PreparedStatement rechercheVenue = null;
        PreparedStatement rechercheExamen = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête
        ResultSet resultats_bd2 = null;
        ResultSet resultats_bd3 = null;
        ResultSet resultats_bd4 = null;
        ResultSet resultats_bd5 = null;

        //----------- Requêtes
        //Requête 1: informations du DMA
        try {
            rechercheIPP = con.prepareStatement("SELECT * FROM DMA WHERE deces = 0 and lower(nom) =  ? and lower(prenom) = ? and dateNaissance = ?");
            rechercheIPP.setString(1, nom.toLowerCase());
            rechercheIPP.setString(2, prenom.toLowerCase());
            rechercheIPP.setDate(3, dateN);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données: requête 1
        try {
            resultats_bd = rechercheIPP.executeQuery();
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

        //-----------parcours des données retournées: requête 1
        //---Variables temporaires 
            try {
                while (resultats_bd.next()) {
                    //Informations du patient
                    this.IPP = resultats_bd.getString("IPP");
                    this.nom = resultats_bd.getString("nom");
                    this.prenom = resultats_bd.getString("prenom");
                    this.sexe = resultats_bd.getString("sexe");
                    this.dateNaissance = resultats_bd.getDate("dateNaissance");
                    this.nom_proche = resultats_bd.getString("nom_proche");
                    this.n_tel_proche = resultats_bd.getString("tel_proche");
                    this.n_tel = resultats_bd.getString("telephone_personnel");
                    this.adresse = resultats_bd.getString("adresse");
                    this.groupeSanguin = resultats_bd.getString("groupe_sanguin");
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

            if (this.IPP != null) {
            //Requête 2: récupérer l'idSIR s'il existe
            try {
                rechercheIDSIR = con.prepareStatement("SELECT idSIR FROM CorrespondanceDMA_SIR where IPP =  ?");
                rechercheIDSIR.setString(1, this.IPP);
            } catch (Exception e) {
                System.out.println("Erreur de requête 2");
            }

            //Requête 3: récupérer le médecin traitant s'il existe
            try {
                rechercheMedTt = con.prepareStatement("SELECT * FROM CorrespondanceMedecinExterne natural join MedecinExterne where IPP =  ?"); //à débugger
                rechercheMedTt.setString(1, this.IPP);
            } catch (Exception e) {
                System.out.println("Erreur de requête 3");
            }

            //Requête 4: récupérer les venues
            try {
                rechercheVenue = con.prepareStatement("SELECT * FROM (Venue natural join CorrespondancePH_Venue natural join LocalisationPatient) where id =  ?"); //à débugger
                rechercheVenue.setString(1, this.IPP);
            } catch (Exception e) {
                System.out.println("Erreur de requête 4");
            }

            //-----------Accès à la base de données
            try {
                resultats_bd2 = rechercheIDSIR.executeQuery();
                resultats_bd3 = rechercheMedTt.executeQuery();
                resultats_bd4 = rechercheVenue.executeQuery();
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
            String r_medAdresse = null;
            String r_medNom = null;
            String r_medPrenom = null;
            String r_medNtel = null;
            int r_medCodePostal = 0;

            try {
                while (resultats_bd2.next()) {
                    this.idSIR = resultats_bd2.getString("idSIR");
                }
                while (resultats_bd3.next()) {
                    //Informations du médecin
                    r_medAdresse = resultats_bd3.getString("adresse");
                    r_medNom = resultats_bd3.getString("nom");
                    r_medPrenom = resultats_bd3.getString("prenom");
                    r_medNtel = resultats_bd3.getString("telephone_pro");
                    r_medCodePostal = resultats_bd3.getInt("codePostal");
                }

                //Fermeture des résultats des requête
                resultats_bd2.close();
                resultats_bd3.close();

                //Création de l'instance du médecin traitant
                if (r_medNom != null && r_medPrenom != null && r_medCodePostal != 0 && r_medNtel != null && r_medAdresse != null) {
                    this.medTraitant = new MedecinTraitant(r_medAdresse, r_medNom, r_medPrenom, r_medNtel, r_medCodePostal);
                } else {
                    this.medTraitant = null;
                }

                while (resultats_bd4.next()) {
                    //Liste des venues
                    String r_numSejour;
                    Date r_dateEntree;
                    Date r_dateSortie;
                    String r_lettreSortie;
                    String nrpps;
                    PH r_PHrespo;
                    String r_num_lit;

                    //On récupère la première venue
                    r_numSejour = resultats_bd4.getString("num_sejour");
                    r_dateEntree = resultats_bd4.getDate("date_entree");
                    r_dateSortie = resultats_bd4.getDate("date_sortie");
                    r_lettreSortie = resultats_bd4.getString("ref_doc_lettre_sortie");
                    nrpps = resultats_bd4.getString("n_rpps");
                    r_num_lit = resultats_bd4.getString("num_lit");

                    //On cherche le PH
                    r_PHrespo = new PH();
                    r_PHrespo = r_PHrespo.rechercherUnMedecinRPPS(nrpps);

                    //On crée le lit 
                    Lit lit = new Lit(true, r_num_lit);

                    //On crée une venue
                    if (r_num_lit != null) {
                        Hospitalisation h1 = new Hospitalisation(r_numSejour, r_dateEntree, r_dateSortie, r_lettreSortie, r_PHrespo, lit);
                        this.ajouterUneVenue(h1);
                    } else {
                        Consultation c1 = new Consultation(r_numSejour, r_dateEntree, r_dateSortie, r_lettreSortie, r_PHrespo);
                        this.ajouterUneVenue(c1);
                    }
                }

                //On ferme les résultats
                resultats_bd4.close();

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
            
        //----------- Requête 5: recherche des examens 
        try {
            rechercheExamen = con.prepareStatement("select * from Resultat where id = ?");
            rechercheExamen.setString(1, IPP);
        } catch (Exception e) {
            System.out.println("Erreur de requête 2");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd5 = rechercheExamen.executeQuery();
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

        //------------------ Ajout de la nature des examens ------------------
        //-----------parcours des données retournées
        //---Variables temporaires
        String r_typeExamen = "";

        try {
            while (resultats_bd5.next()) {
                r_typeExamen = resultats_bd5.getString("type_examen");
            }
            resultats_bd5.close();

            try {
                //On convertit r_examen pour avoir une instance d'Examen
                Examen examen = Examen.valueOf(r_typeExamen);

                if (examen != null) {
                    //On l'ajoute à la liste
                    this.ajouterExamen(examen);
                }
            } catch (Exception e) {
                System.out.println("");
            }

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

    }

    /**
    * Cherche le lit du patient. 
    Méthode à combiner avec la méthode InformationsSecteur dans la classe Secteur pour avoir toutes les informations
    */
    public Lit localiserUnPatient(String nom, String prenom, Date dateN) {
        this.rechercherUnDMA(nom, prenom, dateN);

        //On récupère toutes les hospitalisations
        ArrayList<Hospitalisation> listeHospi = new ArrayList();

        for (int i = 0; i < this.listeVenue.size(); i++) {
            if (this.listeVenue.get(i) instanceof Hospitalisation) {
                Hospitalisation h = (Hospitalisation) this.listeVenue.get(i);
                listeHospi.add(h);
            }
        }

        //On cherche l'hospitalisation la plus récente, qui est en cours
        Lit lit = null;

        int j = 0;

        while (j < listeHospi.size() && listeHospi.get(j).getDateSortie() != null) {
            System.out.println(listeHospi.get(j).getDateSortie());
            j++;
        }

        if (j < listeHospi.size()) {
            lit = listeHospi.get(j).getLocalisation();
        }

        return lit;
    }
    /**
     * Création d'un DMA
     */
    public static void creerUnDMA(String IPP, String nom, String prenom, String sexe, Date dateN, String nom_proche, String n_tel_proche, String n_tel, String adresse, String groupeSanguin, MedecinTraitant medTt) {
        //L'interface envoie des informations
        //Création un peu particulière: la création du DMA crée aussi automatiquement la première venue
        //Voir la méthode créerUneVenue, à associer avec celle-ci lors de la création du DMA
        //Création de l'une ou l'autre instance en fonction du type spécifié dans l'interface par l'utilisateur

        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement creerDMA = null;
        PreparedStatement creerMTT = null;

        //Ajout d'un DMA
        try {
            String requete = "INSERT INTO DMA VALUES(? , ? , ? , ? , ? , ? , ? , ? , ? , 0 , ?)";
            creerDMA = con.prepareStatement(requete);
            creerDMA.setString(1, IPP);
            creerDMA.setString(2, nom);
            creerDMA.setString(3, prenom);
            creerDMA.setString(4, n_tel);
            creerDMA.setString(5, sexe);
            creerDMA.setDate(6, dateN);
            creerDMA.setString(7, nom_proche);
            creerDMA.setString(8, n_tel_proche);
            creerDMA.setString(9, adresse);
            creerDMA.setString(10, groupeSanguin);

            int nbMaj = creerDMA.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //lors de la création d'une table avec cette méthode, la méthode retourne 0 

        //Ajout d'un médecin traitant
        //Requête 3: récupérer le médecin traitant s'il existe
        ResultSet resultats_bd = null;
        MedecinTraitant r_medTraitant = null;

       //si le médecin traitant passé en paramètres n'est pas null
        //on le cherche dans la base de données
                try {
        if (medTt.getNom() != null && medTt.getPrenom() != null && medTt.getCodePostal() != 0) {
            try {
                creerMTT = con.prepareStatement("SELECT * FROM MedecinExterne where lower(nom) = ? and lower(prenom) = ? and codePostal = ?");
                creerMTT.setString(1, medTt.getNom().toLowerCase());
                creerMTT.setString(2, medTt.getPrenom().toLowerCase());
                creerMTT.setInt(3, medTt.getCodePostal());

            } catch (Exception e) {
                System.out.println("Erreur de requête");
            }

            //-----------Accès à la base de données
            try {
                resultats_bd = creerMTT.executeQuery();
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
            String r_medNom = null;
            String r_medPrenom = null;
            int r_medCodePostal = 0;

            try {
                while (resultats_bd.next()) {
                    //Informations du médecin trouvé dans la base de données
                    //censé être unique
                    r_medNom = resultats_bd.getString("nom");
                    r_medPrenom = resultats_bd.getString("prenom");
                    r_medCodePostal = resultats_bd.getInt("codePostal");
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

            //Création de l'instance du médecin traitant
            if (r_medNom != null && r_medPrenom != null && r_medCodePostal != 0) { //le médecin a été trouvé dans la base de données
                //On fait rien au niveau de la base de données
                //On utilise directement le médecin passé en paramètre de la méthode
            } else {
                // le médecin n'existe pas dans la base de données mais n'est pas null
                //On utilise directement l'instance de médecin traitant passé en paramètre de la méthode
                //On ajoute ce médecin traitant non présent dans la base de données
                try {
                    String requete = "INSERT INTO MedecinExterne VALUES (?,?,?,?,?)";
                    creerMTT = con.prepareStatement(requete);
                    creerMTT.setString(1, medTt.getNom());
                    creerMTT.setString(2, medTt.getPrenom());
                    creerMTT.setString(3, medTt.getNtel());
                    creerMTT.setString(4, medTt.getAdresse());
                    creerMTT.setInt(5, medTt.getCodePostal());

                    int nbMaj = creerMTT.executeUpdate();
                    System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //Ajout de la correspondance avec le médecin traitant
            try {
                String requete = "INSERT INTO CorrespondanceMedecinExterne VALUES (?,?,?,?)";
                creerMTT = con.prepareStatement(requete);
                creerMTT.setString(1, IPP);
                creerMTT.setString(2, medTt.getNom());
                creerMTT.setString(3, medTt.getPrenom());;
                creerMTT.setInt(4, medTt.getCodePostal());

                int nbMaj = creerMTT.executeUpdate();
                System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        } catch (Exception e) {
            System.out.println("Le médecin n'existe pas");
        }


        //Si le médecin passé en paramètre de la méthode est null
        //On fait rien
        }

        //Ajout de la correspondance avec le médecin traitant
        try {
            String requete = "INSERT INTO CorrespondanceMedecinExterne VALUES (?,?,?,?)";
            creerMTT = con.prepareStatement(requete);
            creerMTT.setString(1, IPP);
            creerMTT.setString(2, medTt.getNom());
            creerMTT.setString(3, medTt.getPrenom());;
            creerMTT.setInt(4, medTt.getCodePostal());

            int nbMaj = creerMTT.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     /**
    * Mise à jour de la date de sortie d'une venue. Le test de la validité de la date (avant la date d'entrée) est effectué dans la base de données
    */
    public void mettreAJourDateSortie(String numSejour, Date dateSortie){
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement majDateSortie = null;
        
        try {
            String requete = "UPDATE Venue SET date_sortie = ? WHERE num_sejour = ?)";
            majDateSortie= con.prepareStatement(requete);
            majDateSortie.setDate(1, dateSortie);
            majDateSortie.setString(2, numSejour);

            int nbMaj = majDateSortie.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
   
     /**
     * Archiver un dossier
     */
    public void archiver(String IPP) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement majDateSortie = null;

        try {
            String requete = "UPDATE DMA SET deces = 1 WHERE IPP = ?";
            majDateSortie = con.prepareStatement(requete);
            majDateSortie.setString(1, IPP);

            int nbMaj = majDateSortie.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajouter le téléphone personnel
     */
    public void ajouterTel(String tel, String IPP) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement majTel = null;

        try {
            String requete = "UPDATE DMA SET telephone_personnel = ? WHERE IPP = ?";
            majTel = con.prepareStatement(requete);
            majTel.setString(2, IPP);
            majTel.setString(1, tel);

            int nbMaj = majTel.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Ajouter le nom d'un proche
     */
    public void ajouterProche(String nom_proche, String IPP) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement maj = null;

        try {
            String requete = "UPDATE DMA SET nom_proche = ? WHERE IPP = ?";
            maj = con.prepareStatement(requete);
            maj.setString(2, IPP);
            maj.setString(1, nom_proche);

            int nbMaj = maj.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Ajouter le téléphone d'un proche
     */
    public void ajouterTelProche(String tel_proche, String IPP) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement maj = null;

        try {
            String requete = "UPDATE DMA SET tel_proche = ? WHERE IPP = ?";
            maj = con.prepareStatement(requete);
            maj.setString(2, IPP);
            maj.setString(1, tel_proche);

            int nbMaj = maj.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Ajouter l'adresse
     */
    public void ajouterAdresse(String ad, String IPP) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement maj = null;

        try {
            String requete = "UPDATE DMA SET adresse = ? WHERE IPP = ?";
            maj = con.prepareStatement(requete);
            maj.setString(2, IPP);
            maj.setString(1, ad);

            int nbMaj = maj.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
     /**
     * Générer un IPP
     */
    //Il y a 2 méthodes pour générer un IPP: genererUnIPPe() génère un IPP à tester
    //la méthode testerExistenceIPP(...) qui teste l'existence de cet IPP provisoire
    public static String genererUnIPP() {
        //Ajout de l'année
        String IPP = Integer.toString(LocalDateTime.now().getYear()).substring(2, 4);

        //Ajout d'un nombre au hasard à 7 chiffres
        int num = (int) Math.round(Math.random() * 9999999);
        IPP += num;

        while (testerExistenceIPP(IPP) == true) {
            IPP = Integer.toString(LocalDateTime.now().getYear()).substring(2, 4);
            num = (int) Math.round(Math.random() * 9999999);
            IPP += num;
        }

        return IPP;
    }

     /**
     * Test de l'existence d'un IPP
     */
    public static boolean testerExistenceIPP(String IPP) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement chercherIPP = null;
        boolean existe = false;

        //Vérification que ce numéro d'IPP n'existe pas
        try {
            String requete = "select IPP from DMA where IPP = ?";
            chercherIPP = con.prepareStatement(requete);
            chercherIPP.setString(1, IPP);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultats_bd = null;

        //-----------Accès à la base de données
        try {
            resultats_bd = chercherIPP.executeQuery();
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
                if (resultats_bd.getString("IPP") != null) {
                    existe = true;
                }
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
        return existe;
    }

//TEST
    public static void main(String[] args) {
        DossierMedicoAdministratif dma = new DossierMedicoAdministratif();
        Date date1 = new Date(55, 8, 28);
        Date date = new Date(56, 9, 28);
        Date dateTest = new Date(100, 06, 22);

        dma.rechercherUnDMA("180000111"); //OK
        dma.rechercherUnDMA("Gates", "Bill", date1);
//        System.out.println(dma.localiserUnPatient("Gates", "Bill", date1).getNumeroLit()); //trouve un lit: normal car hospitalisation en cours
//        dma.localiserUnPatient("Jobs", "Steve", date); //ne trouve pas de lit: normal car pas d'hospitalisation en cours
//        MedecinTraitant medTest = dma.rechercherUnDMA("180000111").getMedecinTraitant();
//        dma.creerUnDMA(dma.genererUnIPP(), "Test", "Raoul", "M", dateTest, "Test Christine", "0666996699", "0688991122", "32 adresse de test", "B+", medTest);
//        System.out.println(dma.genererUnIPP()); //ça marche
//        System.out.println(dma.testerExistenceIPP("180000111")); //OK
//        System.out.println(dma.testerExistenceIPP("180000131")); //OK: ça marche pas: normal
//        System.out.println(DossierMedicoAdministratif.regulariserDossierTemporaire("999000001")); //ça marche
        
        //Test: ajout d'un médecin traitant -- ça marche
        MedecinTraitant medTest2 = new MedecinTraitant("12 rue alfred", "test", "medThao", null, 38400);
        //dma.creerUnDMA(dma.genererUnIPP(), "Test", "Thao", "F", dateTest, "Test Christine", "0666996699", "0688991122", "32 adresse de test", "B+", null);
        dma.rechercherUnDMA("Test", "Thao", dateTest);
        dma.ajouterMedTraitant(medTest2, dma.getIPP());
    }
}
