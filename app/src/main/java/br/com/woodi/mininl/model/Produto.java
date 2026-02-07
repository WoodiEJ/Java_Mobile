package br.com.woodi.mininl.model;

public class Produto {

    private int id;
    private String descricao;
    private String ean;
    private double preco;

    public Produto(int id, String descricao, String ean, double preco) {
        this.id = id;
        this.descricao = descricao;
        this.ean = ean;
        this.preco = preco;
    }

    public String getDescricao() { return descricao; }

    public String getEan() { return ean; }

    public double getPreco() { return preco; }

}
