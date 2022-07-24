package repository;

import cursojava.Conta;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ContasRepository {
    Map<Integer, Conta> contasRepository = new TreeMap<>();

    public void salvar(Conta conta){
        this.contasRepository.put(conta.getNumero(), conta);
    }

    public List<Conta> buscarTodas(){
        return contasRepository.values().stream().collect(Collectors.toList());
    }

    public Conta buscaPorNumero(Integer numero){
        return contasRepository.get(numero);
    }

    public void excluir(int id){
        contasRepository.remove(id);
    }
}
