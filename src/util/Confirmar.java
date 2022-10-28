package util;

import java.util.Scanner;

public class Confirmar {

    //Recebe uma pergunta como parâmetro e valida a resposta do usuário como "s" ou "n"
    public static String confirmarSimNao(String pergunta) {
        Scanner sc = new Scanner(System.in);
        System.out.println(pergunta);
        String confirmar = sc.next();
        sc.nextLine();
        while (!confirmar.equalsIgnoreCase("s") && !confirmar.equalsIgnoreCase("n")) {
            System.out.println("Opção inválida!");
            System.out.println(pergunta);
            confirmar = sc.next();
            sc.nextLine();
        }

        return confirmar;
    }
}
