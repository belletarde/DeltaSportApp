package com.enterprise.barsemlona.barsemlona_20;


class UserSingleton {

    String id;
    private int enderecoId;  private String pagamentoId;

    String getPagamentoId() {
        return pagamentoId;
    }

    void setPagamentoId(String pagamentoId) {
        this.pagamentoId = pagamentoId;
    }



    int getEnderecoId() {
        return enderecoId;
    }

    void setEnderecoId(int enderecoId) {
        this.enderecoId = enderecoId;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }
    private static final UserSingleton ourInstance = new UserSingleton();

    static UserSingleton getInstance() {
        return ourInstance;
    }

    private UserSingleton() {
    }
}