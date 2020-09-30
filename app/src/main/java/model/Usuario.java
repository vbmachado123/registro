package model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import util.ConfiguracaoFirebase;

/**
 * Created by victor on 02/02/20.
 */

public class Usuario {

    private int id;
    private int idEmpresa;
    private int idImagem;
    private String nome;
    private String email;
    private String senha;

    public Usuario(){}

    public void salvar(){
        DatabaseReference refenciaFirebase = ConfiguracaoFirebase.getFirebase();
        refenciaFirebase.child("usuarios").child(String.valueOf(getId())).setValue(this);
    }

    @Exclude
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdImagem() {
        return idImagem;
    }

    public void setIdImagem(int idImagem) {
        this.idImagem = idImagem;
    }
}
