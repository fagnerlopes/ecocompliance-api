package model;

public class Contexto {

    public static String token;
    public static int status;
    public static String corpo = "";
    public static String location;
    public static Long empresaId;
    public static String recursoCaminho;
    public static String ultimoJson;

    private Contexto() {
    }

    public static void limpar() {
        token = null;
        status = 0;
        corpo = "";
        location = null;
        empresaId = null;
        recursoCaminho = null;
        ultimoJson = null;
    }
}
