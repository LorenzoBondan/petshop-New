package com.projects.petshopNew.dto;

import com.projects.petshopNew.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDTO {
    private String cpf;
    private String name;
    private String password; // REMOVER DEPOIS
    private Long cliendId;
    private final List<RoleDTO> roles = new ArrayList<>();

    public UserDTO(){}

    public UserDTO(User entity){
        this.cpf = entity.getCpf();
        this.name = entity.getName();
        this.password = entity.getPassword(); // remover depois
        this.cliendId = entity.getClient().getId();

        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public UserDTO(String cpf, String name, Long cliendId) {
        this.cpf = cpf;
        this.name = name;
        this.cliendId = cliendId;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCliendId() {
        return cliendId;
    }

    public void setCliendId(Long cliendId) {
        this.cliendId = cliendId;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(cpf, userDTO.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
