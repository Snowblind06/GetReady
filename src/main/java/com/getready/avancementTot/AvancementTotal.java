package com.getready.avancementTot;

import java.time.LocalTime;
import java.util.List;

import com.getready.beans.Bean_Phase;

public class AvancementTotal implements Runnable {

	
	//initialisation des variables
	private int startTimeActivite ;
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
				System.out.println("----------DERNIER BEAN-------------"+dernierBean.getLeNom()+"-----------FIN A ---------------"+dernierBean.getFin());
			}			
			
			heureLimite = heureFin - startTimeActivite;

		return heureLimite;	
	}

public synchronized void startAvanceTotThread(List<Bean_Phase>listPhasesUserBean) {
	
	this.listPhasesUser = listPhasesUserBean;
	running = true;
	
	startTimeActivite = (LocalTime.now().toSecondOfDay());
	
	Thread avanceThread = new Thread(this);
	heureLimite();
	avanceThread.start();
	
	
}

public synchronized void stopAvanceTotThread() {
	
	running = false;
	

	}



public synchronized int getAvancementTotal() {
	
	return avancementTotal;
}
	
}
