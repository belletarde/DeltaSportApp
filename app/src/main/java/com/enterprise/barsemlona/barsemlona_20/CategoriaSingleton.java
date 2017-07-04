package com.enterprise.barsemlona.barsemlona_20;

import java.util.ArrayList;


class CategoriaSingleton {
    private ArrayList<String> nome = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> desc = new ArrayList<>();


    private final static CategoriaSingleton ourInstance = new CategoriaSingleton();
    private CategoriaSingleton() {
    }
    public static CategoriaSingleton getInstance() {
        return ourInstance;
    }
    public void setNome( String nome) { this.nome.add(nome); }
    public  ArrayList<String> getNome() {
        return nome;
    }
    public void setId( String id) { this.id.add(id); }
    public  ArrayList<String> getId() {
        return id;
    }
    void setDescricao(String desc) { this.desc.add(desc); }



}
