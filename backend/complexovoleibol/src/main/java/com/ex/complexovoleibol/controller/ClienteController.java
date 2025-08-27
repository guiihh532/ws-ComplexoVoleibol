package com.ex.complexovoleibol.controller;

import com.ex.complexovoleibol.model.Cliente;
import com.ex.complexovoleibol.repository.ClienteRepository;
import com.ex.complexovoleibol.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Importe esta classe

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    // Método para salvar um cliente (POST)
    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente) {
        try {
            cliente.setId(sequenceGeneratorService.generateSequence(Cliente.SEQUENCE_NAME));
            // O campo 'ativo' é true por padrão (já definido no modelo Cliente)
            Cliente novoCliente = clienteRepository.save(cliente);
            return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Método para listar apenas clientes ativos (GET)
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();

            // Filtra a lista para incluir apenas clientes ativos
            List<Cliente> clientesAtivos = clientes.stream()
                .filter(Cliente::isAtivo)
                .collect(Collectors.toList());

            if (clientesAtivos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(clientesAtivos);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        // Retorna o cliente apenas se ele existir E estiver ativo
        if (cliente.isPresent() && cliente.get().isAtivo()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable long id, @RequestBody Cliente clienteAtualizado) {
        Optional<Cliente> clienteData = clienteRepository.findById(id);

        // Procede com a atualização apenas se o cliente existir
        if (clienteData.isPresent()) {
            Cliente clienteExistente = clienteData.get();
            clienteExistente.setNome(clienteAtualizado.getNome());
            clienteExistente.setEmail(clienteAtualizado.getEmail());
            // ... (o restante da sua lógica de atualização)
            return new ResponseEntity<>(clienteRepository.save(clienteExistente), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Método para "excluir" o cliente de forma lógica (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletarCliente(@PathVariable long id) {
        try {
            Optional<Cliente> clienteData = clienteRepository.findById(id);
            if (clienteData.isPresent()) {
                Cliente cliente = clienteData.get();
                cliente.setAtivo(false); // Define o status como inativo
                clienteRepository.save(cliente); // Salva a alteração
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // NOVO: Endpoint para listar usuários inativos
    @GetMapping("/inativos")
    public ResponseEntity<List<Cliente>> listarClientesInativos() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();
            List<Cliente> clientesInativos = clientes.stream()
                .filter(cliente -> !cliente.isAtivo())
                .collect(Collectors.toList());

            if (clientesInativos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(clientesInativos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
