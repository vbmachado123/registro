package model;

public class Configuracao {
    private int id;
    private String email = "";
    private String nome = "";
    private String caminhoImagem = "";
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

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getCaminhoPasta() {
        return caminhoPasta;
    }

    public void setCaminhoPasta(String caminhoPasta) {
        this.caminhoPasta = caminhoPasta;
    }
}
