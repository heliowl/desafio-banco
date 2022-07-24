package cursojava;

import repository.ContasRepository;

import java.util.List;

public class Banco {

    //private static final String NOME = "Banco Dev";


    protected String nome;
    //private Cliente cliente;
    protected Conta conta;
    private ContasRepository contasRepository = new ContasRepository();



    public Banco(String nome) {
        this.nome = nome;

    }

    public String getNome() {
        return nome;
    }


    public void salvarContas(Conta conta){
        contasRepository.salvar(conta);
    }

    public void mostrarTodas() {
        List<Conta> contas = contasRepository.buscarTodas();
        contas.forEach(c -> System.out.println(c));
    }


}
