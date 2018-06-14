package com.paulo.pontointeligente.api.services;

import com.paulo.pontointeligente.api.entities.Funcionario;
import com.paulo.pontointeligente.api.repositories.FuncionarioRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {

  @MockBean
  private FuncionarioRepository funcionarioRepository;

  @Autowired
  private FuncionarioService funcionarioService;

  @Before
  public void setUp() throws Exception {
    BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
    BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Funcionario()));
    BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
    BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testPersistirFuncionario() {
    Funcionario funcionario = this.funcionarioService.persistir(new Funcionario());
    assertNotNull(funcionario);
  }

  @Test
  public void testBuscarFuncionarioPorCpf() {
    Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf("39135810491");
    assertTrue(funcionario.isPresent());
  }

  @Test
  public void testBuscarFuncionarioPorEmail() {
    Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail("prmorais_13@hotmail.com");
    assertTrue(funcionario.isPresent());
  }

  @Test
  public void testBuscarFuncionarioPorId() {
    Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(1L);
    assertTrue(funcionario.isPresent());
  }
}