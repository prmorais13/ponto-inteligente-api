package com.paulo.pontointeligente.api.services.impl;

import com.paulo.pontointeligente.api.entities.Empresa;
import com.paulo.pontointeligente.api.repositories.EmpresaRepository;
import com.paulo.pontointeligente.api.services.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    @Autowired
    EmpresaRepository empresaRepository;

    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        log.info("Buscando uma empresa para o CNPJ {}", cnpj);
        return Optional.ofNullable(this.empresaRepository.findByCnpj(cnpj));
    }

    @Override
    public Empresa persistir(Empresa empresa) {
        log.info("Persistindo uma empresa: {}", empresa);
        return this.empresaRepository.save(empresa);
    }
}