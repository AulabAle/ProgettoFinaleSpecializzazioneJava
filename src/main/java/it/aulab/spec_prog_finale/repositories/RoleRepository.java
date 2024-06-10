package it.aulab.spec_prog_finale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.spec_prog_finale.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}