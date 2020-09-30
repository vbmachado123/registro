package model;

public class Imagem {

    private int id;
    private int idFormulario;
    private int idUsuario;
    private String caminho;
    private String tipo;
    private int formulario;

    /* Tipos de Imagem
    * Documento
    * Assinatura
    * Logo */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaminho() {
        return caminho;
    }
 
    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getFormulario() {
        return formulario;
    }

    public void setFormulario(int formulario) {
        this.formulario = formulario;
    }

    public int getIdFormulario() {
        return idFormulario;
    }

    public void setIdFormulario(int idFormulario) {
        this.idFormulario = idFormulario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
