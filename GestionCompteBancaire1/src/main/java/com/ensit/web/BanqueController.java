package com.ensit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ensit.entities.Compte;
import com.ensit.entities.Operation;
import com.ensit.metier.IBanqueMetier;

@Controller
public class BanqueController {
	@Autowired
	private IBanqueMetier banqueMetier;
	@RequestMapping("/operations")
	public String index()
	{
		return "comptes";
	}
	
	
	@RequestMapping("/consulterCompte")
	public String consulter(Model model,String codeCompte,@RequestParam(name="page",defaultValue="0") int page,@RequestParam(name="size",defaultValue="5")   int size)
	{
		model.addAttribute("codeCompte",codeCompte);
		
		try 
		{
		
		Compte cp=banqueMetier.consulterCompte(codeCompte);
		Page<Operation> pageOperation=banqueMetier.listOperation(codeCompte, page, size);
		model.addAttribute("listOperation",pageOperation.getContent());
		int []pages=new int[pageOperation.getTotalPages()];
		model.addAttribute("pages",pages);
		model.addAttribute("compte",cp);
		}
		catch (Exception e) {
			model.addAttribute("exception",e);
		}
		return "comptes";
	}
	@RequestMapping(value="/saveOperation",method=RequestMethod.POST)
	public String saveOperation(Model model,String typeOperation,String codeCompte2,String montant,String codeCompte )
	{
		try
		{
			if(codeCompte.isEmpty() || String.valueOf(montant).isEmpty() ) throw new  RuntimeException("champs vide,vérifier que vous avez rempli tous les champs");
			if (typeOperation.equals("VERS") ) banqueMetier.verser(codeCompte,Double.parseDouble(montant));
			else if(typeOperation.equals("RET")) banqueMetier.retirer(codeCompte, Double.parseDouble(montant));
			else if(typeOperation.equals("VIR") ) {
			//	if( codeCompte2.isEmpty()) throw new  RuntimeException("champs vide,vérifier que vous avez rempli tous les champs");
				banqueMetier.virement(codeCompte, codeCompte2, Double.parseDouble(montant));
			}
			
		}
		catch (Exception e) {
			model.addAttribute("error",e);
			return "redirect:/consulterCompte?codeCompte="+codeCompte+"&error="+e.getMessage();
		}
		
		
		
		return "redirect:/consulterCompte?codeCompte="+codeCompte;
	}
	

}
