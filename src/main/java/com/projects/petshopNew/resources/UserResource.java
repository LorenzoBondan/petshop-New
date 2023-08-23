package com.projects.petshopNew.resources;

import com.projects.petshopNew.dto.UserDTO;
import com.projects.petshopNew.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAllPaged(@RequestParam(value = "name", defaultValue = "") String name, Pageable pageable){
        Page<UserDTO> list = service.findAllPaged(name, pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{cpf}")
    public ResponseEntity<UserDTO> findByCpf(@PathVariable String cpf) {
        UserDTO dto = service.findByCpf(cpf);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    private ResponseEntity<UserDTO> insert(@RequestBody UserDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{cpf}")
                .buildAndExpand(dto.getCpf()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{cpf}")
    private ResponseEntity<UserDTO> update(@PathVariable String cpf, @RequestBody UserDTO dto){
        dto = service.update(cpf, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{cpf}")
    public ResponseEntity<UserDTO> delete(@PathVariable String cpf){
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }
}
