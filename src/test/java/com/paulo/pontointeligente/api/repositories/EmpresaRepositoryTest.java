package com.paulo.pontointeligente.api.repositories;

import com.paulo.pontointeligente.api.entities.Empresa;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {

    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String CNPJ = "08241747000143";

    @Before
    public void setUp() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Prefeitura");
        empresa.setCnpj(CNPJ);
        this.empresaRepository.save(empresa);
    }

    @After
    public void tearDown() throws Exception {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void findByCnpj() throws Exception {
        Empresa empresa = this.empresaRepository.findByCnpj(CNPJ);
        assertEquals(CNPJ, empresa.getCnpj());
    }

}