package br.com.woodi.mininl.model;

public class Venda {

    private int id;
    private double total;
    private String dataHora;

    public Venda(int id, double total, String dataHora) {
        this.id = id;
        this.total = total;
        this.dataHora = dataHora;
    }

    public int getId() {
        return id;
    }

    public double getTotal() {
        return total;
    }

    public String getDataHora() {
        return dataHora;
    }
}
