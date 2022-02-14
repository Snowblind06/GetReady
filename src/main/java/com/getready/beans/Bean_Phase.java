package com.getready.beans;

import java.io.Serializable;
import java.time.LocalTime;


public class Bean_Phase implements Serializable {
	
	

	private static final long serialVersionUID = 1L;

	private int indice;
	private String leNom;	
	private LocalTime debut ; 
	private LocalTime fin ;
	private int dureephase;
	private String activiteName;
	
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	public String getLeNom() {
		return leNom;
	}
	public void setLeNom(String leNom) {
		this.leNom = leNom;
	}
	public LocalTime getDebut() {
		return debut;
	}
	public void setDebut(LocalTime debut) {
		this.debut = debut;
	}
	public LocalTime getFin() {
		return fin;
	}
	public void setFin(LocalTime fin) {
		this.fin = fin;
	}
	public int getDureephase() {
		return dureephase;
	}
	public void setDureephase(int dureePhase) {
		this.dureephase = dureePhase;
	}
	public String getActiviteName() {
		return activiteName;
	}
	public void setActiviteName(String activiteName) {
		this.activiteName = activiteName;
	}

}