package com.projects.petshopNew.resources;

import com.projects.petshopNew.dto.AssistanceDTO;
import com.projects.petshopNew.services.AssistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/assistances")
public class AssistanceResource {

    @Autowired
    private AssistanceService service;

    @GetMapping
    public ResponseEntity<List<AssistanceDTO>> findAll() {
        List<AssistanceDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/pageable")
    public ResponseEntity<Page<AssistanceDTO>> findAllPageable(Pageable pageable){
        Page<AssistanceDTO> list = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AssistanceDTO> findById(@PathVariable Long id){
        AssistanceDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<AssistanceDTO> insert(@RequestBody AssistanceDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AssistanceDTO> update(@PathVariable Long id, @RequestBody AssistanceDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AssistanceDTO> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
