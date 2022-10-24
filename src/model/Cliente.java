package model;

public class Cliente {

    private int id;
    private String nome;
    private int SEQUENCIAL = 1;

    private String senha;
    private Conta conta;

    public Cliente(String nome, String senha) {
        this.nome = nome;
        this.id = SEQUENCIAL++;
        this.conta = conta;
        this.senha = senha;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", SEQUENCIAL=" + SEQUENCIAL +
                ", senha='" + senha + '\'' +
                ", conta=" + conta +
                '}';
    }
}
