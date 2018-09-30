package com.example.antoine.deadornotdead.wiki;

import com.example.antoine.deadornotdead.MessageEventPage;
import com.example.antoine.deadornotdead.recyclingReponse.Personne;

/**
 * Created by antoine.gagneux on 13/10/2017.
 */

public class ParseWiki {

    private String pageTxt;
    private String reponse;
    private MessageEventPage mep;
    private Personne poloResult;
    private String erreur;

    private int tailleBlocRecup = 500;
    private int troplong = 100;

    public ParseWiki(String pageTxt,String nomRequete) {
        this.pageTxt = pageTxt;
        poloResult= new Personne(nomRequete);
        erreur=null;
    }
    //Si on envoie toute la réponse
    public ParseWiki(String reponse,int all,String nomRequete) {
        this.reponse = reponse;
        this.pageTxt="";
        poloResult= new Personne(nomRequete);
        erreur=null;
    }
    public int parse() {
        int retour=1;
        if (pageTxt.length()<1) {
            retour=preParsing();
        }
        if (retour!=0) {
            retour = parsePage();
        } else {
            erreur="erreur : parsing de la réponse brute";
        }
        return retour;
    }

    private int preParsing() {

        if (reponse==null) return 0;
        if (reponse.length()<1) return 0;

        int debut = reponse.indexOf("export");
        if (debut==-1) return 0;
        pageTxt =  reponse.substring(debut);

        return 1;
    }

    /******Parsing page wiki : pageTxt******************/

    private int parsePage() {

        if (pageTxt==null || pageTxt.length()<1) {
            erreur="erreur : y'a rien à parser";
            return 0;
        }

        int debutBD =pageTxt.indexOf("birth_date");
        int debutBP =pageTxt.indexOf("birth_place");
        int debutDD =pageTxt.indexOf("death_date");
        int debutDP  =pageTxt.indexOf("death_place");
        int debutBlock;
        int finBlock;

        if (debutBD==-1) {
            erreur="erreur : pas de birth, est-ce vraiment une personne?";
            return 0;
        }

        // Date naissance
        debutBlock=rechercheDebutDoubleAccoladeOuPipe(debutBD);
        if (debutBlock>-1) {
            finBlock=rechercheFinDoubleAccolade(debutBlock);
            if (finBlock>-1) {
                String tmp = getDateFromBlock(pageTxt.substring(debutBlock+2,finBlock));
                if (tmp.length()<1) {
                    erreur="impossible de récuperer 3 dates birth";
                    return 0;
                } else {
                    poloResult.setDateNaissance(tmp);
                }
            } else {
                erreur="pas de fin de block birth !!";
                return 0;
            }
        } else {
            erreur="erreur : pas de birth, est-ce vraiment une personne?";
            return 0;
        }

        // Date deces
        if (debutDD==-1) {
            //System.out.println("Pas de terme death_date ");
        } else {
            debutBlock=rechercheDebutDoubleAccoladeOuPipe(debutDD);
            if (debutBlock>-1) {
                finBlock=rechercheFinDoubleAccolade(debutBlock);
                if (finBlock>-1) {
                    String tmp = getDateFromBlock(pageTxt.substring(debutBlock+2,finBlock));
                    if (tmp.length()<1) {
                        erreur="impossible de récuperer 3 dates death";
                        return 0;
                    } else {
                        poloResult.setDateDeces(tmp);
                        poloResult.setMort(true);
                        poloResult.calculAge();
                    }
                } else {
                    erreur="pas de fin de block death !!";
                    return 0;
                }
            } else {
                //System.out.println("Pas de date de deces");
            }
        }

        mep=new MessageEventPage();
        mep.setPersonne(poloResult);


        return 1;
    }

    private int rechercheFinDoubleAccolade(int debut) {
        int result = -1;
        int i=debut;
        while(result ==-1 && i<debut+troplong) {
            if (pageTxt.charAt(i)=='}') {
                result = i;
            }
            i++;
        }
        return result;
    }
    private int rechercheDebutDoubleAccoladeOuPipe(int debut) {
        //renvoie pos si {{ existe -1 si on tombe d'abord sur | ou trop long
        int result = -1;
        int i=debut;
        while(result ==-1 && i<debut+troplong && pageTxt.charAt(i)!='|') {
            if (pageTxt.charAt(i)=='{') {
                result = i;
            }
            i++;
        }
        return result;
    }


    private String getDateFromBlock(String block) {
        block = block.trim();
        String[] tabBlock = block.split("\\|");
        int[] tabResult =new int[6];
        int k=0;
        for(int i=tabBlock.length-1;i>=0;i--) {
            try {
                int nb=Integer.parseInt(tabBlock[i]);
                tabResult[k]=nb;
                k++;
            }catch (Exception e) {

            }
        }
        if (k==3) {
            return tabResult[0]+" "+tabResult[1]+" "+tabResult[2];
        }
        if (k==6) {
            return tabResult[3]+" "+tabResult[4]+" "+tabResult[5];
        }
        return "";


    }



    public MessageEventPage getMep() {
        return mep;
    }

    public String getErreur() {
        return erreur;
    }
}
