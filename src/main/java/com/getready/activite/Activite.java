package com.getready.activite;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.getready.beans.Bean_Phase;
import com.getready.connexion.Connexion;
import com.getready.userbean.userbean;



public class Activite {

	public List<Bean_Phase> recupererPhase(userbean user) {
		
		//on déclare un nouvel array et les variables de requête/connexion
		List<Bean_Phase> bean_List = new ArrayList<Bean_Phase>();
		Statement statement = null;
		ResultSet resultat = null;
		
		
		String nomTable = user.getNomTableUser();
		System.out.println("ACTIVITE.JAVA -- TABLE: " + nomTable);
		
		Connection con = Connexion.loadDatabase();
		
		//on parcours la base de données et on stocke les entrées dans la variable resultat
		try {
			
			statement = con.createStatement();
			String commandSQL = "SELECT indice, nomphase, debutphase, finphase, dureephase FROM `"+nomTable+"` ;";
			
			System.out.println("ACTIVITE.JAVA---RECUPERER PHASE:   " +commandSQL);
			
			resultat = statement.executeQuery(commandSQL);
			
			//pour chaque résultat on stocke les valeurs des champs de la BD et on défini les propriétés du bean créé dans la servlet avec ces mêmes valeurs
			while (resultat.next()) {
				
				int indice = resultat.getInt("indice");
				String nomphase = resultat.getString("nomphase");
				LocalTime debut = LocalTime.parse(resultat.getString("debutphase"));
				LocalTime fin = LocalTime.parse(resultat.getString("finphase"));
				int duree = resultat.getInt("dureephase");
				
				System.out.println(indice +"  "+ nomphase);
				
				//enregistrement des valeurs des variables/issues de la BD dans les propriétés du bean
				Bean_Phase bean = new Bean_Phase();
				bean.setLeNom(nomphase);
				bean.setIndice(indice);
				bean.setDebut(debut);
				bean.setFin(fin);
				bean.setDureephase(duree);
				
				//on ajoute le bean remplis dans le tableau
				bean_List.add(bean);
			
				}
		}catch (SQLException e) {
		
		}finally {
			
			try {
				if(resultat != null)
					resultat.close();
				if(statement != null)
					statement.close();
				if(con != null)
					con.close();
			}catch(SQLException ignore) {
			}
		}
		
		//on renvoie le tableau à la servlet qui l'a appelé: Ready.java
		return bean_List;
	}
	
	
	
	// charger la base de données: trouver le driver, définir l'objet connexion avec l'adresse de la BD l'id de connexion et le mdp
//	private void loadDatabase() {
//		
//		 // Chargement du driver
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//        }
//        //on créée la connexion avec les identifiants/mdp
//		try {
//			connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/getready", "root", "Aqwel83alex87!");
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	public void testAjoutModif(userbean user, Bean_Phase bean) {
		
		Connection con = Connexion.loadDatabase();
		Statement statement = null;
		ResultSet resultat = null;
		String nomTable = user.getNomTableUser();
		System.out.println("ACTIVITE.JAVA -- testAjoutModif -- activite:   " + nomTable);

		try {
			
			//on parcours la BD et instancie la connexion
			statement = con.createStatement();
			
			resultat = statement.executeQuery("SELECT indice FROM `"+nomTable+"` ;");
			
			//pour chaque entrée parcourue on teste si une entrée dans la BD existe déjà à cet indice (l'indice du bean = celui donné par le user dans le form)
			while (resultat.next()) {
				
				//on initialise la variable dans la boucle pour qu'elle change à chaque itération pour test ultérieur
				int indiceMod = resultat.getInt("indice");
			
				//si l'indice du bean/rentré par l'user dans le formulaire est déjà présent dans la base de donnée, on modifie l'entrée avec les nouvelles valeurs données par l'user= dans le bean
				if(indiceMod == bean.getIndice()) {
					modifierPhase(user, bean);
						
				}else {
					ajouterPhase(user, bean);
					}
				
			}
			
			if(resultat.next() == false) {
				ajouterPhase(user, bean);
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
				

	public void ajouterPhase (userbean user, Bean_Phase bean) {		
		Connection con = Connexion.loadDatabase();
		
		try {
			
			int indice = bean.getIndice();
			String nomphase = bean.getLeNom();
			LocalTime debutphase = bean.getDebut();
			LocalTime finphase = bean.getFin();
			String nomTable = user.getNomTableUser();
			
			
			PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO `"+nomTable+"` (indice, nomPhase, debutPhase, finPhase) VALUES('"+indice+"', '"+nomphase+"', '"+debutphase+"', '"+finphase+"') ;");
					

			preparedStatement.executeUpdate();
			
			calculIntervalle(user, bean);
			
			}catch (SQLException e) {
				e.printStackTrace();
					}			

	}		
	
	
	//modification d'une entrée existante, trouvée par l'indice de la BD vs indice du bean - fonction appelée par ajouterPhase()
	public void modifierPhase(userbean user, Bean_Phase bean) {
		
		Connection con = Connexion.loadDatabase();
		
		try {
			//on initialise les variables: importation des propriétés du bean pour ensuite les  envoyer dans la requête SQL
			int cle = bean.getIndice();
			String nomphase = bean.getLeNom();
			LocalTime debutphase = bean.getDebut();
			LocalTime finphase = bean.getFin();
			String nomTable = user.getNomTableUser();
			
			//envoi de la requête SQL à la BD incluant les variables initialisée ci-dessus
			PreparedStatement preparedStatement = con.prepareStatement("UPDATE `"+nomTable+"` SET nomphase = '"+nomphase+"', debutphase = '"+debutphase+"', finphase = '"+finphase+"'  WHERE indice= "+cle+";");
			preparedStatement.executeUpdate();
			
			calculIntervalle(user, bean);
			
		}catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
			}
	}
	
	
	
	//méthode pour supprimer une entrée dans la BD: l'ordre vient de l'user dans la jsp, chaque ligne du tableau de recap du contenu de la BD a un boutton "supprimer" envoyant un indice
	public void supprimerPhase(int indice, userbean user) {
		
		System.out.println("ACTIVITE.JAVA supprimerPhase PRINT activiteName:   " + user.getNomTableUser());
		
		Connection con = Connexion.loadDatabase();
	
		try {
			PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM `"+user.getNomTableUser()+"` WHERE indice='"+indice+"';");
			preparedStatement.executeUpdate();
			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//Calculer la durée de l'étape
	private void calculIntervalle(userbean user, Bean_Phase bean) {

		Connection con = Connexion.loadDatabase();
		
			try {
				
				PreparedStatement preparedStatement;
				
				//import de l'indice de la phase  en BD dont il faut calculer la durée
				int cle = bean.getIndice();
				
				//conversion des propriétés Time du bean (saisie user) en LocalTime pour calcul d'interval (Duration.between)
				LocalTime debutPhase = bean.getDebut();
				LocalTime finPhase = bean.getFin();
				String nomTable = user.getNomTableUser();
				System.out.println("ACTIVITE.JAVA PRINT BEAN.ACTIVITE:   " + nomTable);
				Long intervallePhase = Duration.between(debutPhase, finPhase).toMinutes();
				
				//Conversion en entier (compatibilité avec JProgressBar)
				int dureePhase = intervallePhase.intValue();
				
				//définition de la propriété du bean
				bean.setDureephase(dureePhase);

				//enregistrement de la propriété du bean (durée) en BD incluant les variables initialisée ci-dessus
				preparedStatement = con.prepareStatement("UPDATE `"+nomTable+"` SET dureephase = '"+dureePhase+"' WHERE indice= '"+cle+"';");
				preparedStatement.executeUpdate();
			
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			}
	
	}

