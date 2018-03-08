<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : LettreDeSortie.xsl
    Created on : 8 mars 2018, 10:51
    Author     : Thao
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <xsl:template match="/">
        <xsl:variable name="type_impression" select="name(*)"/>
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

            <!-- Modèle de pages -->
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4"
                                       page-height="29.7cm"
                                       page-width="21cm"
                                       margin-top="2cm"
                                       margin-bottom="1cm"
                                       margin-left="2.5cm"
                                       margin-right="2.5cm">
                    <fo:region-body margin-top="5cm"/>
                    <fo:region-before extent="3cm"/>
                    <fo:region-after extent="1.5cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
       
            <!-- Contenus -->
            <fo:page-sequence master-reference="A4">
                <!-- Contenu de la tête de page -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block text-align="center"> 
                        <fo:block font-size="14pt" text-align="center" line-height="26pt" space-before="0.5cm" space-after="0.0cm">
                            Centre Hospitalier Universitaire de Princeton Plainsboro
                            <fo:block>&#160;</fo:block>
                        </fo:block>
                        <fo:block font-size="18pt" text-align="center" line-height="30pt" space-before="0.5cm" space-after="0.0cm">
                            <!-- On teste le type d'impression -->
                            <xsl:if test="$type_impression = 'prescription_medic'">
                                Prescription médicamenteuse
                            </xsl:if>
                            <xsl:if test="$type_impression = 'prescription_examen'">
                                Prescription d'un examen
                            </xsl:if>
                            
                            <xsl:if test="$type_impression = 'resultat'">
                                Résultat d'un examen
                                <!-- On teste le type d'examen -->
                                <xsl:variable name="type" select="//type_examen"/>
                                <xsl:if test="$type = 'radiologie'">
                                    Radiologie
                                </xsl:if>
                                <xsl:if test="$type = 'hematologie'">
                                    Hématologie
                                </xsl:if>
                                <xsl:if test="$type = 'biologie'">
                                    Biologie
                                </xsl:if>
                                <xsl:if test="$type = 'anapathologie'">
                                    Anapathologie
                                </xsl:if>
                                <xsl:if test="$type = 'anesthesie'">
                                    Anesthésie
                                </xsl:if>
                            </xsl:if>
                                                    
                            <fo:block>&#160;</fo:block>
                        </fo:block>
                    </fo:block>
                </fo:static-content>
                
                
                <!-- Contenu de la partie centrale -->
                <fo:flow flow-name="xsl-region-body">
                    <fo:block text-align="justify"
                              font="12pt Times" space-before="10.0cm"
                              border="black solid thick">

                        <!-- On teste le type d'impression -->
                        <xsl:if test="$type_impression = 'prescription_medic'">
                            Date de prescription: <xsl:value-of select ="//*[local-name()='date_prescription']"/>
                                                   
                            <fo:block>&#160;</fo:block>
                            <fo:inline font-weight="bold">Informations sur le prescripteur:</fo:inline>
                            <fo:block>&#160;</fo:block>
                            Nom: <xsl:value-of select ="//*[local-name()='informationsPrescripteur']/*[local-name()='nom']"/>                        
                            <fo:block>&#160;</fo:block>
                            Prénom: <xsl:value-of select ="//*[local-name()='informationsPrescripteur']/*[local-name()='prenom']"/>                         
                            <fo:block>&#160;</fo:block>
                            Spécialité: <xsl:value-of select ="//*[local-name()='informationsPrescripteur']/*[local-name()='specialite']"/>                        
                            <fo:block>&#160;</fo:block>
                                                    
                            <fo:block>&#160;</fo:block>
                            <fo:inline font-weight="bold">Informations sur le patient:</fo:inline>
                            <fo:block>&#160;</fo:block>
                            Nom: <xsl:value-of select ="//*[local-name()='informationsPatient']/*[local-name()='nom']"/>                        
                            <fo:block>&#160;</fo:block>
                            Prénom: <xsl:value-of select ="//*[local-name()='informationsPatient']/*[local-name()='prenom']"/>                         
                            <fo:block>&#160;</fo:block>
                            Date de naissance: <xsl:value-of select ="//*[local-name()='informationsPatient']/*[local-name()='date_naissance']"/>                         
                            <fo:block>&#160;</fo:block>
                                                   
                            <fo:block>&#160;</fo:block>
                            Prescription médicamenteuse:
                            <xsl:value-of select ="//*[local-name()='liste_medicaments']"/>
                        </xsl:if>
                        
                        <xsl:if test="$type_impression = 'prescription_examen'">
                            Date de prescription: <xsl:value-of select ="//*[local-name()='date_prescription']"/>
                            <fo:block>&#160;</fo:block>
                            Type de l'examen: <xsl:value-of select ="//*[local-name()='type_examen']"/> 
                            <fo:block>&#160;</fo:block>
                           
                            <fo:inline font-weight="bold">Informations sur le prescripteur:</fo:inline>
                            <fo:block>&#160;</fo:block>
                            Nom: <xsl:value-of select ="//*[local-name()='informationsPrescripteur']/*[local-name()='nom']"/>
                            <fo:block>&#160;</fo:block>
                            Prénom: <xsl:value-of select ="//*[local-name()='informationsPrescripteur']/*[local-name()='prenom']"/> 
                            <fo:block>&#160;</fo:block>
                            Spécialité: <xsl:value-of select ="//*[local-name()='informationsPrescripteur']/*[local-name()='specialite']"/>
                           
                            <fo:block>&#160;</fo:block>
                            <fo:inline font-weight="bold">Informations sur le patient:</fo:inline>
                            <fo:block>&#160;</fo:block>
                            Nom: <xsl:value-of select ="//*[local-name()='informationsPatient']/*[local-name()='nom']"/>                        
                            <fo:block>&#160;</fo:block>
                            Prénom: <xsl:value-of select ="//*[local-name()='informationsPatient']/*[local-name()='prenom']"/>                         
                            <fo:block>&#160;</fo:block>
                            Date de naissance: <xsl:value-of select ="//*[local-name()='informationsPatient']/*[local-name()='date_naissance']"/>                         
                            <fo:block>&#160;</fo:block>
                            
                            <fo:block>&#160;</fo:block>
                            Exigences de l'examen:
                            <xsl:value-of select ="//*[local-name()='exigences_examen']"/>
                        </xsl:if>
                          
                        <xsl:if test="$type_impression = 'resultat'">
                            Résultat d'un examen
                            <!-- On teste le type d'examen -->
                            <xsl:variable name="type" select="//*[local-name()='type_examen']"/>
                            <xsl:if test="$type = 'radiologie'">
                                Examen de radiologie              
                            </xsl:if>
                            <xsl:if test="$type = 'hematologie'">
                                Examen d'hématologie
                            </xsl:if>
                            <xsl:if test="$type = 'biologie'">
                                Examen de biologie
                            </xsl:if>
                            <xsl:if test="$type = 'anapathologie'">
                                Examen d'anapathologie
                            </xsl:if>
                            <xsl:if test="$type = 'anesthesie'">
                                Examen d'anesthésie
                            </xsl:if>
                            <fo:block>&#160;</fo:block>
                       
                    
                            Date du résultat: <xsl:value-of select ="//*[local-name()='date_resultat']"/> 
                            <fo:block>&#160;</fo:block>
                                
                            <fo:inline font-weight="bold">Informations sur le réalisateur:</fo:inline>
                            <fo:block>&#160;</fo:block>
                            Nom: <xsl:value-of select ="//*[local-name()='informationsRealisateur']/*[local-name()='nom']"/> 
                            <fo:block>&#160;</fo:block>
                            Prénom: <xsl:value-of select ="//*[local-name()='informationsRealisateur']/*[local-name()='prenom']"/>  
                            <fo:block>&#160;</fo:block>
                            Spécialité: <xsl:value-of select ="//*[local-name()='informationsRealisateur']/*[local-name()='specialite']"/> 
                            <fo:block>&#160;</fo:block>
                           
                            <fo:block>&#160;</fo:block>
                            <fo:inline font-weight="bold">Informations sur le patient:</fo:inline>
                            <fo:block>&#160;</fo:block>
                            Nom: <xsl:value-of select ="//*[local-name()='informationsPatient']/*[local-name()='nom']"/>                        
                            <fo:block>&#160;</fo:block>
                            Prénom: <xsl:value-of select ="//*[local-name()='informationsPatient']/*[local-name()='prenom']"/>                         
                            <fo:block>&#160;</fo:block>
                            Date de naissance: <xsl:value-of select ="//*[local-name()='informationsPatient']/*[local-name()='date_naissance']"/>                         
                            <fo:block>&#160;</fo:block> 
                            <fo:block>&#160;</fo:block>
                            Compte-rendu:
                            <xsl:value-of select ="//*[local-name()='compte_rendu']"/>
                            <fo:block>&#160;</fo:block>
                            Observations:
                            <xsl:value-of select ="//*[local-name()='observations']"/> 
                        </xsl:if>       
                
                    </fo:block>
                </fo:flow>
    
            </fo:page-sequence>
        </fo:root>
       
    </xsl:template>

</xsl:stylesheet>
