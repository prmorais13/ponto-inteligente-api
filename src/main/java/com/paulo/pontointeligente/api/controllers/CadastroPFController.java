package com.paulo.pontointeligente.api.controllers;

import com.paulo.pontointeligente.api.dtos.CadastroPFDto;
import com.paulo.pontointeligente.api.dtos.CadastroPJDto;
import com.paulo.pontointeligente.api.entities.Empresa;
import com.paulo.pontointeligente.api.entities.Funcionario;
import com.paulo.pontointeligente.api.enums.PerfilEnum;
import com.paulo.pontointeligente.api.response.Response;
import com.paulo.pontointeligente.api.services.EmpresaService;
import com.paulo.pontointeligente.api.services.FuncionarioService;
import com.paulo.pontointeligente.api.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

  private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);

  @Autowired
  private FuncionarioService funcionarioService;

  @Autowired
  private EmpresaService empresaService;

  @PostMapping
  public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto,
                                                           BindingResult result) {
    log.info("Cadastrando PF: {}", cadastroPFDto.toString());
    Response<CadastroPFDto> response = new Response<CadastroPFDto>();

    validarDadosExistentes(cadastroPFDto, result);
    Funcionario funcionario = this.converteDtoParaFuncionario(cadastroPFDto, result);

    if (result.hasErrors()) {
      log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
      result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
      return ResponseEntity.badRequest().body(response);
    }

    Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
    empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
    this.funcionarioService.persistir(funcionario);

    response.setData(this.converteCadastroPFDto(funcionario));
    return ResponseEntity.ok(response);
  }

  private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
    Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
      if (!empresa.isPresent()) {
        result.addError(new ObjectError("Empresa", "Empresa não cadastrada!"));
      }

    this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
        .ifPresent(func -> result.addError(new ObjectError("Funcionário", "CPF já existente")));

    this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
        .ifPresent(func -> result.addError(new ObjectError("Funcionário", "E-mail já existente")));
  }

  private Funcionario converteDtoParaFuncionario(CadastroPFDto cadastroPFDto, BindingResult result) {
    Funcionario funcionario = new Funcionario();
    funcionario.setNome(cadastroPFDto.getNome());
    funcionario.setEmail(cadastroPFDto.getEmail());
    funcionario.setCpf(cadastroPFDto.getCpf());
    funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
    funcionario.setSenha(PasswordUtils.gerarBcrypt(cadastroPFDto.getSenha()));
    cadastroPFDto.getQtdHorasAlmoco()
        .ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
    cadastroPFDto.getQtdHorasTrabalhoDia()
        .ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));
    cadastroPFDto.getValorHora()
        .ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

    return funcionario;
  }

  private CadastroPFDto converteCadastroPFDto(Funcionario funcionario) {
    CadastroPFDto cadastroPFDto = new CadastroPFDto();
    cadastroPFDto.setId(funcionario.getId());
    cadastroPFDto.setNome(funcionario.getNome());
    cadastroPFDto.setEmail(funcionario.getEmail());
    cadastroPFDto.setCpf(funcionario.getCpf());
    cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());
    funcionario.getQtdHorasAlmocoOpt()
        .ifPresent(qtdHorasAlmoço -> cadastroPFDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoço))));
    funcionario.getQtdHorasTrabalhoDiaOpt()
        .ifPresent(qtdHorasTrabDia -> cadastroPFDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
    funcionario.getValorHoraOpt()
        .ifPresent(valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString())));

    return cadastroPFDto;
  }

}
