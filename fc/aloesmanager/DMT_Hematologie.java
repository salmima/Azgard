package fc.aloesmanager;

import java.util.ArrayList;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DMT_Hematologie extends DMT {

    private ArrayList<ResultatHematologie> liste_resultats;
    private ArrayList<PrescriptionExamen> liste_examens;
    private String observations;
    private String id;

    /**
     * Constructeur initialisant
     */
    public DMT_Hematologie() {
        this.id = null;
        this.observations = null;
        this.liste_resultats = new ArrayList<Resultat>();
        this.liste_examens = new ArrayList<PrescriptionExamen>();
    }

    /**
     * Constructeur complet
     */
    public DMT_Hematologie(String id, String observations, ArrayList<Resultat> liste, ArrayList<PrescriptionExamen> liste_exam) {
        this.id = id;
        this.observations = observations;
        this.liste_resultats = liste;
        this.liste_examens = liste_exam;
    }

    /**
     * Ajout d'un résultat à la liste de résultats
     */
    public void ajouterResultat(ResultatImagerie resultat) {
        this.liste_resultats.add(resultat);
    }


    /**
     * Recherche d'un DMT de Hematologie d'un patient
     */
    //ATTENTION: il faut vérifier dans l'interface que la personne est bien de secteur "hematologie" et que son statut est bien "PH" 
    public void rechercherUnDMHematologie(String IPP, String identifiant) { //identifiant de la personne connectée
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheDMHemato = null;
        PreparedStatement rechercheResultat = null;
        PreparedStatement recherchePrescription = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête
        ResultSet resultats_bd2 = null;
        ResultSet resultats_bd3 = null;

        //----------- Requête 1: recherche du DMT Biologie
        try {
           rechercheDMHemato = con.prepareStatement("select * from DMT_Hematologie where id = ?");
           rechercheDMHemato.setString(1, IPP);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd =rechercheDMHemato.executeQuery();
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
        String r_observations = null;
        String r_id = null;

        try {
            while (resultats_bd.next()) {
                r_observations = resultats_bd.getString("observations");
                r_id = resultats_bd.getString("id");

                //Création partielle d'un DMT de radiologie
                if (r_id != null) {
                    this.id = r_id;
                    this.observations = r_observations;
                }
            }
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

        if (r_id != null) {
            //----------- Requête 2: recherche des résultats d'examen hematologie
            try {
                rechercheResultat = con.prepareStatement("select * from Resultat where id = ? and type_examen = 'hematologie'");
                rechercheResultat.setString(1, r_id);
            } catch (Exception e) {
                System.out.println("Erreur de requête 2");
            }

            //-----------Accès à la base de données
            try {
                resultats_bd2 = rechercheResultat.executeQuery();
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

            //------------------ Ajout des résultats d'examen ------------------
            //-----------parcours des données retournées
            //---Variables temporaires
            String r_compteRendu = "";
            Date r_date = null;
            String r_observations2 = "";
            String r_nrpps = "";
            String r_service_demandeur = "";
            PH r_ph = new PH();
            ResultatHematologie resultat;

            try {
                while (resultats_bd2.next()) {
                    r_observations2 = resultats_bd2.getString("observations");
                    r_compteRendu = resultats_bd2.getString("compte_rendu");
                    r_date = resultats_bd2.getDate("date");
                    r_nrpps = resultats_bd2.getString("n_rpps");
                    r_service_demandeur = resultats_bd2.getString("service_demandeur");
                    
                    try {
                    //On cherche le PH
                    r_ph = r_ph.rechercherUnMedecinRPPS(r_nrpps);
                    if (r_ph != null) {
                        //On crée le résultat
                        resultat = new ResultatHematologie(r_compteRendu, r_date, r_observations, r_id, r_ph, r_service_demandeur);
                        this.ajouterResultat(resultat);
                    }
                } catch (Exception e) {
                    System.out.println("");
                }
                }
                resultats_bd2.close();

                

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

            //----------- Requête 3: on cherche les demandes de prescription
            try {
                recherchePrescription = con.prepareStatement("select * from Prescription_examen where id = ? and examen = 'hematologie' ");
                recherchePrescription.setString(1, r_id);
            } catch (Exception e) {
                System.out.println("Erreur de requête 3");
            }

            //-----------Accès à la base de données
            try {
                resultats_bd3 = recherchePrescription.executeQuery();
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

            //------------------ Ajout des prescriptions d'examen ------------------
            //-----------parcours des données retournées
            //---Variables temporaires
            Date r_date2 = null;
            String r_nrpps2 = null;
            String r_exigences = null;
            boolean r_done = false;
            PH r_ph2 = new PH();
            PrescriptionExamen pres;

            try {
                while (resultats_bd3.next()) {
                    r_date2 = resultats_bd3.getDate("date_pres");
                    r_nrpps2 = resultats_bd3.getString("n_rpps");
                    r_exigences = resultats_bd3.getString("exigences_examen");
                    r_done = resultats_bd3.getBoolean("done");
                    try {
                        //On cherche le PH demandeur
                        r_ph2 = r_ph2.rechercherUnMedecinRPPS(r_nrpps2);

                        if (r_ph2 != null) {
                            pres = new PrescriptionExamen(r_date2, Examen.valueOf("hematologie"), r_ph2, r_exigences, r_id, r_done);
                            this.ajouterPrescription(pres);
                        }
                    } catch (Exception e) {
                        System.out.println("");
                    }
                }

                resultats_bd3.close();
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

            //Ajout de la traçabilité 
            PreparedStatement ajouterTrace = null;
            java.util.Date utilDate2 = new java.util.Date();
            java.sql.Date date_co = new java.sql.Date(utilDate2.getTime());

            try {
                String requete = "INSERT INTO Tracabilite VALUES(? , ? , ?)";
                ajouterTrace = con.prepareStatement(requete);
                ajouterTrace.setDate(3, date_co);
                ajouterTrace.setString(2, identifiant);
                ajouterTrace.setString(1, IPP);

                int nbMaj = ajouterTrace.executeUpdate();
                System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

     /**
     * Ajout d'un résultat d'hematologie
     */
    //Penser à faire dma.ajouterExamen(new Examen.valueOf('hematologie')
    public void ajouterResultat(Date date, PrescriptionExamen pres, String id, String observations, String compteRendu, String identifiant) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement ajouterResultat;
        String n_rpps = PH.returnNRPPS(identifiant);

        //Requête: Ajout du résultat dans le service demandeur
        //On recherche le service demandeur
        PH ph = pres.getPH();
        String service_demandeur = ph.getService().getLibelle();

        try {
            String requete = "INSERT INTO Resultat VALUES(? ,?, ?, ?, ? ,'hematologie',?)";
            ajouterResultat = con.prepareStatement(requete);
            ajouterResultat.setDate(1, date);
            ajouterResultat.setString(2, id);
            ajouterResultat.setString(3, n_rpps);
            ajouterResultat.setString(4, compteRendu);
            ajouterResultat.setString(5, observations);
            ajouterResultat.setString(6, service_demandeur);
            int nbMaj = ajouterResultat.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //On met à jour la prescription      
        try {
            String requete = "UPDATE Prescription_examen set done = 1 where id = ? and date_pres = ? and n_rpps = ? and examen = ? and exigences_examen = ? ";
            ajouterResultat = con.prepareStatement(requete);
            ajouterResultat.setString(1, id);
            ajouterResultat.setDate(2, pres.getDate());
            ajouterResultat.setString(3, pres.getPH().getNRPPS());
            ajouterResultat.setString(4, pres.getLibelleExamen());
            ajouterResultat.setString(5, pres.getExigences());

            int nbMaj = ajouterResultat.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * Création d'un DMT de Biologie d'un patient
     */
    public void creerUnDMHematologie(String id, String observations) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement creerDMRadio;
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        //Requête 1: Ajout d'un DM clinique
        //La non-vérification de l'existence du DM se fait dans la base de données
        try {
            String requete = "INSERT INTO DMT_Hematologie VALUES(? ,?)";
            creerDMRadio = con.prepareStatement(requete);
            creerDMRadio.setString(1, id);
            creerDMRadio.setString(2, observations);
            int nbMaj = creerDMRadio.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //TEST
    public static void main(String[] args) {
        DMT_Hematologie dmt = new DMT_Hematologie();
        Date dateTest = new Date(118, 2, 3);
        PrescriptionExamen pres = new PrescriptionExamen(dateTest, Examen.valueOf("hematologie"), PH.rechercherUnMedecin("house", "gregory"), "pres 2", "180000111", false);
        dmt.rechercherUnDMHematologie("180000111", "JEAG"); //ça marche
//        dmt.creerUnDMHematologie("180000111","hemato - bill gates"); //ça marche
//        dmt.ajouterResultat(dateTest, pres, "180000111", "", "test sang", "JEAG"); //ça marche
    }

}
