package com.paulo.pontointeligente.api.controllers;

import com.paulo.pontointeligente.api.dtos.EmpresaDto;
import com.paulo.pontointeligente.api.entities.Empresa;
import com.paulo.pontointeligente.api.response.Response;
import com.paulo.pontointeligente.api.services.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin("*")
public class EmpresaController {

  private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

  @Autowired
  private EmpresaService empresaService;

  @GetMapping("/cnpj/{cnpj}")
  private ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(@PathVariable String cnpj) {
    log.info("Buscando empresa por CNPJ: {}", cnpj);
    Response<EmpresaDto> response = new Response<>();
    Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cnpj);

    if (!empresa.isPresent()) {
      log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
      response.getErrors().add("Empresa não encontrada para o CNPJ " + cnpj);
      return ResponseEntity.badRequest().body(response);
    }

    response.setData(this.converteEmpresaParaDto(empresa.get()));
    return ResponseEntity.ok(response);
  }

  private EmpresaDto converteEmpresaParaDto(Empresa empresa) {
    EmpresaDto empresaDto = new EmpresaDto();
    empresaDto.setId(empresa.getId());
    empresaDto.setRazaoSocial(empresa.getRazaoSocial());
    empresaDto.setCnpj(empresa.getCnpj());

    return empresaDto;
  }

}
