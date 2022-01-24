package com.getready.servlets;

import java.io.IOException;

import com.getready.activite.Activite;
import com.getready.beans.Bean_Phase;
import com.getready.userManagement.userManagement;
import com.getready.userbean.userbean;

import java.time.LocalTime;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class Ready
 */
@WebServlet({"/Menu"})
public class Ready extends HttpServlet {
	
	Bean_Phase bean = new Bean_Phase();
	
	userManagement userAuth = new userManagement();
	
	userbean user = new userbean();
	
	private static final long serialVersionUID = 1L;
	
	// Initialisation des Maps pour le stockage des valeurs du formulaires pr�c�demment stock�es dans les Strings
	
    /**
     * Default constructor. 
     */
    public Ready() {
        // TODO Auto-generated constructor stub
    	
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		Activite tableauActivite = new Activite();
		//RECUPERATION DE L'ID USER DEPUIS LA SESSION----------------------------
		HttpSession session = request.getSession();
		
		String id = (String) session.getAttribute("id");	
		
		System.out.println("READY.JAVA: " + id);
		
		//ON CHARGE LE BEAN USER AVEC LES DONN2ES EN BASE
		user = userAuth.afficherUserBean(id);
		System.out.println("BEAN USER:     "+ user.getNomTableUser());
		
		
		//AVEC L'ID ON AFFICHE LE NOM D'ACTIVITE POUR AFFICHAGE -------------------
		String activiteName = user.getActivite();
		
		//ON CHARGE LA REQUETE AVEC LE BEAN ISSU DE ACTIVITE.JAVA, LECTURE PUIS AFFICHAGE DE LA DB
		request.setAttribute("activite", tableauActivite.recupererPhase(user));	
		request.setAttribute("activiteName", activiteName);
		
		
		//ON CHARGE LE BEAN USER PROVENANT DE LA DB DANS LA SESSION POUR LA SUITE --------------------------------------
		session.setAttribute("beanUser", user);
		session.setAttribute("activite", tableauActivite.recupererPhase(user));
		
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/Menu.jsp").forward(request, response);
		}

	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
	
		//CREATION TABLEAU DES MESSAGES D'ERREUR DE SAISIE
		ArrayList<String> messageSaisie = new ArrayList<String>();

		
		//on instancie la class Activite pour afficher le tableau sur la JSP-----------------------------------------  
		Activite tableauActivite = new Activite();
	
		String activiteName = (String) session.getAttribute("activiteUser");
		
		
		request.setAttribute("activite", tableauActivite.recupererPhase(user));

		
		
		//AJOUT/MODIF DE PHASE --------------------------------------------------------------------------------------
		
		//initialiser les variables issues du formulaire et convertir les champ "num�ro" en int, et DebutPhase/FinPhase en Time	
		if (request.getParameter("phaseForm") != null) {
			
			
			
			//ON IMPORTE ET CONVERTI LES CHAMPS DU FORM AVANT TRAITEMENT --------------------------------------------
			String leNom = request.getParameter("NomPhase");
			int indice = Integer.parseInt(request.getParameter("numero"));
			LocalTime debut = LocalTime.parse(request.getParameter("DebutPhase"));
			LocalTime fin = LocalTime.parse(request.getParameter("FinPhase"));

			
			// Cr�er un bean avec les donn�es re�us des champs du formulaire------------------------------------------
			Bean_Phase bean = new Bean_Phase();
			
		
			//Enregistrer dans le bean les valeurs des champs au bon format:------------------------------------------
			bean.setIndice(indice);
			bean.setLeNom(leNom);
			bean.setDebut(debut);
			bean.setFin(fin);
			bean.setActiviteName(activiteName);
			
			//on lance la m�thode testajoutModif d'apr�s le bean nouvellement cr��---------------------------------------------
			tableauActivite.testAjoutModif(user, bean);
				
			messageSaisie.add("Etape " + indice + " créée/modifiée avec succés");
			request.setAttribute("messageSaisie", messageSaisie);
				
				
			//on utilise la m�thode recupererPhase pour envoyer la bean_List � la JSP pour affichage sous forme de tableau HTML
			request.setAttribute("activite", tableauActivite.recupererPhase(user));
			
			
			this.getServletContext().getRequestDispatcher("/WEB-INF/Menu.jsp").forward(request, response);
		}
			
		
		
	//SUPPRESSION PHASE----------------------------------------------------------------------------------------------------------
		
		//Si bouton "Supprimer la Phase" actionn� -> supprimer l'entr�e dans la BD-----------------------------------------------
		if(request.getParameter("suppr") != null) {
			String paramImport = request.getParameter("suppr");
			
			//ON DEDUIT L'INDICE DE LA PHASE A RETIRER DEPUIS CE QUI EST ECRIT SUR LE BOUTON DU TABLEAU SUR MENU.JSP-------------
			String retraitInt = paramImport.substring(13, paramImport.length());
			int indice = Integer.parseInt(retraitInt);
			
			
			//on utilise la m�thode recupererPhase pour envoyer la bean_List � la JSP pour affichage sous forme de tableau HTML
			tableauActivite.supprimerPhase(indice, user);
			
			//ON VIDE LE TABLEAU MESSAGE SAISIE ET ON CONFIRME LA SUPPRESSION-----------------------------------------------------
			messageSaisie.clear();
			messageSaisie.add("Etape " + indice+ " supprimée!");
			
			
			request.setAttribute("messageSaisie", messageSaisie);
			request.setAttribute("activite", tableauActivite.recupererPhase(user));	
			this.getServletContext().getRequestDispatcher("/WEB-INF/Menu.jsp").forward(request, response);
		}
		
		
	// DECONNEXION -------------------------------------------------------------------------------------------
		if(request.getParameter("deconnexion") != null) {
		
			response.sendRedirect("/GetReady/Login");
			request.getSession().invalidate();
//			this.getServletContext().getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
		}
		
		
	// GERER LE COMPTE---------------------------------------------------------------------------------
		if(request.getParameter("gestion") != null) {
			
			response.sendRedirect("/GetReady/Gestion");
		}
		
		
        
	}

}
