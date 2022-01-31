package com.getready.timecalculator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.getready.beans.Bean_Phase;



public class TimeCalculator implements Runnable {
	
	//initialisation des variables
	public final int startTimeActivite = (LocalTime.now().toSecondOfDay());
	private int indice;
	public ArrayList<String> messageLecteur = new ArrayList<String>();
	private int avancementPhase ; 
	public String nomPhaseEnCours ;
	public float stopTimeLecteur ;
	private boolean running ;
	public int heureDepart ;
	public String nomTableEnCours;
	Thread threadPhase = new Thread(this);
	private List<Bean_Phase> listPhasesUser;
	
	
	@Override
	public void run() {
	
	avancementPhase = 0;	
		
	while(running) {
		
		System.out.println("DANS LE THREAD-----"+listPhasesUser.size());
		while(indice < listPhasesUser.size()) {
			
			
			Bean_Phase beanPhase = listPhasesUser.get(indice);
			
			
			nomPhaseEnCours = beanPhase.getLeNom().toUpperCase();
		

			float stopTimePrevuPhase = (beanPhase.getDureephase()*60) ; 
			float currentTime= 0;
			float startPhase = LocalTime.now().toSecondOfDay() ;
			float avancement = 0;
			
			
			do {		
				if(running) {
					
					currentTime =	(LocalTime.now().toSecondOfDay()) - startPhase;
				
					avancement = (currentTime*100)/stopTimePrevuPhase ;
				
					avancementPhase = (int)avancement;
									
					
				}else {
			
					threadPhase.interrupt();
					avancementPhase = 0;
					
					return;
					}
				
				
			}while(avancement <= 100);
			
			indice ++;
			System.out.println("----PHASE-----"+nomPhaseEnCours+"-----TERMINE------");
			
		}
		
	}
}
	

	public synchronized void setListPhasesUser(List<Bean_Phase> listPhasesUser) {
		
		this.listPhasesUser = listPhasesUser;
		
	}
	
	public synchronized void setIndice(int nouvelIndice) {
		
		this.indice = nouvelIndice;
	}
	
	public synchronized int getIndice() {
		return indice;
	}
	
	public synchronized void startPhaseThread(int indiceStart) {
		
//		setListPhasesUser(listPhasesUser);
		setIndice(indiceStart);
		running = true;	
		threadPhase.start();
		System.out.println("START METHOD - THREAD: "+threadPhase.getName()+"   INDICE:    "+indice+"     STATUT:    "+threadPhase.getState()+"      STATUT_RUNNING:   "+running+"      PHASE-EN-COURS:    "+nomPhaseEnCours);
//		Thread.currentThread().start();
	
		
	}
	
	
	public synchronized void stopPhaseThread() {
				
		running = false ;
		threadPhase.interrupt();
		avancementPhase = 0;	

		System.out.println("STOP METHOD - THREAD PHASE:  INDICE:    "+indice+"     STATUT:    "+threadPhase.getState()+"      STATUT_RUNNING:   "+running+"      PHASE-EN-COURS:    "+nomPhaseEnCours);
		
	}

	public synchronized void phaseChange(int indiceIncremente) {
			
			avancementPhase = 0;
			indice = indice + indiceIncremente;
			System.out.println("CHANGE METHOD 1- THREAD: "+threadPhase.getName()+"   INDICE:    "+indice+"     STATUT:    "+threadPhase.getState()+"      STATUT_RUNNING:   "+running+"      PHASE-EN-COURS:    "+nomPhaseEnCours);
			startPhaseThread(indice);
			
	}
	

	public synchronized int getAvancementPhase() {
			
		return avancementPhase;
	}

	public synchronized String getNomPhaseEnCours() {
		return nomPhaseEnCours;
	}

}
