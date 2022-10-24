package util;

import java.text.DecimalFormat;

public class ImprimeValores {

    public static String valorFinanceiro(double valor){
        String valorFormatado = new DecimalFormat("#,##0.00").format(valor);
        return valorFormatado;
    }
}
