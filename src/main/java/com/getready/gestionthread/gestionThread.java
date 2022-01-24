package com.getready.gestionthread;

import com.getready.avancementTot.AvancementTotal;
import com.getready.timecalculator.TimeCalculator;

public class gestionThread {

	private int avancePhase;
	
	
	TimeCalculator timeCalculator = new TimeCalculator();
	AvancementTotal avanceTot = new AvancementTotal();
	
	
	public synchronized void startThreads(String nomTable) {
				
		timeCalculator.indice = 0;
		timeCalculator.startPhaseThread(nomTable);	
		
		//Lancement AVCEMENT TOTAL
		avanceTot.startAvanceTotThread(nomTable);
		
	}
	
	public synchronized int getAvancePhase() {
		avancePhase = timeCalculator.avancementPhase;
		
		return this.avancePhase;
		
	}
	
	public synchronized void stopThreads(int indice, String nomTable) {
		
		//arrêt des threads----------------------------------------------------------------------------------------------------------	
		timeCalculator.stopPhaseThread(0, nomTable);
		avanceTot.stopAvanceTotThread();
		
		timeCalculator = null;
		avanceTot = null;
	}
	
	
	
}
