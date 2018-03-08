/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Thao
 */
public class Impression_LettreDeSortie {

    private Document document;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Element racine;

    /* 
    * Constructeur initialisant
    */
    public Impression_LettreDeSortie() {
        this.document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            //Création d'un parseur
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Création d'un Document
        document = builder.newDocument();
        //Création de l'élément racine et ajout au document
        racine = document.createElement("lettreDeSortie");
        racine.setAttribute("xmlns:","http://azgardengineering.com/LettreDeSortie");
        racine.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance"); 
        racine.setAttribute("xsi:schemaLocation","http://azgardengineering.com/LettreDeSortie LettreDeSortie.xsd"); 
        document.appendChild(racine);
    }

    /* 
    * Renvoie l'objet Document de la lettre de sortie
    */
    public Document getDocument() {
        return this.document;
    }

    /*
    *  Méthode permettant de finaliser la lettre de sortie
     */
    public void faireUneLettre(Element infosMA, Element infosMedic) {
        racine.appendChild(infosMA);
        racine.appendChild(infosMedic);
    }

    /*Méthode créant un document XML afin de stocker les informations médico-administratives obligatoires
    * de la lettre de sortie
    */
    public Element infosMedicoAdministratives(String nomp, String prenomp, String dateNaissancep, String sexep, String dateRedaction, 
           String nomRedacteur, String modeEntree, String statut) {
        //Ajout des rubriques médico-administratives
        //Informations obligatoires
        Element infosMedicoAdministratives = document.createElement("informationsMedicoAdministratives");

        Element patient = document.createElement("informationsPatient");
        Element nom = document.createElement("nom");
        Element prenom = document.createElement("prenom");
        Element dateNaissance = document.createElement("dateNaissance");
        Element sexe = document.createElement("sexe");
        Element date_redaction = document.createElement("date_redaction"); //non obligatoire mais choix de l'ajouter
        Element nom_redacteur = document.createElement("nom_redacteur"); //non obligatoire mais choix de l'ajouter
        Element mode_entree = document.createElement("moyen_arrivee"); //non obligatoire mais choix de l'ajouter
        Element statut_document = document.createElement("statut");

        //Ajout des valeurs
        nom.appendChild(document.createTextNode(nomp));
        prenom.appendChild(document.createTextNode(prenomp));
        dateNaissance.appendChild(document.createTextNode(dateNaissancep));
        sexe.appendChild(document.createTextNode(sexep));

        //Ces valeurs peuvent être nulles
        date_redaction.appendChild(document.createTextNode(dateRedaction));
        nom_redacteur.appendChild(document.createTextNode(nomRedacteur));
        mode_entree.appendChild(document.createTextNode(modeEntree));
        statut_document.appendChild(document.createTextNode(statut));

        //Ajout au noeud parent
        patient.appendChild(nom);
        patient.appendChild(prenom);
        patient.appendChild(dateNaissance);
        patient.appendChild(sexe);

        infosMedicoAdministratives.appendChild(patient);
        infosMedicoAdministratives.appendChild(date_redaction);
        infosMedicoAdministratives.appendChild(nom_redacteur);
        infosMedicoAdministratives.appendChild(mode_entree);
        infosMedicoAdministratives.appendChild(statut_document);

        //Informations non obligatoires
        //A ajouter avec les autres méthodes
        return infosMedicoAdministratives;
    }

    //INFORMATIONS OPTIONNELLES
    /*Méthode permettant d'ajouter les informations du médecin traitant
    * dans la lettre de sortie
     */
    //element est infosMedicoAdministratives
    public void ajouterUnMedecinTraitant(Element infosMedicoAdministratives, String nomMed, String prenomMed, String codePostal, String adresse, String tel) {
        Element medecinTraitant = document.createElement("medecinTraitant");
        Element nom_med = document.createElement("nom");
        Element prenom_med = document.createElement("prenom");
        Element codePostal_med = document.createElement("codePostal");
        Element adresse_med = document.createElement("adresse");
        Element tel_med = document.createElement("telephone");

        //Ajout des valeurs
        nom_med.appendChild(document.createTextNode(nomMed));
        prenom_med.appendChild(document.createTextNode(prenomMed));
        codePostal_med.appendChild(document.createTextNode(codePostal));
        adresse_med.appendChild(document.createTextNode(adresse));
        tel_med.appendChild(document.createTextNode(tel));

        //Ajout au noeud parent
        medecinTraitant.appendChild(nom_med);
        medecinTraitant.appendChild(prenom_med);
        medecinTraitant.appendChild(codePostal_med);
        medecinTraitant.appendChild(adresse_med);
        medecinTraitant.appendChild(tel_med);

        //Ajout à infosMedicoAdministratives
        infosMedicoAdministratives.appendChild(medecinTraitant);
    }

    /* Méthode permettant d'ajouter les dates d'entrée et de sortie aux informations medico-administratives
    * dans la lettre de sortie
     */
    public void ajouterDates(Element infosMedicoAdministratives, String dateE, String dateS) {
        Element dates = document.createElement("dates");
        Element date_entree = document.createElement("dateEntree");
        Element date_sortie = document.createElement("dateSortie");

        //Ajout des valeurs
        date_entree.appendChild(document.createTextNode(dateE));
        date_sortie.appendChild(document.createTextNode(dateS));

        //Ajout au noeud parent
        dates.appendChild(date_entree);
        dates.appendChild(date_sortie);

        //Ajout à infosMedicoAdministratives au noeud parent
        infosMedicoAdministratives.appendChild(dates);
    }


    /* Méthode permettant d'ajouter les dates d'entrée et de sortie aux informations medico-administratives
    * dans la lettre de sortie
     */
    public void ajouterInformationsHopital(Element infosMedicoAdministratives, String serv, String nomRef, String prenomRef, String fonct, String modeEntree) {
        Element hopital = document.createElement("hopital");
        Element service = document.createElement("service");
        Element personne_ref = document.createElement("PH_referent");
        Element nom_ref = document.createElement("nom_referent");
        Element prenom_ref = document.createElement("prenom_referent");
        Element fonction = document.createElement("fonction");
        Element mode_entree = document.createElement("modeEntree");

        //Ajout des valeurs
        service.appendChild(document.createTextNode(serv));
        nom_ref.appendChild(document.createTextNode(nomRef));
        prenom_ref.appendChild(document.createTextNode(prenomRef));
        fonction.appendChild(document.createTextNode(fonct));
        mode_entree.appendChild(document.createTextNode(modeEntree));

        //Ajout au noeud parent
        hopital.setAttribute("hopital", "CHU Princeton Plainsboro");
        hopital.appendChild(service);

        personne_ref.appendChild(nom_ref);
        personne_ref.appendChild(prenom_ref);
        personne_ref.appendChild(fonction);

        mode_entree.setAttribute("mode_entree", modeEntree);

        //Ajout à infosMedicoAdministratives
        infosMedicoAdministratives.appendChild(hopital);
        infosMedicoAdministratives.appendChild(personne_ref);
        infosMedicoAdministratives.appendChild(mode_entree);
    }

    //INFORMATIONS OBLIGATOIRES
    /* Méthode permettant d'ajouter les informations médicales obligatoires
    * dans la lettre de sortie 
     */
    //Pour les actes techniques, on considère que le médecin les rentre à la main dans un champ texte
    public Element infosMedicales(Date date_entree, String motifh, ArrayList<String> pathologies, String BMR, String BHRe, String transfusion, String suiteD, ArrayList<String> medic_avant,
            ArrayList<String> medic_apres, String commentaire_medic, String actesTechniques, ArrayList<PrescriptionExamen> listePres, ArrayList<Resultat> listeRes) {

        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        //Création du parent
        Element infosMedicales = document.createElement("informationsMedicales");

        //Création des enfants
        Element motif = document.createElement("motifHospitalisation");
        Element synthese_medicale = document.createElement("synthese_medicale");
        Element actes = document.createElement("actes_examens");
        Element traitement = document.createElement("traitement_medic");
        Element suite = document.createElement("suite");
        Element porteur_BMR = document.createElement("porteur_BMR");
        Element porteur_BHRe = document.createElement("porteur_BHRe");
        Element transfu = document.createElement("transfusion");

        Element suite_donner = document.createElement("suite_a_donner");

        //Ajout des valeurs rubrique 1
        motif.appendChild(document.createTextNode(motifh));

        //Ajout des valeurs rubrique 2
        //Ajout des élements de la liste de pathologies
        for (int i = 0; i < pathologies.size(); i++) {
            Element path = document.createElement("pathologie");

            path.appendChild(document.createTextNode(pathologies.get(i)));
            synthese_medicale.appendChild(path);
        }

        porteur_BMR.appendChild(document.createTextNode(BMR));
        porteur_BHRe.appendChild(document.createTextNode(BHRe));
        transfu.appendChild(document.createTextNode(transfusion));
        synthese_medicale.appendChild(porteur_BMR);
        synthese_medicale.appendChild(porteur_BHRe);
        synthese_medicale.appendChild(transfu);

        //Ajout des valeurs rubrique 3
        //Ajout des actes techniques (acte + resultat) (CCAM), non pris en charge dans ce logiciel
        //Pas d'actes techniques dans ce logiciel: on les ajoute à la main
        Element actes_techniques = document.createElement("actes_techniques");
        actes_techniques.appendChild(document.createTextNode(actesTechniques));
        actes.appendChild(actes_techniques);

        //Séparation des prescriptions
        //Les prescriptions ont été mises dans l'ordre, les résultats (par type d'examen) devraient donc être dans l'ordre également
        ArrayList<String> exam_compl = new ArrayList<String>(); //radiologie
        ArrayList<String> exam_biol = new ArrayList<String>(); //biologie, hematologie, anapath, anesthésie
        ArrayList<String> exam_hema = new ArrayList<String>();
        ArrayList<String> exam_ana = new ArrayList<String>();
        ArrayList<String> exam_anes = new ArrayList<String>(); //à ajouter après

        ArrayList<String> result_compl = new ArrayList<String>();
        ArrayList<String> result_biol = new ArrayList<String>();
        ArrayList<String> result_hema = new ArrayList<String>();
        ArrayList<String> result_ana = new ArrayList<String>();
        ArrayList<String> result_anes = new ArrayList<String>(); //à ajouter après

        for (int i = 0; i < listePres.size(); i++) {
            if (listePres.get(i).getDate().compareTo(date_entree) > 0) {
                String libelle = listePres.get(i).getLibelleExamen();
                if (libelle.equals("radiologie")) {
                    exam_compl.add(listePres.get(i).getExigences());
                } else if (libelle.equals("hematologie")) {
                    exam_hema.add(listePres.get(i).getExigences());
                } else if (libelle.equals("anesthesie")) {
                    exam_anes.add(listePres.get(i).getExigences());
                } else if (libelle.equals("anatomopathologie")) {
                    exam_ana.add(listePres.get(i).getExigences());
                } else if (libelle.equals("biologie")) {
                    exam_biol.add(listePres.get(i).getExigences());
                }
            }
        }

        //Séparation des résultats
        //ajouter resultat anesthésie plus tard
        for (int i = 0; i < listePres.size(); i++) {
            if (listeRes.get(i).getDate().compareTo(date_entree) > 0) {
                String compte_rendu = listeRes.get(i).getCompteRendu();
                Resultat res = listeRes.get(i);
                if (res instanceof ResultatImagerie) {
                    result_compl.add(listeRes.get(i).getCompteRendu());
                } else if (res instanceof ResultatHematologie) {
                    result_hema.add(listeRes.get(i).getCompteRendu());
                } else if (res instanceof ResultatAnapathologie) {
                    result_ana.add(listeRes.get(i).getCompteRendu());
                } else if (res instanceof ResultatBiologique) {
                    result_biol.add(listeRes.get(i).getCompteRendu());
                }
            }
        }

        //Ajout des examens complémentaires (examen + resultat): examens radiologiques
        //Automatisation
        Element exam_complementaire = document.createElement("examens_complementaires");

        for (int i = 0; i < exam_compl.size(); i++) {
            Element exam = document.createElement("examen");
            Element resultat = document.createElement("resultat");

            exam.appendChild(document.createTextNode(exam_compl.get(i)));
            resultat.appendChild(document.createTextNode(result_compl.get(i)));
            exam_complementaire.appendChild(exam);
            exam_complementaire.appendChild(resultat);
        }
        if (exam_compl.size() != 0){
            actes.appendChild(exam_complementaire);
        }
        

        //Ajout des examens biologiques (examen + resultat)
        //Automatisation
        Element exam_biologie = document.createElement("examens_biologiques");
        for (int i = 0; i < exam_biol.size(); i++) {
            Element examens_biologiques = document.createElement("examen");
            Element resultat = document.createElement("resultat");

            examens_biologiques.appendChild(document.createTextNode(exam_biol.get(i)));
            resultat.appendChild(document.createTextNode(result_biol.get(i)));

            exam_biologie.appendChild(exam_biologie);
            exam_biologie.appendChild(resultat);
        }
        if (exam_biol.size() != 0){
             actes.appendChild(exam_biologie);
        }
       
        //Ajout des valeurs rubrique 4
        //Ajout des médicaments avant admission
        for (int i = 0; i < medic_avant.size(); i++) {
            Element avant = document.createElement("medicament_avant_admission");

            avant.appendChild(document.createTextNode(medic_avant.get(i)));
            traitement.appendChild(avant);
        }
        //Ajout des médicaments après admission
        for (int i = 0; i < medic_apres.size(); i++) {
            Element apres = document.createElement("medicament_apres_admission");

            apres.appendChild(document.createTextNode(medic_apres.get(i)));
            traitement.appendChild(apres);
        }
        Element comm = document.createElement("commentaires");
        comm.appendChild(document.createTextNode(commentaire_medic));

        traitement.appendChild(comm);

        //Ajout des valeurs rubrique 5
        suite_donner.appendChild(document.createTextNode(suiteD));

        //ajout au parent
        infosMedicales.appendChild(motif);
        infosMedicales.appendChild(synthese_medicale);
        infosMedicales.appendChild(actes);
        infosMedicales.appendChild(traitement);
        infosMedicales.appendChild(suite);

        return infosMedicales;
    }

    //Informations OPTIONNELLES
    /* Méthode permettant d'ajouter les antécédents aux informations medicales
    * dans la lettre de sortie
     */
    public void ajouterAntecedents(Element infosMedicales, String antecedent) {
        Element antecedents = document.createElement("antecedents");

        //Ajout des valeurs
        antecedents.appendChild(document.createTextNode(antecedent));

        //Ajout à infosMedicales
        infosMedicales.appendChild(antecedents);
    }

    /* Méthode permettant d'ajouter les évènements indésirables aux informations medicales
    * dans la lettre de sortie
     */
    public void ajouterEvenements(Element infosMedicales, String evenement) {
        Element antecedents = document.createElement("antecedents");

        //Ajout des valeurs
        antecedents.appendChild(document.createTextNode(evenement));

        //Ajout à infosMedicales
        infosMedicales.appendChild(antecedents);
    }

    /* Méthode permettant d'ajouter les évènements indésirables aux informations medicales
    * dans la lettre de sortie
     */
    public void ajouterModeDeVie(Element infosMedicales, String modeVie) {
        Element mode = document.createElement("mode_vie");

        //Ajout des valeurs
        mode.appendChild(document.createTextNode(modeVie));

        //Ajout à infosMedicales
        infosMedicales.appendChild(mode);
    }

    /* Méthode permettant d'ajouter l'historique de la maladie aux informations medicales
    * dans la lettre de sortie
     */
    public void ajouterHistoireMaladie(Element infosMedicales, String histoireMaladie) {
        Element histoire = document.createElement("histoire_maladie");

        //Ajout des valeurs
        histoire.appendChild(document.createTextNode(histoireMaladie));

        //Ajout à infosMedicales
        infosMedicales.appendChild(histoire);
    }

    /* Méthode permettant d'ajouter des examens cliniques et statut fonctionnel dans le service aux informations medicales
    * dans la lettre de sortie
     */
    public void ajouterInfosFonctionnelles(Element infosMedicales, ArrayList<String> revueParOrgane, ArrayList<String> examenClinique, ArrayList<String> constantes, ArrayList<String> etatFonctionnel) {
        Element fonct = document.createElement("informations_fonctionnelles");

        for (int i = 0; i < revueParOrgane.size(); i++) {
            Element revue_organe = document.createElement("revue_organe");

            revue_organe.appendChild(document.createTextNode(revueParOrgane.get(i)));
            fonct.appendChild(revue_organe);
        }

        for (int i = 0; i < examenClinique.size(); i++) {
            Element examen_clinique = document.createElement("examen_clinique");

            examen_clinique.appendChild(document.createTextNode(examenClinique.get(i)));
            fonct.appendChild(examen_clinique);
        }

        for (int i = 0; i < constantes.size(); i++) {
            Element constante = document.createElement("constantes");

            constante.appendChild(document.createTextNode(constantes.get(i)));
            fonct.appendChild(constante);
        }

        for (int i = 0; i < etatFonctionnel.size(); i++) {
            Element etat_fonc = document.createElement("etat_fonctionnel");

            etat_fonc.appendChild(document.createTextNode(etatFonctionnel.get(i)));
            fonct.appendChild(etat_fonc);
        }

        //Ajout à infosMedicales
        infosMedicales.appendChild(fonct);
    }

    /* Méthode permettant d'ajouter l'évolution dans le service aux informations medicales
    * dans la lettre de sortie
     */
    public void ajouterEvolutionService(Element infosMedicales, String evolutionService) {
        Element evolution = document.createElement("evolution_dans_service");

        //Ajout des valeurs
        evolution.appendChild(document.createTextNode(evolutionService));

        //Ajout à infosMedicales
        infosMedicales.appendChild(evolution);
    }

    /* Méthode permettant d'ajouter les éléments remis au patient dans le service aux informations medicales
    * dans la lettre de sortie
     */
    public void ajouterElementsRemis(Element infosMedicales, String elementsRemis) {
        Element elementR = document.createElement("evolution_dans_service");

        //Ajout des valeurs
        elementR.appendChild(document.createTextNode(elementsRemis));

        //Ajout à infosMedicales
        infosMedicales.appendChild(elementR);
    }

    public static void main(String[] args) {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (Exception e) {
            System.out.println("erreur");
        }

        //Création d'une lettre
        Impression_LettreDeSortie lettre = new Impression_LettreDeSortie();

        //Remplissage d'une lettre    
        Element infosMA = lettre.infosMedicoAdministratives("nom", "prenom", "2018-06-07", "M", "2018-03-07", "Monsieur médecin","SAMU", "oui");
        ArrayList<String> pathologies = new ArrayList<String>();
        pathologies.add("thrombose");
        ArrayList<String> medic_avant = new ArrayList<String>();
        medic_avant.add("doliprane");
        ArrayList<String> medic_apres = new ArrayList<String>();
        medic_apres.add("mediator");
        ArrayList<PrescriptionExamen> pres = new ArrayList<PrescriptionExamen>();
        ArrayList<Resultat> res = new ArrayList<Resultat>();
        Date dateTest = new Date(118, 1, 1);
        Element infosMedic = lettre.infosMedicales(dateTest, "douleur à la hanche", pathologies, "non", "non", "non", "pas de suite", medic_avant, medic_apres, "", "actes techniques", pres, res);

        lettre.faireUneLettre(infosMA, infosMedic);
        
        //Transformation
        final DOMSource source = new DOMSource(lettre.getDocument());
        //Code à utiliser pour afficher dans un fichier
         final StreamResult sortie = new StreamResult(new File("C:\\Users\\Thao\\Documents\\NetBeansProjects\\TIS4\\AloesManager\\src\\xml\\lettre.xml"));
        //final StreamResult result = new StreamResult(System.out);
        try {
            transformer.transform(source, sortie);
        } catch (Exception e) {
            System.out.println("erreur");
        }

        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

    }
}
