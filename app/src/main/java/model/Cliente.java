package model;

/**
 * Created by victor on 31/01/20.
 */

public class Cliente {

    private Long id;
    private String endereco="";
    private String nome="";
    private String rg="";
    private String cpf="";
    private String celular="";
    private String opcaoEscolhida;

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getOpcaoEscolhida() {
        return opcaoEscolhida;
    }

    public void setOpcaoEscolhida(String opcaoEscolhida) {
        this.opcaoEscolhida = opcaoEscolhida;
    }

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }
}
