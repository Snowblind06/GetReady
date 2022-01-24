package com.getready.userManagement;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import com.getready.connexion.Connexion;
import com.getready.userbean.userbean;



public class userManagement {
	
		public String messageUser ;
	
	   PreparedStatement statement = null;
	   ResultSet resultat = null;
	   Connection connexion = null;
	   
	   
	   public void saveUser(userbean user) {
		  
		   connexion = Connexion.loadDatabase();
		   String now = LocalDate.now().toString();
		   
		   String commandSQL = "INSERT INTO users (id, password, email, creation, activite, nomTableActivite) VALUE(?, ?, ?, ?, ?, ?) ;";
		  
		   
		   try {
			   
			   statement = connexion.prepareStatement(commandSQL);
			   statement.setString(1, user.getId());		   
			   statement.setString(2, user.getPassword());
			   statement.setString(3, user.getEmail());
			   statement.setString(4, now);
			   statement.setString(5, user.getActivite());
			   statement.setString(6, user.getNomTableUser());
			   
			   statement.executeUpdate();
			   	   
			   statement.close();
			   
		   }catch(SQLException ex) {
			   ex.printStackTrace();
			   messageUser = "une erreur s'est produite à la création de votre compte, merci de contacter l'administrateur";
		   	}
		   
		   creerTable(user);
		   
		   messageUser = "utilisateur créé avec succés! Bienvenue "+user.getId();
		   
	   }
	
	   
	   public void updateUser(String modif, userbean nouveauUser, String champModif) {
		   
		   connexion = Connexion.loadDatabase();
		   
		   //r�cup�ration de l'id
		   String id = nouveauUser.getId();
		   		   
		   //commande SQL pour modifier les donn�es utilisateur dans la table users
		   String commandSQL = "UPDATE users SET "+champModif+"= '"+modif+"' WHERE id = '"+id+"' ;";

		   System.out.println(commandSQL);

		   
		   
		   try {
			   
			   statement = connexion.prepareStatement(commandSQL);
			   
			   statement.executeUpdate();
			   
			   statement.close();
			   
		   }catch(SQLException ex) {
			   ex.printStackTrace();
			   messageUser = "une erreur s'est produite à la création de votre compte, merci de contacter l'administrateur";
		   	}
 
		   
		  messageUser = "modifications prises en compte!" ;
		  
	   }
	   
	   public void modifActivite ( String champModif, userbean user, userbean nouveauUser) {
		   
		   connexion = Connexion.loadDatabase();
		   
		   //d�finition du nom de table avant modification: id+nomActivite
		   String nomTable = user.getNomTableUser();  
		   String nouvNomTable = nouveauUser.getNomTableUser();
		   
		  
		   //commandSQL2
		   String id = nouveauUser.getId();
		   String modif = nouveauUser.getActivite();
		   
		   
		   //commande SQL pour modifier les donn�es utilisateur dans la table users
		   String commandSQL2 = "UPDATE users SET "+champModif+"= '"+modif+"' WHERE id = '"+id+"' ;";
		   
		   
		   //commandeSQL pour modifier le nom de la table associ�e au user dans getready;
		   String commandSQL = "ALTER TABLE `"+nomTable+"` RENAME TO `"+nouvNomTable+"` ;"  ;
		   
		   
		   System.out.println(commandSQL);

		   
		   try {
			   
			   statement = connexion.prepareStatement(commandSQL);
			   
			   statement.executeUpdate();
			   
			   statement.close();
			   
		   }catch(SQLException ex) {
			   ex.printStackTrace();
			   messageUser = "une erreur s'est produite à la création de votre compte, merci de contacter l'administrateur";
		   	}
		   
		   try {
			   
			   statement = connexion.prepareStatement(commandSQL2);
			   
			   statement.executeUpdate();
			   
			   statement.close();
			   
		   }catch(SQLException ex) {
			   ex.printStackTrace();
			   messageUser = "une erreur s'est produite à la création de votre compte, merci de contacter l'administrateur";
		   	}
		   
		
		   
		  messageUser = "modifications prises en compte!" ;
		  
	   }
		   
	   
	   public void deleteUser(userbean user) {
		   
		   connexion = Connexion.loadDatabase();
		   
		   //r�cup�ration de l'Id depuis le bean cr�� dans al servlet d'apr�s la session
		   String id = user.getId();
		   
		   //on r�cup�re le nom de la table � supprimer
		   String nomTable = user.getNomTableUser();
		   
		   //commande SQL pour supprimer l'user de la table users
		   String commandSQL1 = "DELETE FROM users WHERE id='"+id+"' ;";

		   System.out.println(commandSQL1);
		   
		   //commande SQL pour supprimer la table de l'activite du user dans getready
		   String commandSQL2 = "DROP TABLE `"+nomTable+"` ;" ;
		   System.out.println(commandSQL2);
		   
		   try {
			   
			   statement = connexion.prepareStatement(commandSQL1);
			   PreparedStatement statementDrop = connexion.prepareStatement(commandSQL2);
			   
			   statementDrop.executeUpdate();
			   statement.executeUpdate();
			   	
			   statement.close();
			   statementDrop.close();
			   
		   }catch(SQLException ex) {
			   ex.printStackTrace();
			   messageUser = "une erreur s'est produite,s merci de contacter l'administrateur";
		   	}
		   
		   
		   messageUser = "votre compte a été supprimé, au plaisir de vous revoir!";
	   }
	   
	   
	   public userbean afficherUserBean(String id) {

		   userbean user = new userbean();
		   
		   connexion = Connexion.loadDatabase();
		   
		   String commandSQL = "SELECT * FROM users WHERE id='"+id+"' ;";
		   System.out.println(commandSQL);
		   
		   try {
			   
			   statement = connexion.prepareStatement(commandSQL);
			   
			   resultat = statement.executeQuery();
			   
			   if(resultat.next() == true) {
				   user.setId(resultat.getString("id"));
				   user.setActivite(resultat.getString("activite"));
				   user.setEmail(resultat.getString("email"));
				   user.setPassword(resultat.getString("password"));
				   user.setNomTableUser(resultat.getString("nomTableActivite"));
				   
				   System.out.println("USERMANAGEMENT - RESULTAT COMMAND NOM ACTIVITE: " + user.getActivite());
				   
			   }
			   
		   }catch(SQLException e) {
			   e.printStackTrace();
			   messageUser = "une erreur s'est produite, merci de contacter l'administrateur";
		   }
		   
		   messageUser = "Bonjour " + user.getId() ;
		   
		   return user;
	   }
	   
	   
	   //VERIFICATION DU MOT DE PASSE A LA CONNEXION
	   public boolean passwordControl(String id, String passwordEntry) {
		   
		   
		   //INITIALISATION DES VARIABLES DONT LE CHECK A RETOURNER
		   boolean check = false;
		   String passwordDB = null;
		   
		   
		   //PREPARATION CONNECTION A LA DB SELON LE NOM D'USER
		   connexion = Connexion.loadDatabase();
		   String commandSQL = "SELECT password FROM users WHERE id='"+id+"' ;";
		   
		   
		   //CONNEXION ET SORTIE DU PASSWORD DU USER
		   try {
			statement = connexion.prepareStatement(commandSQL);
			resultat = statement.executeQuery();
			
			
			if(resultat.next() == true) {
			// ON STOCK LE PASSWORD DANS LA STRING
			passwordDB = resultat.getString("password");
			System.out.println("PASSWORD-US= " + passwordEntry);
			System.out.println("PASSWORD-DB= " + passwordDB);

			}
		   } catch (SQLException e) {
			e.printStackTrace();
		   	}
		   
		   
		   //ON VERIFIE SI LE PASSWORD DONNE CORRESPOND A CELUI DE LA DB POUR L'ID RENSEIGNE
		   if(passwordDB != null) {
			   if(passwordDB.equals(passwordEntry)) {
				   System.out.println("LES DEUX PASSWORDS SONT IDENTIQUES");
				   check = true;
				   return check;
				
			   }else {
				   System.out.println("LES DEUX PASSWORDS NE SONT PAS IDENTIQUES");
				   check = false;
				   messageUser = "mot de passe incorrect";
				   return check;
			   }
		   }else {
			   
			   check = false;
			   messageUser = "identifiant incorrect";
			   return check;
		   }
		}
		  
	   
	   
	   private void creerTable(userbean user) {
		   
		   connexion = Connexion.loadDatabase();
		   String nomTable = user.getNomTableUser();
		   System.out.println("USER MANAGEMENT CREER TABLE ACITVITE:    " + nomTable);
		   
		   String commandSQL = "CREATE TABLE `"+nomTable+"` (indice INT PRIMARY KEY, nomphase VARCHAR(100), debutphase VARCHAR(100), finphase VARCHAR(100), dureephase INT) ;";
		   
		   try {
			   
			statement = connexion.prepareStatement(commandSQL);
			statement.executeUpdate();
			statement.close();
			
		   } catch (SQLException e) {
			   e.printStackTrace();
		}
		   messageUser = nomTable + " a bien été créée!" ;
	   }
	   
	   
}
