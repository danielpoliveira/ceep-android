package br.com.alura.ceep.model;

import java.io.Serializable;

public class Nota implements Serializable {

    private String titulo;
    private String descricao;
    private Cor cor;

    public Nota(String titulo, String descricao, Cor cor) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.cor = cor;
    }

    public Nota() { }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Cor getCor() {
        return cor;
    }

}