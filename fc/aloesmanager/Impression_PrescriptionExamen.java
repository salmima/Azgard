/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.io.StringWriter;
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
public class Impression_PrescriptionExamen {

    private Document document;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Element racine;

    /* 
    * Constructeur créant un document contenant le XML
     */
    public Impression_PrescriptionExamen(PrescriptionExamen pres) {
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
        racine = document.createElement("prescription_examen");
        racine.setAttribute("xmlns:", "http://azgardengineering.com/PrescriptionExamen");
        racine.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        racine.setAttribute("xsi:schemaLocation", "http://azgardengineering.com/PrescriptionExamen PrescriptionExamen.xsd");
        document.appendChild(racine);

        //Création des éléments fils
        Element date = document.createElement("date_prescription");
        Element type_examen = document.createElement("type_examen");
        Element exigences_examen = document.createElement("exigences_examen");
        Element informationsPrescripteur = document.createElement("informationsPrescripteur");
        Element nom = document.createElement("nom");
        Element prenom = document.createElement("prenom");
        Element specialite = document.createElement("specialite");
        Element informationsPatient = document.createElement("informationsPatient");
        Element nomp = document.createElement("nom");
        Element prenomp = document.createElement("prenom");
        Element dateNaissance = document.createElement("date_naissance");

        //Ajout des valeurs
        date.appendChild(document.createTextNode(pres.getDate().toString()));
        type_examen.appendChild(document.createTextNode(pres.getLibelleExamen()));
        exigences_examen.appendChild(document.createTextNode(pres.getExigences()));
        nom.appendChild(document.createTextNode(pres.getPH().getNom()));
        prenom.appendChild(document.createTextNode(pres.getPH().getPrenom()));
        specialite.appendChild(document.createTextNode(pres.getPH().getSpecialite()));

        DossierMedicoAdministratif dma = new DossierMedicoAdministratif();
        dma.rechercherUnDMA(pres.getID());
        nomp.appendChild(document.createTextNode(dma.getNom()));
        prenomp.appendChild(document.createTextNode(dma.getPrenom()));
        dateNaissance.appendChild(document.createTextNode(dma.getDateN().toString()));

        //Ajout des noeuds
        informationsPrescripteur.appendChild(nom);
        informationsPrescripteur.appendChild(prenom);
        informationsPrescripteur.appendChild(specialite);
        informationsPatient.appendChild(nomp);
        informationsPatient.appendChild(prenomp);
        informationsPatient.appendChild(dateNaissance);

        racine.appendChild(date);
        racine.appendChild(type_examen);
        racine.appendChild(informationsPrescripteur);
        racine.appendChild(informationsPatient);
        racine.appendChild(exigences_examen);

    }

    /* 
    * Renvoie l'objet Document de la lettre de sortie
     */
    public Document getDocument() {
        return this.document;
    }

    //Test: uniquement pour tester! Ce n'est pas la manière dont on s'en sert dans l'interface!
    public static void main(String[] args) {
        //Création d'une prescription
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PH ph = new PH();
        ph = PH.rechercherUnMedecin("house", "gregory");
        Examen examen = Examen.valueOf("radiologie");
        PrescriptionExamen pres = new PrescriptionExamen(sqlDate, examen, ph, "radio latérale", "180000111", false);

        //Création du fichier XML
        Impression_PrescriptionExamen imp = new Impression_PrescriptionExamen(pres);

        //Instanciation des objets nécessaires
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (Exception e) {
            System.out.println("erreur");
        }

        //Transformation du document contenu dans imp
        final DOMSource source = new DOMSource(imp.getDocument());

        //Code à utiliser pour créer un fichier XML
        //final StreamResult sortie = new StreamResult(new File("C:\\Users\\Thao\\Documents\\NetBeansProjects\\TIS4\\AloesManager\\src\\xml\\pres_examen"));
        
        //Code afin d'afficher le XML dans la sortie
        StringWriter writer = new StringWriter();
        final StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(source, result);
        } catch (Exception e) {
            System.out.println("erreur");
        }
         
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        String strResult = writer.toString();
        System.out.println("test:"+strResult);
    }
}
