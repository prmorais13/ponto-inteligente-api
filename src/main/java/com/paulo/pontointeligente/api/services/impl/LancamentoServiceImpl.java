package com.paulo.pontointeligente.api.services.impl;

import com.paulo.pontointeligente.api.entities.Lancamento;
import com.paulo.pontointeligente.api.repositories.LancamentoRepository;
import com.paulo.pontointeligente.api.services.LancamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

  private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

  @Autowired
  private LancamentoRepository lancamentoRepository;

  @Override
  public Page<Lancamento> buscarFuncionarioPorId(Long funcionarioId, PageRequest pageRequest) {
    log.info("Buscando lançamentos para o o Funcionário ID {}", funcionarioId);
    return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
  }

  @Override
  public Optional<Lancamento> buscarPorId(Long id) {
    log.info("Buscando um lançamento por ID {}", id);
    return this.lancamentoRepository.findById(id);
  }

  @Override
  public Lancamento persistir(Lancamento lancamento) {
    log.info("Persistindo um lançamento no banco de dados", lancamento);
    return this.lancamentoRepository.save(lancamento);
  }

  @Override
  public void remover(Long id) {
    log.info("Excluindo o lançamento ID {}", id);
    this.lancamentoRepository.deleteById(id);
  }
}
