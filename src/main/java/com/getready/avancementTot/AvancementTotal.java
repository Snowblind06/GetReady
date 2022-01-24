package com.getready.avancementTot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import com.getready.connexion.Connexion;

public class AvancementTotal implements Runnable {

	
	//initialisation des variables
	public final int startTimeActivite = (LocalTime.now().toSecondOfDay());
	public int indice;
	public float stopTimeLecteur ;
	public boolean running ;
	public int avancementTotal;
	public int heureLimite ;
	public static String tableUser;
	
	
@Override
public void run() {
	
	heureLimite(tableUser);
	
	while(running) {
	
		while(avancementTotal <100 || avancementTotal > 0) {
			
			if(running && avancementTotal <100) {
			
				int currentTime = LocalTime.now().toSecondOfDay() - startTimeActivite ;
				avancementTotal =  (int)(currentTime*100) / heureLimite;				
				}			
			
			else {
				
				Thread.currentThread().interrupt();
				avancementTotal = 0;
				return;
				}
						
		}
		
		Thread.currentThread().interrupt();
		avancementTotal = 0;
		return;
	}
	
	if(Thread.currentThread().isInterrupted()) {
		avancementTotal = 0;
		return;
	}
	
}
	
	
	
//CALCUL DUREE TOTALE ACTIVITE
private int heureLimite(String nomTable) {
		
		tableUser = nomTable;
		Connection con = Connexion.loadDatabase();
		
		//ON PREND L'HEURE DE FIN DE LA DERNIERE ETAPE = HEURE DE FIN
		try {
			
			int heureDepart ;
			Statement statement = null;
			ResultSet resultat = null;
			
			String commandSQL = "SELECT * FROM `"+tableUser+"`;" ;
			
			
			statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultat = statement.executeQuery(commandSQL);
			
			resultat.next();
			resultat.last();
			
			String finDePhase = resultat.getString("finphase");
			
			heureDepart = LocalTime.parse(finDePhase).toSecondOfDay();					
			
			heureLimite = heureDepart - startTimeActivite;
			
		} catch (SQLException e) {

			e.printStackTrace();
			}
		
		return heureLimite;	
	}

public void startAvanceTotThread(String nomTable) {
	
	tableUser = nomTable;
	running = true;
	
	Thread avanceThread = new Thread(this);
	avanceThread.start();
	
	System.out.println("START THREAD AVANCEMENT TOTAL:------- RUNNING:   "+running+"        NOM TABLE USER:     "+tableUser);
	
}

public void stopAvanceTotThread() {
	
	running = false;
	
	
	System.out.println("STOP THREAD AVANCEMENT TOTAL:-------  RUNNING:   "+running+"        NOM TABLE USER:     "+tableUser);
	}
	
}
