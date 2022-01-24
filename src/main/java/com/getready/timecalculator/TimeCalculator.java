package com.getready.timecalculator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import com.getready.beans.Bean_Time;
import com.getready.connexion.Connexion;



public class TimeCalculator implements Runnable {
	
	//initialisation des variables
	public final int startTimeActivite = (LocalTime.now().toSecondOfDay());
	public int indice;
	public ArrayList<String> messageLecteur = new ArrayList<String>();
	private int avancementPhase ; 
	public String nomPhaseEnCours ;
	public float stopTimeLecteur ;
	private boolean running ;
	public int heureDepart ;
	public String nomTableEnCours;
	
	
	
	@Override
	public void run() {
		
	while(running) {
		
		
		String nomphase = null; 
		
		Connection con = Connexion.loadDatabase();
		//loadDatabase();
		
		//on parcours la base de donn�es et on stocke les entr�es dans la variable resultat
		try {
			
			Statement statement = null;
			ResultSet resultat = null;	
			statement = con.createStatement();
			
			
			if(indice == 0) {
				indice = 1;
			}
			
			String commandSQL = "SELECT nomPhase, dureephase FROM `"+nomTableEnCours+"` WHERE indice = '"+indice+"' ;";
			System.out.println("----------------------DEBUT THREAD-----------------------"+"/n"+"NOM TABLE EN COURS=   "+nomTableEnCours);
			
			//Lecture d'une entr�e index�e de la BD par appel de la m�thode
			resultat = statement.executeQuery(commandSQL);
			
			if(resultat.next() == true){	
				
				nomphase = resultat.getString("nomPhase");
				int duree = resultat.getInt("dureephase");
				
				
				//enregistrement des valeurs des variables/issues de la BD dans les propri�t�s du bean
				Bean_Time phaseRun = new Bean_Time();
					
				phaseRun.setNomPhase(nomphase);
				phaseRun.setIndice(indice);
				phaseRun.setDuree(duree);
				
				nomPhaseEnCours = nomphase.toUpperCase();
				
				float stopTimePrevuPhase = (phaseRun.getDuree()*60) ; 
				float currentTime= 0;
				float startPhase = LocalTime.now().toSecondOfDay() ;
				float avancement = 0;
				avancementPhase = 0;
				
				
				do {		
					if(running) {
						
						currentTime =	(LocalTime.now().toSecondOfDay()) - startPhase;
					
						avancement = (currentTime*100)/stopTimePrevuPhase ;
					
						avancementPhase = (int)avancement;
						getAvancementPhase();
						
//						System.out.println("--------------AVANCEMENT "+ nomPhaseEnCours + ":       "+ getAvancementPhase());
						
					}else {

						avancementPhase = 0;
						Thread.currentThread().interrupt();
						return;
					}
					
					
				}while(avancement <= 100);
				

				avancementPhase = 0;
				
				indice = indice + 1;
				
				if(running) {
					
					
					run();		
					}
				else {
					Thread.currentThread().interrupt();
					avancementPhase = 0;
					
				}
				
				}else {
					
					avancementPhase = 0;
					
					stopPhaseThread(indice, nomPhaseEnCours);
					
					return;
					}
					
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}if(Thread.currentThread().isInterrupted()) {

			return;
		}
	}
		
	public void startPhaseThread(String nomTable) {
		
		synchronized(nomTable) {
			
		Thread threadPhase = new Thread(this);
		nomTableEnCours = nomTable;
		running = true;		
		
		threadPhase.start();
		
		System.out.println("START METHOD - THREAD PHASE:  INDICE:    "+indice+"     STATUT:    "+threadPhase.getState()+"      RUNNING:   "+running+"      NOM-TABLE-EN-COURS:    "+nomTableEnCours+"----NOM TABLE:----"+nomTable);
		}
	}
	
	
	public void stopPhaseThread(int rang, String nomTable) {
		
		synchronized(nomTable) {
		nomTableEnCours = nomTable;
		running = false ;
		indice = indice + rang ;
		avancementPhase = 0;	

		System.out.println("STOP METHOD - THREAD PHASE:   INDICE:  "+indice+"    RUNNING:   "+running+"      NOM-TABLE-EN-COURS:    "+nomTable+"----NOM TABLE:----"+nomTable);
		}
	}

	public void phaseChange(String nomTable) {
			
			running = true;			
			avancementPhase = 0;
			
			
			startPhaseThread(nomTable);
		
	}
	

	public synchronized int getAvancementPhase() {
		
		
		return avancementPhase;
	}
}
