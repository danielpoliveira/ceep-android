package br.com.alura.ceep.model;

import java.util.Arrays;
import java.util.List;

public enum Cor {
    AZUL("#408EC9"),
    BRANCO("#FFFFFF"),
    VERMELHO("#EC2F4B"),
    VERDE("#9ACD32"),
    AMARELO("#F9F256"),
    LILAS("#F1CBFF"),
    CINZA("#D2D4DC"),
    MARROM("#A47C48"),
    ROXO("#BE29EC");

    private final String corString;

    Cor(String corString) {
        this.corString = corString;
    }

    public static Cor toCor(String corString) {
        for (Cor cor : Cor.values()) {
            if (cor.corString.equalsIgnoreCase(corString))
                return cor;
        }

        return BRANCO;
    }

    public static List<Cor> getAll() { return Arrays.asList(values()); }

    public String getCorString() {
        return this.corString;
    }

}
