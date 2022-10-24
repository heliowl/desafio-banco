package model;

import java.util.ArrayList;
import java.util.List;

public class Banco {

    private static final String NOME = "Banco Dev";

    protected String nome;



    public Banco() {
        this.nome = NOME;

    }

//    public String getNome() {
//        return nome;
//    }

    public static String nomeBanco(){
        return NOME;
    }


}
