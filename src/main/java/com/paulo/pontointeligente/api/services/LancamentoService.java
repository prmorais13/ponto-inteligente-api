package com.paulo.pontointeligente.api.services;

import com.paulo.pontointeligente.api.entities.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;


public interface LancamentoService {

  Page<Lancamento> buscarFuncionarioPorId(Long funcionarioId, PageRequest pageRequest);

  Optional<Lancamento> buscarPorId(Long id);

  Lancamento persistir(Lancamento lancamento);

  void remover(Long id);
}
