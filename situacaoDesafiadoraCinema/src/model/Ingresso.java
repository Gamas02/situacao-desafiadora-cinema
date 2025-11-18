package model;

public class Ingresso {
    private Long id;
    private String assento;
    private String filme;

    public Ingresso() {
    }

    public Ingresso(Long id, String assento, String filme) {
        this.id = id;
        this.assento = assento;
        this.filme = filme;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAssento() {
        return assento;
    }
    public void setAssento(String assento) {
        this.assento = assento;
    }
    public String getFilme() {
        return filme;
    }
    public void setFilme(String filme) {
        this.filme = filme;
    }

    @Override
    public String toString(){
        return "Ingresso[assento=" + assento + 
        ", filme=" + filme +
        "  ]";
    }
}
