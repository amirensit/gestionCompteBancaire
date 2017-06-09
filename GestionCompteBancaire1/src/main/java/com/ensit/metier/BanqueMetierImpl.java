package com.ensit.metier;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.ensit.dao.CompteRepository;
import com.ensit.dao.OperationRepository;
import com.ensit.entities.Compte;
import com.ensit.entities.CompteCourant;
import com.ensit.entities.Operation;
import com.ensit.entities.Retrait;
import com.ensit.entities.Versement;
@Service
@Transactional
public class BanqueMetierImpl implements IBanqueMetier {
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Override
	public Compte consulterCompte(String codeCpte) {
		Compte cp= compteRepository.findOne(codeCpte);
		if(cp== null) throw new RuntimeException("Compte introuvable");
		return cp;
	}

	@Override
	public void verser(String codeCpte, double montant) {
		Compte cp=consulterCompte(codeCpte);
		Versement v= new Versement(new Date(),montant,cp);
		operationRepository.save(v);
		cp.setSolde(cp.getSolde()+montant);
		compteRepository.save(cp);
		
		
	}

	@Override
	public void retirer(String codeCpte, double montant) {
		
		Compte cp=consulterCompte(codeCpte);
		double faciliteCaisse = 0;
		if(cp instanceof CompteCourant)  // pour tester si c'est une instance de comptecourant ou compteEpargne
			faciliteCaisse=( (CompteCourant) cp ).getDecouvert();
		if(cp.getSolde()+ faciliteCaisse< montant) throw new RuntimeException("solde insuffisant");
		Retrait v= new Retrait(new Date(),montant,cp);
		operationRepository.save(v);
		cp.setSolde(cp.getSolde() - montant);
		compteRepository.save(cp);
		
	}

	@Override
	public void virement(String codeCpte1, String codeCpte2, double montant) {
		if(codeCpte1.equals(codeCpte2)) throw new RuntimeException("impossible,virement sur le même compte");
		retirer(codeCpte1,montant);
		verser(codeCpte2, montant);
		
	}

	@Override
	public Page<Operation> listOperation(String codeCpte, int page, int size) {
		
		return operationRepository.listOperation(codeCpte, new PageRequest(page, size));
	}
	
	

}
