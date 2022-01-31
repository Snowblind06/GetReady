package com.getready.userbean;

import java.time.LocalTime;

public class userbean {

	private String id ;
	private String email ;
	private String password ;
	private String activite ;
	private LocalTime creation;
	private String nomTableUser;
	private int avancementPhaseBean;
	private int avancementTotalBean;
	private String phaseEnCoursBean;
	private int indiceEnCours;
	
	public userbean() {
	
	}
	
	public void setIndiceEnCours(int indice) {
		
		this.indiceEnCours = indice;
	}
	
	public int getIndiceEnCours() {
		return indiceEnCours;
	}
	
	public int getAvancementPhaseBean() {
		return avancementPhaseBean;
	}


	public void setAvancementPhaseBean(int avancementPhase) {
		this.avancementPhaseBean = avancementPhase;
	}


	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPassword() {
		
		return password;	
	}
	
	 public void setPassword(String password) {
		 this.password = password;
	 }
	 
	 public String getActivite() {
		 return activite; 
	 }
	 
	 public void setActivite(String activite) {
		 this.activite = activite;
	 }
	 
	 public LocalTime creation() {
		 return creation;
	 }
	 
	 public void setCreation(LocalTime creation) {
		 this.creation = creation;
	 }


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getNomTableUser() {
		return nomTableUser;
	}


	public void setNomTableUser(String nomTableUser) {
		this.nomTableUser = nomTableUser;
	}


	public int getAvancementTotalBean() {
		return avancementTotalBean;
	}


	public void setAvancementTotalBean(int avancementTotal) {
		this.avancementTotalBean = avancementTotal;
	}


	public String getPhaseEnCoursBean() {
		return phaseEnCoursBean;
	}


	public void setPhaseEnCoursBean(String phaseEnCours) {
		this.phaseEnCoursBean = phaseEnCours;
	}
	 
	 
	 
}
