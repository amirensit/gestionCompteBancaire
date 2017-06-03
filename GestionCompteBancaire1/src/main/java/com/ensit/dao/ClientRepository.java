package com.ensit.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensit.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
