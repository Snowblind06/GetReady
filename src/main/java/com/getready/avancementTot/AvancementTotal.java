package com.getready.avancementTot;

import java.time.LocalTime;
import java.util.List;

import com.getready.beans.Bean_Phase;

public class AvancementTotal implements Runnable {

	
	//initialisation des variables
	private final int startTimeActivite = (LocalTime.now().toSecondOfDay());
	public int indice;
	public float stopTimeLecteur ;
	private boolean running ;
	public int avancementTotal;
	private int heureFin;
	private int heureLimite ;
	public String tableUser;
	private List<Bean_Phase> listPhasesUser ; 
	
@Override
public void run() {
	
	
	
	while(running) {
	
		while(avancementTotal <100 || avancementTotal > 0) {
			
			if(running && avancementTotal <100) {
			
				int currentTime = LocalTime.now().toSecondOfDay() - startTimeActivite ;
				avancementTotal =  (int)(currentTime*100) / heureLimite;	
				System.out.println("/////////////AVANEMENT TOTAL\\\\\\\\\\\\\\"+avancementTotal+"//////////////HEURE LIMITE\\\\\\\\\\\\\\"+heureLimite);
				}			
			
			else {
				
				stopAvanceTotThread();
				avancementTotal = 0;
				return;
				}
						
		}
		
		stopAvanceTotThread();
		avancementTotal = 0;
		return;
	}
	
	if(Thread.currentThread().isInterrupted()) {
		avancementTotal = 0;
		return;
	}
	
}
	

	
//CALCUL DUREE TOTALE ACTIVITE
private synchronized int heureLimite() {
	
			
			Bean_Phase dernierBean;
	
			if(listPhasesUser != null && !listPhasesUser.isEmpty()) {
				
				dernierBean = listPhasesUser.get(listPhasesUser.size()-1);
				heureFin = dernierBean.getFin().toSecondOfDay();
				System.out.println("/////////////HEURE FIN\\\\\\\\\\\\"+dernierBean.getFin());
			}
						
			
			heureLimite = heureFin - startTimeActivite;
			System.out.println("/////////////HEURE LIMITE\\\\\\\\\\\\"+heureLimite);

		return heureLimite;	
	}

public synchronized void startAvanceTotThread(List<Bean_Phase>listPhasesUserBean) {
	
	this.listPhasesUser = listPhasesUserBean;
	running = true;
	
	Thread avanceThread = new Thread(this);
	heureLimite();
	avanceThread.start();
	
	System.out.println("START THREAD AVANCEMENT TOTAL:------- RUNNING:   "+running+"        NOM TABLE USER:     "+tableUser);
	
}

public synchronized void stopAvanceTotThread() {
	
	running = false;
	
	
	System.out.println("STOP THREAD AVANCEMENT TOTAL:-------  RUNNING:   "+running+"        NOM TABLE USER:     "+tableUser);
	}



public synchronized int getAvancementTotal() {
	
	return avancementTotal;
}
	
}
