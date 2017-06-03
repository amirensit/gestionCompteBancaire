package com.ensit.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensit.entities.Compte;

public interface CompteRepository extends JpaRepository<Compte, String> {

}
