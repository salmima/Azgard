/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.io.File;
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
public class Impression_Resultat {
    private Document document;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Element racine;
    
     /* 
    * Constructeur initialisant
    */
    public Impression_Resultat(Resultat res, String typeExamen) {
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
        racine = document.createElement("resultat");
        racine.setAttribute("xmlns:","http://azgardengineering.com/Resultat");
        racine.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance"); 
        racine.setAttribute("xsi:schemaLocation","http://azgardengineering.com/Resultat Resultat.xsd"); 
        document.appendChild(racine);
        
        //Création des éléments fils
        Element date = document.createElement("date_resultat");
        Element type_examen = document.createElement("type_examen");
        Element compte_rendu = document.createElement("compte_rendu");
        Element observations = document.createElement("observations");
        Element informationsRealisateur = document.createElement("informationsRealisateur");
        Element nom = document.createElement("nom");
        Element prenom = document.createElement("prenom");
        Element specialite = document.createElement("specialite");
        Element informationsPatient = document.createElement("informationsPatient");
        Element nomp = document.createElement("nom");
        Element prenomp = document.createElement("prenom");
        Element dateNaissance = document.createElement("date_naissance");
        
        //Ajout des valeurs
        date.appendChild(document.createTextNode(res.getDate().toString()));
        type_examen.appendChild(document.createTextNode(typeExamen));
        compte_rendu.appendChild(document.createTextNode(res.getCompteRendu()));
        observations.appendChild(document.createTextNode(res.getObservations()));
        
        nom.appendChild(document.createTextNode(res.getPH().getNom()));
        prenom.appendChild(document.createTextNode(res.getPH().getPrenom()));
        specialite.appendChild(document.createTextNode(res.getPH().getSpecialite()));

        DossierMedicoAdministratif dma = new DossierMedicoAdministratif();
        dma.rechercherUnDMA(res.getID());
        nomp.appendChild(document.createTextNode(dma.getNom()));
        prenomp.appendChild(document.createTextNode(dma.getPrenom()));
        dateNaissance.appendChild(document.createTextNode(dma.getDateN().toString()));
        
        //Ajout des noeuds
        informationsRealisateur.appendChild(nom);
        informationsRealisateur.appendChild(prenom);
        informationsRealisateur.appendChild(specialite);
        informationsPatient.appendChild(nomp);
        informationsPatient.appendChild(prenomp);
        informationsPatient.appendChild(dateNaissance);
        
        racine.appendChild(date);
        racine.appendChild(type_examen);
        racine.appendChild(informationsRealisateur);
        racine.appendChild(informationsPatient);
        racine.appendChild(compte_rendu);
        racine.appendChild(observations);
        
    }
    
    /* 
    * Renvoie l'objet Document de la lettre de sortie
    */
    public Document getDocument() {
        return this.document;
    }

    //Test
    public static void main(String[] args){
        //Création d'une rescription
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PH ph = new PH();
        ph = PH.rechercherUnMedecin("house", "gregory");

        //En fonction du résultat que l'on veut imprimer, changer de constructeur
        Resultat res = new ResultatImagerie("compte-rendu", sqlDate, "observations", "180000111", ph);
        
        Impression_Resultat imp = new Impression_Resultat(res,"radiologie");
        
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        
        try {
            transformer = transformerFactory.newTransformer();
        } catch (Exception e) {
            System.out.println("erreur");
        }
        
        //Transformation
        final DOMSource source = new DOMSource(imp.getDocument());
        //Code à utiliser pour afficher dans un fichier
        final StreamResult sortie = new StreamResult(new File("C:\\Users\\Thao\\Documents\\NetBeansProjects\\TIS4\\AloesManager\\src\\xml\\res.xml"));
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
