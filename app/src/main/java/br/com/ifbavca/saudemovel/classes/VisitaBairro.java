package br.com.ifbavca.saudemovel.classes;

/**
 * Created by Cleiton on 17/10/2015.
 */
public class VisitaBairro {

    private String nome;
    private int numero;

    public VisitaBairro(String nome, int numero){
        this.nome = nome;
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
