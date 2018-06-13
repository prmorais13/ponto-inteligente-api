package com.paulo.pontointeligente.api.services;

import com.paulo.pontointeligente.api.entities.Empresa;

import java.util.Optional;

public interface EmpresaService {
    /**
     * Retorna uma empresa dado o CNPJ
     * @param cnpj
     * @return
     */
    Optional<Empresa> buscarPorCnpj(String cnpj);


    /**
     * Cadastra uma nova empresa na base de dados
     * @param empresa
     * @return
     */
    Empresa persistir(Empresa empresa);
}
