package br.com.woodi.mininl.model;

public class VendaItem {

    private Produto produto;
    private double quantidade;

    public VendaItem(Produto produto, double quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() { return produto; }
    public double getQuantidade() { return quantidade; }

    public double getTotalLinha() { return produto.getPreco() * quantidade; }

    public void somarQuantidade(double qtd) {
        this.quantidade += qtd;
    }

}
