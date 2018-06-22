package com.paulo.pontointeligente.api.controllers;

import com.paulo.pontointeligente.api.dtos.FuncionarioDto;
import com.paulo.pontointeligente.api.entities.Funcionario;
import com.paulo.pontointeligente.api.response.Response;
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
@RequestMapping("/api/funcionarios")
@CrossOrigin("*")
public class FuncionarioController {

  private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);

  @Autowired
  private FuncionarioService funcionarioService;

  @PutMapping("/{id}")
  public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable  Long id,
          @Valid @RequestBody FuncionarioDto funcionarioDto, BindingResult result) {

    log.info("Atualizando funcionário: {}", funcionarioDto.toString());
    Response<FuncionarioDto> response = new Response<>();

    Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);
    if (!funcionario.isPresent()) {
      result.addError(new ObjectError("funcionario", "Funcionário não encontrado!"));
    }

    this.atualizaDadosFuncionario(funcionario.get(), funcionarioDto, result);

    if (result.hasErrors()) {
      log.error("Erro validando funcionário: {}", result.getAllErrors());
      result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
      ResponseEntity.badRequest().body(response);
    }

    this.funcionarioService.persistir(funcionario.get());
    response.setData(this.converterFuncionarioDto(funcionario.get()));

    return ResponseEntity.ok(response);
  }

  private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {
    FuncionarioDto funcionarioDto = new FuncionarioDto();
    funcionarioDto.setId(funcionario.getId());
    funcionarioDto.setNome(funcionario.getNome());
    funcionarioDto.setEmail(funcionario.getEmail());
    funcionario.getQtdHorasAlmocoOpt()
      .ifPresent(qtdHorasAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
    funcionario.getQtdHorasTrabalhoDiaOpt()
      .ifPresent(qtdHorasTrabDia -> funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
    funcionario.getValorHoraOpt()
      .ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));

    return funcionarioDto;
  }

  private void atualizaDadosFuncionario(Funcionario funcionario,
    FuncionarioDto funcionarioDto, BindingResult result) {

    funcionario.setNome(funcionarioDto.getNome());
    if (!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
      this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
          .ifPresent(func -> result.addError(new ObjectError("email", "E-mail já existente!")));
      funcionario.setEmail(funcionarioDto.getEmail());
    }

    funcionario.setQtdHorasAlmoco(null);
    funcionarioDto.getQtdHorasAlmoco()
        .ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));

    funcionario.setQtdHorasTrabalhoDia(null);
    funcionarioDto.getQtdHorasTrabalhoDia()
        .ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));

    funcionario.setValorHora(null);
    funcionarioDto.getValorHora()
        .ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

    if (funcionarioDto.getSenha().isPresent()) {
      funcionario.setSenha(PasswordUtils.gerarBcrypt(funcionarioDto.getSenha().get()));
    }

  }
}
