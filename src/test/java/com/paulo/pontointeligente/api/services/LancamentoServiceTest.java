package com.paulo.pontointeligente.api.services;

import com.paulo.pontointeligente.api.entities.Empresa;
import com.paulo.pontointeligente.api.entities.Funcionario;
import com.paulo.pontointeligente.api.entities.Lancamento;
import com.paulo.pontointeligente.api.enums.PerfilEnum;
import com.paulo.pontointeligente.api.enums.TipoEnum;
import com.paulo.pontointeligente.api.repositories.LancamentoRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceTest {

  @Autowired
  private LancamentoRepository lancamentoRepository;

  @Autowired
  private LancamentoService lancamentoService;

  @Autowired
  EmpresaService empresaService;

  @Autowired
  FuncionarioService funcionarioService;

  private Lancamento lancamento;
  private Funcionario func;
  private Empresa empresa;

  @Before
  public void setUp() throws Exception {
/*    BDDMockito.given(this.lancamentoRepository.save(Mockito.any(Lancamento.class))).willReturn(new Lancamento());
    BDDMockito
        .given(this.lancamentoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
        .willReturn(new PageImpl<>(new ArrayList<>()));

    BDDMockito.given(this.lancamentoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Lancamento()));*/
  this.empresa = new Empresa();
  empresa.setCnpj("08241747000143");
  empresa.setRazaoSocial("Prefeitura");
  empresa.setDataAtualizacao(new Date());
  empresa.setDataCriacao(new Date());
  empresa.setFuncionarios(new ArrayList<>());
  this.empresaService.persistir(empresa);


  this.func = new Funcionario();
  func.setCpf("39135810491");
  func.setEmail("prmorais@hotmail.com");
  func.setEmpresa(this.empresa);
  func.setNome("Paulo");
  func.setPerfil(PerfilEnum.ROLE_USUARIO);
  func.setSenha("123456");
  func.setDataAtualizacao(new Date());
  func.setDataCriacao(new Date());
  func.setLancamentos(new ArrayList<>());
  func.setQtdHorasAlmoco(10);
  func.setQtdHorasTrabalhoDia(5);
  func.setValorHora(new BigDecimal(10.00));
  this.funcionarioService.persistir(this.func);


  this.lancamento = new Lancamento();
  lancamento.setData(new Date());
  lancamento.setFuncionario(this.func);
  lancamento.setTipoEnum(TipoEnum.INICIO_ALMOÇO);
  lancamento.setDataAtualizacao(new Date());
  lancamento.setDataCriacao(new Date());
  lancamento.setDescricao("Descrição");
  lancamento.setLocalizacao("localização");
  // this.lancamentoService.persistir(this.lancamento);

  }

/*  @After
  public void tearDown() throws Exception {
  }*/

  @Test
  public void testBuscarLancamentoPorFuncionarioId() {
    Page<Lancamento> lancamento;
    lancamento = this.lancamentoService.buscarFuncionarioPorId(1L, PageRequest.of(0, 10));
    assertNotNull(lancamento);
  }

  @Test
  public void testBuscarLancamentoPorId() {
    Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(1L);
    assertTrue(lancamento.isPresent());
  }

  @Test
  public void testPersistirLancamento() {
    Lancamento lancamento = this.lancamentoService.persistir(this.lancamento);
    assertNotNull(lancamento);
  }

}