package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormataData {
    //Formato da data do cadastro e da atualização
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    //Imprime data do cadastro e da atualização no formato acima
    public static String dataHora(LocalDateTime data){
        return data.format(formatter);
    }
}
