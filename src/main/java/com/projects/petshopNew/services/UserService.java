package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.RoleDTO;
import com.projects.petshopNew.dto.UserDTO;
import com.projects.petshopNew.entities.*;
import com.projects.petshopNew.repositories.*;
import com.projects.petshopNew.services.exceptions.DataBaseException;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AssistanceRepository assistanceRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(String name, Pageable pageable){
        Page<User> list = repository.find(name, pageable);
        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findByCpf(String cpf){
        try{
            User entity = repository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("Cpf not found " + cpf));
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Cpf not found");
        }
    }

    @Transactional
    public UserDTO insert(UserDTO dto){
        User entity = new User();
        entity.setCpf(dto.getCpf());

        copyDtoToEntity(dto, entity);

        // ao criar um novo usuário, cria também um cliente, endereço e contato
        Client client = new Client();
        Address address = new Address();
        Contact contact = new Contact();

        client.setName(dto.getName());
        client.setRegisterDate(Instant.now());
        client = clientRepository.save(client);

        address.setClient(client);
        address = addressRepository.save(address);

        contact.setClient(client);
        contact = contactRepository.save(contact);

        entity.setClient(client);

        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(String cpf, UserDTO dto){
        try{
            User entity = repository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("Cpf not found " + cpf));
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Cpf not found");
        }
    }

    @Transactional
    public void delete(String cpf) {
        try {
            // delete contact, address, assistances, pets and contact from user
            User user = repository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("Cpf not found " + cpf));
            addressRepository.deleteById(user.getClient().getAddress().getId());
            contactRepository.deleteById(user.getClient().getContact().getId());
            for(Pet pet : user.getClient().getPets()){
                for(Assistance assistance : pet.getAssistances()){
                    assistanceRepository.deleteById(assistance.getId());
                }
                petRepository.deleteById(pet.getId());
            }
            clientRepository.deleteById(user.getClient().getId());
            repository.deleteByCpf(cpf);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Cpf not found " + cpf);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity Violation");
        }
    }

    public void copyDtoToEntity(UserDTO dto, User entity){
        entity.setCpf(dto.getCpf());
        entity.setName(dto.getName());

        entity.setPassword(dto.getPassword()); // remover depois

        if(dto.getCliendId() != null){
            entity.setClient(clientRepository.getReferenceById(dto.getCliendId()));
        }

        entity.getRoles().clear();
        for(RoleDTO roleDTO : dto.getRoles()){
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }
}
