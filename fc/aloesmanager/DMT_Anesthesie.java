package fc.aloesmanager;

import java.util.ArrayList;
import java.sql.*;

public class DMT_Anesthesie extends DMTech {

    private ArrayList<PrescriptionMedicamenteuse> liste_presMedic;

    /**
     * Constructeur initialisant
     */
    public DMT_Anesthesie() {
        this.id = null;
        this.observations = null;
        this.liste_resultats = new ArrayList<Resultat>();
        this.liste_examens = new ArrayList<PrescriptionExamen>();
        this.liste_presMedic = new ArrayList<PrescriptionMedicamenteuse>();
    }

    /**
     * Constructeur complet
     */
    public DMT_Anesthesie(String id, String observations, ArrayList<Resultat> liste, ArrayList<PrescriptionExamen> liste_exam, ArrayList<PrescriptionMedicamenteuse> liste_medic) {
        this.id = id;
        this.observations = observations;
        this.liste_resultats = liste;
        this.liste_examens = liste_exam;
        this.liste_presMedic = liste_medic;
    }

    /**
     * Ajout d'un résultat à la liste de résultats
     */
    public void ajouterResultat(ResultatAnesthesie resultat) {
        this.liste_resultats.add(resultat);
    }

    /**
     * Ajout d'une prescription d'examens à la liste de prescriptions d'examens
     */
    public void ajouterPrescription(PrescriptionExamen pres) {
        this.liste_examens.add(pres);
    }

    /**
     * Ajout d'une prescription de médicaments à la liste de prescriptions
     * médicamenteuses
     */
    public void ajouterPrescription(PrescriptionMedicamenteuse pres) {
        this.liste_presMedic.add(pres);
    }

    /**
     * Récupération de la liste de prescription médicamenteuse
     */
    public ArrayList<PrescriptionMedicamenteuse> getListePresMedic() {
        return this.liste_presMedic;
    }

    /**
     * Récupération de la liste de prescription d'examen
     */
    public ArrayList<PrescriptionExamen> getListePresExam() {
        return this.liste_examens;
    }

   
    /**
     * Recherche d'un DMT d'Anesthésie d'un patient
     */
    //ATTENTION: il faut vérifier dans l'interface que la personne est bien de secteur "anesthesie" et que son statut est bien "PH" 
    public void rechercherUnDMAnesthesie(String IPP, String identifiant) { //identifiant de la personne connectée
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheDMAnesthesie = null;
        PreparedStatement rechercheResultat = null;
        PreparedStatement recherchePrescription = null;
        PreparedStatement rechercheResExam = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête
        ResultSet resultats_bd2 = null;
        ResultSet resultats_bd3 = null;
        ResultSet resultats_bd4 = null;

        //----------- Requête 1: recherche du DMT Anesthésie
        try {
            rechercheDMAnesthesie = con.prepareStatement("select * from DMT_Anesthesie where id = ?");
            rechercheDMAnesthesie.setString(1, IPP);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheDMAnesthesie.executeQuery();
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
                System.out.println("Accès aux résultats refusé 1");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        if (r_id != null) {
            //----------- Requête 2: recherche des résultats d'examen anesthesie (correspondance pour le service demandeur)
            //----------- Requête 3: on cherche les demandes de prescription d'anesthésie
            //----------- Requête 4: on cherche tous les résultats d'examen d'un patient
            try {
                rechercheResultat = con.prepareStatement("select * from Resultat where id = ? and type_examen = ?");
                rechercheResultat.setString(1, r_id);
                rechercheResultat.setString(2, "anesthesie");

                recherchePrescription = con.prepareStatement("select * from Prescription_examen where id = ? and examen = 'anesthesie' ");
                recherchePrescription.setString(1, r_id);

                rechercheResExam = con.prepareStatement("select * from Resultat where id = ?");
                rechercheResExam.setString(1, r_id);
            } catch (Exception e) {
                System.out.println("Erreur de requête");
            }

            //-----------Accès à la base de données
            try {
                resultats_bd2 = rechercheResultat.executeQuery();
                resultats_bd4 = rechercheResExam.executeQuery();
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
            ResultatAnesthesie resultat;

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
                        resultat = new ResultatAnesthesie(r_compteRendu, r_date, r_observationsé, r_id, r_ph, r_service_demandeur);
                        this.ajouterResultat(resultat);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }

                resultats_bd2.close();
                

            } catch (SQLException e) {
                do {
                    System.out.println("Accès aux résultats refusé 2");
                    System.out.println("SQLState : " + e.getSQLState());
                    System.out.println("Description : " + e.getMessage());
                    System.out.println("code erreur : " + e.getErrorCode());
                    System.out.println("");
                    e = e.getNextException();
                } while (e != null);
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
                            pres = new PrescriptionExamen(r_date2, Examen.valueOf("anesthesie"), r_ph2, r_exigences, r_id, r_done);
                            this.ajouterPrescription(pres);
                        }
                    } catch (Exception e) {
                        System.out.println("");
                    }
                }
                resultats_bd3.close();
            } catch (SQLException e) {
                do {
                    System.out.println("Accès aux résultats refusé 0");
                    System.out.println("SQLState : " + e.getSQLState());
                    System.out.println("Description : " + e.getMessage());
                    System.out.println("code erreur : " + e.getErrorCode());
                    System.out.println("");
                    e = e.getNextException();
                } while (e != null);
            }

            //------------------ Ajout des résultats d'examen demandés par le service d'anesthésie ------------------
            //-----------parcours des données retournées
            //---Variables temporaires
            String r_compteRendu3 = "";
            Date r_date3 = null;
            String r_observations3 = "";
            String r_nrpps3 = "";
            PH r_ph3 = new PH();
            ResultatAnesthesie resultat3;

            try {
                while (resultats_bd4.next()) {
                    r_observations3 = resultats_bd4.getString("observations");
                    r_compteRendu3 = resultats_bd4.getString("compte_rendu");
                    r_date3 = resultats_bd4.getDate("date");
                    r_nrpps3 = resultats_bd4.getString("n_rpps");;
                }
                resultats_bd4.close();

                try {
                    //On cherche le PH 
                    r_ph3 = r_ph3.rechercherUnMedecinRPPS(r_nrpps);

                    if (r_ph3 != null) {
                        //On crée le résultat
                        resultat3 = new ResultatAnesthesie(r_compteRendu3, r_date3, r_observations3, r_id, r_ph3, "anesthesie");
                        this.ajouterResultat(resultat3);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                do {
                    System.out.println("Accès aux résultats refusé 4");
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
     * Ajout d'un résultat d'Anesthésie d'un patient
     */
    //Penser à faire dma.ajouterExamen(new Examen.valueOf('ranesthesie')
    public void ajouterResultat(Date date, PrescriptionExamen pres, String id, String observations, String compteRendu, String identifiant) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement ajouterResultat;
        String n_rpps = PH.returnNRPPS(identifiant);
        String service_demandeur = "";

        try {
            //Requête: Ajout du résultat dans le service demandeur
            //On recherche le service demandeur
            PH ph = pres.getPH();
            service_demandeur = ph.getService().getLibelle();
        } catch (Exception e) {
            System.out.println("");
        }

        try {
            String requete = "INSERT INTO Resultat VALUES(? ,?, ?, ?, ? ,'anesthesie',?)";
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
     * Recherche d'un DMT de Radiologie d'un patient
     */
    public boolean creerUnDMAnesthesie(String id, String observations) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement creerDMRadio;
        boolean ok = false;
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        //Requête 1: Ajout d'un DM clinique
        //La vérification de non-existence du DM se fait dans la base de données
        try {
            String requete = "INSERT INTO DMT_Anesthesie VALUES(? ,?)";
            creerDMRadio = con.prepareStatement(requete);
            creerDMRadio.setString(1, id);
            creerDMRadio.setString(2, observations);
            int nbMaj = creerDMRadio.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return ok;
    }

    //TEST
    public static void main(String[] args) {
        DMT_Anesthesie da = new DMT_Anesthesie();
        Date dateTest = new Date(118, 2, 9);

        PrescriptionExamen pres = new PrescriptionExamen(dateTest, Examen.valueOf("anesthesie"), PH.rechercherUnMedecin("house", "gregory"), "pres 2", "180000111", false);
        da.rechercherUnDMAnesthesie("180000111", "ANNB"); //ça marche
        System.out.println(da.getObs());
        //       da.creerUnDMAnesthesie("180000111","anesthesie - bill gates"); //ça marche
        //       da.ajouterResultat(dateTest, pres, "180000111", "rien à redire sur les allergies - anesthésie", "chirurgie feu vert", "ANNB"); //ça marche
    }

}
