package model;

import lombok.Data;

@Data
public class EmpresaModel {
    private String nome;
    private String cnpj;
    private String setor;
    private String emailResponsavel;
}
