package model;

public class Configuracao {
    private int id;
    private String email = "";
    private String nome = "";
    private int exibeAssinatura = 0;
    private String caminhoPasta = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getExibeAssinatura() {
        return exibeAssinatura;
    }

    public void setExibeAssinatura(int exibeAssinatura) {
        this.exibeAssinatura = exibeAssinatura;
    }

    public String getCaminhoPasta() {
        return caminhoPasta;
    }

    public void setCaminhoPasta(String caminhoPasta) {
        this.caminhoPasta = caminhoPasta;
    }
}
