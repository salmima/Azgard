package fc.aloesmanager;

import java.sql.*;

public abstract class Resultat {

    protected String compteRendu;
    protected Date date;
    protected String observations;
    protected String id;
    protected PH ph; //ph qui a ajouté le résultat
    protected String service_demandeur; //service demandeur
    
    /**
     * Retourne le compte-rendu
     */
    public String getCompteRendu(){
        return this.compteRendu;
    }
    
    /**
     * Retourne les dates
     */
    public Date getDate(){
        return this.date;
    }
    
    /**
     * Retourne les observations
     */
    public String getObservations(){
        return this.observations;
    }
    
    //pour obtenir le type de résultat, tester avec instanceOf ResultatBiologie, ResultatAnapathologie, ResultatImagerie
    
     /**
     * Retourne l'ID du patient
     */
    public String getID(){
        return this.id;
    }
    
    
    /**
     * Retourne le numéro RPPS du personnel MT qui a publié le résultat
     */
    public PH getPH(){
        return this.ph;
    }
    
    /**
     * Méthode renvoie la liste des résultats pour un patient dans tous les
     * services MT 
     * Méthode à utiliser pour automatiser le remplissage de la
     * lettre de sortie secteur est le secteur de la personne demandeur
     */
    public static ArrayList<Resultat> retourneListeResultat(String IPP, String secteur) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheRes = null;
        ResultSet resultats_bd = null;
        ArrayList<Resultat> listeRes = new ArrayList<Resultat>(); //on retourne une liste d'examens

        //----------- Requête 1: recherche des résultats d'un certain service demandeur
        try {
            rechercheRes = con.prepareStatement("select * from Resultat where id = ? and lower(service_demandeur)=? ORDER BY date desc");
            rechercheRes.setString(1, IPP);
            rechercheRes.setString(2, secteur.toLowerCase());
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------parcours des données retournées
        //---Variables temporaires
        String r_compteRendu = "";
        Date r_date = null;
        String r_observations = "";
        String r_nrpps = "";
        String r_type_examen = "";
        String r_service_demandeur = "";
        PH r_ph = new PH();
        Resultat resultat = null;
        try {
            while (resultats_bd.next()) {
                r_observations = resultats_bd.getString("observations");
                r_compteRendu = resultats_bd.getString("compte_rendu");
                r_date = resultats_bd.getDate("date");
                r_nrpps = resultats_bd.getString("n_rpps");
                r_type_examen = resultats_bd.getString("type_examen");
                r_service_demandeur = resultats_bd.getString("service_demandeur");

                try {
                    //On cherche le PH
                    r_ph = r_ph.rechercherUnMedecinRPPS(r_nrpps);

                    if (r_nrpps != null) {
                        if (r_type_examen.equals("radiologie")) {
                            resultat = new ResultatImagerie(r_compteRendu, r_date, r_observations, IPP, r_ph,r_service_demandeur);
                            listeRes.add(resultat);
                        } else if (r_type_examen.equals("hematologie")) {
                            resultat = new ResultatHematologie(r_compteRendu, r_date, r_observations, IPP, r_ph,r_service_demandeur);
                            listeRes.add(resultat);
                        } else if (r_type_examen.equals("anatomopathologie")) {
                            resultat = new ResultatAnapathologie(r_compteRendu, r_date, r_observations, IPP, r_ph,r_service_demandeur);
                            listeRes.add(resultat);
                        } else if (r_type_examen.equals("biologie")) {
                            resultat = new ResultatBiologique(r_compteRendu, r_date, r_observations, IPP, r_ph,r_service_demandeur);
                            listeRes.add(resultat);
                        } //ajouter resultat anesthesie
                        listeRes.add(resultat);
                    }
                } catch (Exception e) {
                    System.out.println("");
                }
            }

            resultats_bd.close();
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

        return listeRes;
    }

}
