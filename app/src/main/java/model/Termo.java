package model;

public class Termo {

    private int id;
    private String titulo;
    private int categoria;

    /* CATEGORIAS
    * 0 - CADASTRO
    * 1 - VISTORIA
    * 2 - COBRANÇAS
    * 3 - IRREGULARIDADES */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
}
