package com.paulo.pontointeligente.api.services.impl;

import com.paulo.pontointeligente.api.entities.Funcionario;
import com.paulo.pontointeligente.api.repositories.FuncionarioRepository;
import com.paulo.pontointeligente.api.services.FuncionarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {
  private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);

  @Autowired
  private FuncionarioRepository funcionarioRepository;

  @Override
  public Funcionario persistir(Funcionario funcionario) {
    log.info("Persistindo funcionário: {}", funcionario);
    return this.funcionarioRepository.save(funcionario);
  }

  @Override
  public Optional<Funcionario> buscarPorCpf(String cpf) {
    log.info("Buscando funcionário por CPF {}", cpf);
    return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
  }

  @Override
  public Optional<Funcionario> buscarPorEmail(String email) {
    log.info("Buscando funcionário por email {}", email);
    return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
  }

  @Override
  public Optional<Funcionario> buscarPorId(Long id) {
    log.info("Buscando funcionário por ID {}", id);
    return this.funcionarioRepository.findById(id);
  }
}
