package model;

public class CampoTermo {

    private int id;
    private int idTermo;
    private int posicao;
    private String descricao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTermo() {
        return idTermo;
    }

    public void setIdTermo(int idTermo) {
        this.idTermo = idTermo;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
