package com.projects.petshopNew.repositories;

import com.projects.petshopNew.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCpf(String cpf);

    @Query("SELECT DISTINCT obj FROM User obj " +
            "WHERE (UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%')) ) ORDER BY obj.name")
    Page<User> find(String name, Pageable pageable);

    void deleteByCpf(String cpf);
}
