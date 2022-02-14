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
		
		//on d�clare un nouvel array et initialise les variables de requ�te/connexion
		List<Bean_Phase> bean_List = new ArrayList<Bean_Phase>();
		Statement statement = null;
		ResultSet resultat = null;
		
		//on importe le nom de la table de DB à lire pour la prochaine requête SQL
		String nomTable = user.getNomTableUser();
		
		Connection con = Connexion.loadDatabase();
		
		//on parcours la base de donn�es et on stocke les entr�es dans la variable resultat
		try {
			
			statement = con.createStatement();
			String commandSQL = "SELECT indice, nomphase, debutphase, finphase, dureephase FROM `"+nomTable+"` ;";
			
			
			resultat = statement.executeQuery(commandSQL);
			
			//pour chaque r�sultat on stocke les valeurs des champs de la BD et on d�fini les propri�t�s du bean cr�� dans la servlet avec ces m�mes valeurs
			while (resultat.next()) {
				
				int indice = resultat.getInt("indice");
				String nomphase = resultat.getString("nomphase");
				LocalTime debut = LocalTime.parse(resultat.getString("debutphase"));
				LocalTime fin = LocalTime.parse(resultat.getString("finphase"));
				int duree = resultat.getInt("dureephase");
				
				System.out.println(indice +"  "+ nomphase);
				
				//enregistrement des valeurs des variables/issues de la BD dans les propri�t�s du bean
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
		
		//on renvoie le tableau � la servlet qui l'a appel�: Ready.java
		return bean_List;
	}
	
	

	
	public void testAjoutModif(userbean user, Bean_Phase bean) {
		
		Connection con = Connexion.loadDatabase();
		Statement statement = null;
		ResultSet resultat = null;
		String nomTable = user.getNomTableUser();

		try {
			
			//on parcours la BD et instancie la connexion
			statement = con.createStatement();
			
			resultat = statement.executeQuery("SELECT indice FROM `"+nomTable+"` ;");
			
			//pour chaque entr�e parcourue on teste si une entr�e dans la BD existe d�j� � cet indice (l'indice du bean = celui donn� par le user dans le form)
			while (resultat.next()) {
				
				//on initialise la variable dans la boucle pour qu'elle change � chaque it�ration pour test ult�rieur
				int indiceMod = resultat.getInt("indice");
			
				//si l'indice du bean/rentr� par l'user dans le formulaire est d�j� pr�sent dans la base de donn�e, on modifie l'entr�e avec les nouvelles valeurs donn�es par l'user= dans le bean
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
			//on importe dans les variables de méthode le bean reçu en argument
			int indice = bean.getIndice();
			String nomphase = bean.getLeNom();
			LocalTime debutphase = bean.getDebut();
			LocalTime finphase = bean.getFin();
			String nomTable = user.getNomTableUser();
			
			
			////////////////// A SIMPLIFIER: SUPPRIMER LES VARIABLES DE LA METHODE ET UTILISER LES GETERS DANS LA REQUETE\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
			
			//on rempli la requête sql avec les valeurs des variables
			PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO `"+nomTable+"` (indice, nomPhase, debutPhase, finPhase) VALUES('"+indice+"', '"+nomphase+"', '"+debutphase+"', '"+finphase+"') ;");
					

			preparedStatement.executeUpdate();
			
			calculIntervalle(user, bean);
			
			}catch (SQLException e) {
				e.printStackTrace();
					}			

	}		
	
	
	//modification d'une entr�e existante, trouv�e par l'indice de la BD vs indice du bean - fonction appel�e par ajouterPhase()
	public void modifierPhase(userbean user, Bean_Phase bean) {
		
		Connection con = Connexion.loadDatabase();
		
		try {
			//on initialise les variables: importation des propri�t�s du bean pour ensuite les  envoyer dans la requ�te SQL
			int cle = bean.getIndice();
			String nomphase = bean.getLeNom();
			LocalTime debutphase = bean.getDebut();
			LocalTime finphase = bean.getFin();
			String nomTable = user.getNomTableUser();
			
			//envoi de la requ�te SQL � la BD incluant les variables initialis�e ci-dessus
			PreparedStatement preparedStatement = con.prepareStatement("UPDATE `"+nomTable+"` SET nomphase = '"+nomphase+"', debutphase = '"+debutphase+"', finphase = '"+finphase+"'  WHERE indice= "+cle+";");
			preparedStatement.executeUpdate();
			
			//CALCUL DE LA DURE DE L'ETAPE
			calculIntervalle(user, bean);
			
		}catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
			}
	}
	
	
	
	//m�thode pour supprimer une entr�e dans la BD: l'ordre vient de l'user dans la jsp, chaque ligne du tableau de recap du contenu de la BD a un boutton "supprimer" envoyant un indice
	public void supprimerPhase(int indice, userbean user) {

		
		Connection con = Connexion.loadDatabase();
	
		try {
			PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM `"+user.getNomTableUser()+"` WHERE indice='"+indice+"';");
			preparedStatement.executeUpdate();
			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//Calculer la dur�e de l'�tape ET ENREGISTREMENT EN DB
	private void calculIntervalle(userbean user, Bean_Phase bean) {

		Connection con = Connexion.loadDatabase();
		
			try {
				
				PreparedStatement preparedStatement;
				
				//import de l'indice de la phase  en BD dont il faut calculer la dur�e
				int cle = bean.getIndice();
				
				//conversion des propri�t�s Time du bean (saisie user) en LocalTime pour calcul d'interval (Duration.between)
				LocalTime debutPhase = bean.getDebut();
				LocalTime finPhase = bean.getFin();
				String nomTable = user.getNomTableUser();
				System.out.println("ACTIVITE.JAVA PRINT BEAN.ACTIVITE:   " + nomTable);
				Long intervallePhase = Duration.between(debutPhase, finPhase).toMinutes();
				
				//Conversion en entier (compatibilit� avec JProgressBar)
				int dureePhase = intervallePhase.intValue();
				
				//d�finition de la propri�t� du bean
				bean.setDureephase(dureePhase);

				//enregistrement de la propri�t� du bean (dur�e) en BD incluant les variables initialis�e ci-dessus
				preparedStatement = con.prepareStatement("UPDATE `"+nomTable+"` SET dureephase = '"+dureePhase+"' WHERE indice= '"+cle+"';");
				preparedStatement.executeUpdate();
			
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			}
	
	}

