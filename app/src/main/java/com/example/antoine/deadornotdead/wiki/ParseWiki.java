package com.example.antoine.deadornotdead.wiki;

import com.example.antoine.deadornotdead.MessageEventPage;

/**
 * Created by antoine.gagneux on 13/10/2017.
 */

public class ParseWiki {

    private String pageTxt;
    private String reponse;
    private MessageEventPage mep;
    private String erreur;

    private int tailleBlocRecup = 500;

    public ParseWiki(String pageTxt) {
        this.pageTxt = pageTxt;
        erreur=null;
    }
    //Si on envoie toute la réponse
    public ParseWiki(String reponse,int all) {
        this.reponse = reponse;
        this.pageTxt="";
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
    private int parsePage() {

        if (pageTxt==null || pageTxt.length()<1) {
            erreur="erreur : y'a rien à parser";
            return 0;
        }

        int debut = pageTxt.indexOf("birth_date");
        if (debut==-1) {
            erreur="erreur : pas de birth, est-ce vraiment une personne?";
            return 0;
        }

        int taille = tailleBlocRecup;
        if(debut+taille>=reponse.length()) taille= pageTxt.length()-debut;
        String petitBlock=pageTxt.substring(debut,debut+taille);

        mep=new MessageEventPage();
        mep.setTxtTmp(petitBlock);

        return 1;
    }

    public MessageEventPage getMep() {
        return mep;
    }

    public String getErreur() {
        return erreur;
    }
}
