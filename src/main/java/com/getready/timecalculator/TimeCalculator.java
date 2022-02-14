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
	
	
	
	
	//THREAD DE CALCUL D'AVANCEMENT DU TEMPS PAR PHASE:
	////IL RECOIT LA LISTE DES BEANS ISSUS DE LA DB VIA LEUR IMPORTATION DEPUIS LA SESSION PAR LA SERVLET - VOIR LA METHODE DU SETTER DE LA LIST
	////L'INDICE CORRESPOND AU RANG DU BEAN STOCK2 DANS LA LIST
	////POUR CHAQUE INDICE DE LA LISTE IL IMPORTE LE BEAN ET GARDE LES VALEURS DE SES PROPRIETES - 1 BEAN = 1 PHASE - UNE BOUCLE DE CALCUL TEMPS ACTUEL vs FIN DE PHASE EST LANCEE, 
	////A CHAQUE FIN DE BOUCLE, QUAND LE TEMPS EST ECOULE IL INCREMENTE L'INDICE ET RELANCE LA BOUCLE AVEC LE BEAN SUIVANT
	@Override
	public void run() {
	
	avancementPhase = 0;	
		
	while(running) {
		
		
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
					running = false;
					return;
					}
				
				
			}while(avancement <= 100);
			
			indice ++;
			
		}
		
	}
}
	
	//SETTER DE LA LISTE DE BEAN_PHASE, EN VARIABLE DE CLASSE - APPELE PAR LA SERVLET AFFICHAGE AVANCEMENT PHASE
	public synchronized void setListPhasesUser(List<Bean_Phase> listPhasesUser) {
		
		this.listPhasesUser = listPhasesUser;
		
	}
	
	
	//SETTER DE L'INDICE EN VARIABLE DE CLASSE - APPELE PAR LA SERVLET AFFICHAGE AVANCEMENT PHASE
	public synchronized void setIndice(int nouvelIndice) {
		
		this.indice = nouvelIndice;
	}
	
	
	//GETTER DE L'INDICE EN VARIABLE DE CLASSE - APPELE PAR LA SERVLET AFFICHAGE AVANCEMENT PHASE
	public synchronized int getIndice() {
		return indice;
	}
	
	
	//COMMANDE START DU THREAD ET INITIALISATION DES VARIABLES INDICE ET RUNNING - APPELE PAR LA SERVLET AFFICHAGE AVANCEMENT PHASE
	public synchronized void startPhaseThread(int indiceStart) {
		
		setIndice(indiceStart);
		running = true;	
		threadPhase.start();
	
		
	}
	
	//COMMANDE STOP DU THREAD ET REINITIALISATION DES VARIABLES AVANCEMENT PHASE ET RUNNING - APPELE PAR LA SERVLET AFFICHAGE AVANCEMENT PHASE
	public synchronized void stopPhaseThread() {
				
		running = false ;
		threadPhase.interrupt();
		avancementPhase = 0;	

	}

	//COMMANDE DE CHANGEMENT DE PHASE, DANS LE SENS DE L'INDICE INCREMENTE: +/- -APPELE PAR LA SERVLET AFFICHAGE AVANCEMENT PHASE
	public synchronized void phaseChange(int indiceIncremente) {
			
			avancementPhase = 0;
			indice = indice + indiceIncremente;
			startPhaseThread(indice);
			
	}
	
	//GETTER DE LA VARIABLE D'AVANCEMENT DE LA PHASE EN COURS - APPELE PAR LA SERVLET AFFICHAGEAVANCEMENTPHASE
	public synchronized int getAvancementPhase() {
			
		return avancementPhase;
	}
	//GETTER DE LA VARIABLE DU NOM DE LA PAHSE EN COURS - APPELE PAR LA SERVLET AFFICHAGEAVANCEMENTPHASE
	public synchronized String getNomPhaseEnCours() {
		return nomPhaseEnCours;
	}

}
