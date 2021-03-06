package model;

import java.io.Serializable;

/**
 * Created by victor on 01/02/20.
 */

public class Formulario implements Serializable {

    private Integer id;
    private String endereco;
    private String nome;
    private String rg;
    private String cpf;
    private String celular;
    private String responsabilidade;
    private String caminhoImagem;
    private int idTermoSelecionado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getResponsabilidade() {
        return responsabilidade;
    }

    public void setResponsabilidade(String responsabilidade) {
        this.responsabilidade = responsabilidade;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public int getIdTermoSelecionado() {
        return idTermoSelecionado;
    }

    public void setIdTermoSelecionado(int idTermoSelecionado) {
        this.idTermoSelecionado = idTermoSelecionado;
    }

    @Override
    public String toString(){
            return getId() + "-" + getNome();
    }
}

