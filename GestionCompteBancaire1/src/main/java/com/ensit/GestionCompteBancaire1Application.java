package com.ensit;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.ensit.dao.ClientRepository;
import com.ensit.dao.CompteRepository;
import com.ensit.dao.OperationRepository;
import com.ensit.entities.Client;
import com.ensit.entities.Compte;
import com.ensit.entities.CompteCourant;
import com.ensit.entities.CompteEpargne;
import com.ensit.entities.Retrait;
import com.ensit.entities.Versement;
import com.ensit.metier.IBanqueMetier;




//		---1ere méthode qui ajoute un client à la table client

	/*
	 
	 
	@SpringBootApplication
	public class GestionCompteBancaire1Application  {

		public static void main(String[] args) {
		
		ApplicationContext ctx=SpringApplication.run(GestionCompteBancaire1Application.class, args);
		ClientRepository clientRepository=ctx.getBean(ClientRepository.class);
		clientRepository.save( new Client("amir","amir@gmail.com") );


	}
	
	
	*/
	

	@SpringBootApplication
	public class GestionCompteBancaire1Application implements CommandLineRunner  {
		@Autowired
		private ClientRepository clientRepository;
		@Autowired
		private CompteRepository compteRepository;
		@Autowired
		private OperationRepository operationRepository;
		@Autowired
		private IBanqueMetier banqueMetier;
		public static void main(String[] args) {
		SpringApplication.run(GestionCompteBancaire1Application.class, args);
		
		}

		@Override
		public void run(String... arg0) throws Exception {
			Client c1= clientRepository.save( new Client("hassen","hassen@gmail.com") );
			Client c2= clientRepository.save( new Client("rachid","rachid@gmail.com") );
			
			Compte cp1 = compteRepository.save(new CompteCourant("cp1",new Date(),90000,c1,6000) );
			Compte cp2 = compteRepository.save(new CompteEpargne("cp2",new Date(),6000,c2,5.5) );
			
			operationRepository.save(new Versement(new Date(),9000,cp1));
			operationRepository.save(new Versement(new Date(),6000,cp1));
			operationRepository.save(new Retrait(new Date(),9000,cp1));
			
			operationRepository.save(new Versement(new Date(),2300,cp2));
			operationRepository.save(new Versement(new Date(),3000,cp2));
			operationRepository.save(new Retrait(new Date(),4000,cp2));
			
			
			banqueMetier.verser("cp1", 1111111);
		
		}
	
	
}
