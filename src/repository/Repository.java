package repository;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Repository<I, T> {

    Map<I,T> repository = new TreeMap<>();

    public List<T> buscarTodos(){

        return repository.values().stream().collect(Collectors.toList());
    }

    public T buscarPorId(I id){

        return repository.get(id);
    }

    public void salvar(I id, T objeto){

        repository.put(id, objeto);
    }

    public void excluir(I id){

        repository.remove(id);
    }
}
