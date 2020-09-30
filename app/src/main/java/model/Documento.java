package model;

public class Documento {

    private int id;
    private int idFormulario;
    private String tipo;
    private String documento;

    /* Tipos de Documento
    * RG
    * CPF
    * CNH
    * CNPJ
    * INSCRIÇÃO ESTADUAL
    * INSCRIÇÃO MUNICIPAL
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFormulario() {
        return idFormulario;
    }

    public void setIdFormulario(int idFormulario) {
        this.idFormulario = idFormulario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
