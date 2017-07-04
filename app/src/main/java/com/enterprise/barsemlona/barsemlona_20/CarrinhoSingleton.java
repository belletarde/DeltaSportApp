package com.enterprise.barsemlona.barsemlona_20;

import java.math.BigDecimal;
import java.util.ArrayList;

class CarrinhoSingleton {

    private ArrayList<String> nome = new ArrayList<String> ();
    private ArrayList<String> preco = new ArrayList<String> ();
    private ArrayList<String> desc = new ArrayList<String> ();
    private ArrayList<String> cat = new ArrayList<String> ();
    private ArrayList<String> qtd = new ArrayList<String> ();
    private ArrayList<String> precoComDesconto = new ArrayList<String> ();
    ArrayList<String> id = new ArrayList<String> ();
    private ArrayList<String> desconto = new ArrayList<String> ();

    ArrayList<String> precoCompleto = new ArrayList<String> ();

    void setDestroi(int i){
        this.nome.remove(i);
        this.preco.remove(i);
        this.desc.remove(i);
        this.cat.remove(i);
        this.qtd.remove(i);
        this.id.remove(i);
        this.precoComDesconto.remove(i);
        this.desconto.remove(i);



    }

    ArrayList<String> getDesconto() {
        return desconto;
    }

    void setDesconto(String desconto) {
        this.desconto.add(desconto);
    }




    private final static CarrinhoSingleton ourInstance = new CarrinhoSingleton();
    private CarrinhoSingleton() {
    }

    public static CarrinhoSingleton getInstance() {
        return ourInstance;
    }
    public void setNome( String nome) { this.nome.add(nome); }
    public  ArrayList<String> getNome() {
        return nome;
    }
    void setPreco(String preco) { this.preco.add(preco); }
    ArrayList<String> getPreco() {
        return preco;
    }
    void setDescricao(String desc) { this.desc.add(desc); }
    ArrayList<String> getDescricao() {
        return desc;
    }
    void setCat(String cat) { this.cat.add(cat); }
    ArrayList<String> getCat() {
        return cat;
    }
    String getTotal(){
        return Total();
    }



    ArrayList<String> getQtd() {
        return qtd;
    }

    public void setId( String id) { this.id.add(id); }

    public  ArrayList<String> getId() {
        return id;
    }

    void setQtd(String qtd, int i) {
        this.qtd.set(i,qtd);

    }
    void setQtd(String qtd) {

        this.qtd.add(qtd);
    }



    void setDestroiTudo(){
        int i = 0;
        while (id.size() > 0) {
            this.nome.remove(i);
            this.preco.remove(i);
            this.desc.remove(i);
            this.cat.remove(i);
            this.qtd.remove(i);
            this.id.remove(i);
            this.precoComDesconto.remove(i);
            this.desconto.remove(i);
            i++;
        }


    }


    private String Total(){
        BigDecimal total = new BigDecimal(0.00);
        for (int i = 0 ;i<getId().size();i++ ){
            BigDecimal  x = valorDescontado(preco.get(i),desconto.get(i));
            precoComDesconto.add(x.toString());
            x = x.multiply(new BigDecimal(qtd.get(i)));
            total=total.add(x);

        }
        return total.toString();
    }

    private BigDecimal valorDescontado(String val, String desc){
        BigDecimal  x;
        x = new BigDecimal(val).subtract(new BigDecimal(desc));
        return x;

    }
}