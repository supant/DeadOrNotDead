package com.example.antoine.deadornotdead;

import java.util.LinkedList;

public class MessageEventListTitle {

    private LinkedList<String> listTitle= new LinkedList<String>();

    public LinkedList<String> getListTitle() {
        return listTitle;
    }

    public void addtListTitle(String title) {
        this.listTitle.add(title);
    }

}