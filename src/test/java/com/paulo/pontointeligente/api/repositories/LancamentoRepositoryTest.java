package com.paulo.pontointeligente.api.repositories;

import com.paulo.pontointeligente.api.entities.Empresa;
import com.paulo.pontointeligente.api.entities.Funcionario;
import com.paulo.pontointeligente.api.entities.Lancamento;
import com.paulo.pontointeligente.api.enums.PerfilEnum;
import com.paulo.pontointeligente.api.enums.TipoEnum;
import com.paulo.pontointeligente.api.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    LancamentoRepository lancamentoRepository;

    private Long funcionarioid;

    @Before
    public void setUp() {
        Empresa empresa = this.empresaRepository.save(this.obterDadosEmpresa());

        Funcionario funcionario = this.funcionarioRepository.save(this.obterDadosFuncionario(empresa));
        this.funcionarioid = funcionario.getId();

        this.lancamentoRepository.save(this.obterDadosLancamento(funcionario));
        this.lancamentoRepository.save(this.obterDadosLancamento(funcionario));
    }

    @After
    public void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarLancamentosPorFuncionarioId() {
        List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(this.funcionarioid);
        assertEquals(2, lancamentos.size());
    }

    @Test
    public void testBuscarLancamentosPorFuncionarioIdPaginado() {
        PageRequest page = PageRequest.of(0,10);
        Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(this.funcionarioid, page);
        assertEquals(2, lancamentos.getTotalElements());
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Paulo Roberto");
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBcrypt("123456"));
        funcionario.setCpf("39135810491");
        funcionario.setEmail("prmorais_13@hotmail.com");
        funcionario.setEmpresa(empresa);
        return funcionario;
    }

    private Lancamento obterDadosLancamento(Funcionario funcionario) {
        Lancamento lancamento = new Lancamento();
        lancamento.setData(new Date());
        lancamento.setTipoEnum(TipoEnum.INICIO_ALMOÇO);
        lancamento.setFuncionario(funcionario);
        return lancamento;
    }

    private Empresa obterDadosEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setCnpj("08241747000143");
        empresa.setRazaoSocial("Prefeitura");
        return empresa;
    }
    
}