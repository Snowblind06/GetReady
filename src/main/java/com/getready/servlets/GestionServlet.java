package com.getready.servlets;

import java.io.IOException;

import com.getready.userbean.userbean;
import com.getready.userManagement.userManagement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class GestionServlet
 */
@WebServlet({"/Gestion"})
public class GestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	//CREATION INTERFACE DE GESTION DES USERS------------------------------------------------------------------
	userManagement userManagement = new userManagement();
	userbean user = new userbean();
	
    /**
     * Default constructor. 
     */
    public GestionServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//ON IMPORTE LA SESSION-----------------------------------------------------------------------------------
		HttpSession session = request.getSession();
		//ON RECUPERE L'ID DU USER DANS LA SESSION------------------------------------------------------------------
		String id = (String) session.getAttribute("id");
		
		//ON CHARGE LE BEAN AVEC LES DONNES EN BASE
		user = userManagement.afficherUserBean(id);
			
		//ON CHARGE LES INFOS DANS LA REQUETE POUR AFFICHAGE-----------
		request.setAttribute("userBean", user);
				
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/Gestion.jsp").forward(request, response);
	}

	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		//ON RECUPERE L'ID ET L'ACTIVITE DU USER DANS LA SESSION------------------------------------------------------------------
		String nomUser = (String) session.getAttribute("id");
		//CREATION DU BEAN POUR RECUPERER LES CHAMPS DU FORMULAIRE DE MODIFICATION DU COMPTE USER--------------------------------
		userbean nouveauUser = new userbean();
		nouveauUser.setId(nomUser);
		nouveauUser.setPassword(request.getParameter("nouveauPassword"));
		nouveauUser.setEmail(request.getParameter("nouveauEmail"));
		nouveauUser.setActivite(request.getParameter("nouveauActivite"));
		nouveauUser.setNomTableUser(user.getId()+request.getParameter("nouveauActivite"));
		
		
		//SI BOUTON MODIF DU MOT DE PASSE APPUYE -----------------------------------------------
		if(request.getParameter("modifPassword") != null) {
			
			//on d�fini le champ sur lequel porte la modification----------------------------------
			String champModif = "password";
			
			String modif = nouveauUser.getPassword();
			
			//ON LANCE L'UPDATE DANS LA BASE DE DONNES AVEC: nom d'activit� tel qu'� l'ouverture de session, ID du user, champ � modifier, nouveau mot de passe---
			userManagement.updateUser(modif, nouveauUser, champModif);
			
			//ON CHARGE LA REQUETE AVEC LE BEAN UTILISATEUR POUR LECTURE DE LA DB DEPUIS L'ID DE SESSION = AFFICHAGE DES MODIFICATIONS---------------------------------
			request.setAttribute("userBean", userManagement.afficherUserBean(nomUser));
			
			//MISE A JOUR DE LA SESSION AVEC LE BEAN USER MODIFIE--------------------------------------------------------------------------------------------------------
			user.setPassword(nouveauUser.getPassword());
			session.setAttribute("beanUser", user);
			
			this.getServletContext().getRequestDispatcher("/WEB-INF/Gestion.jsp").forward(request, response);
			
		}
		
		
		//SI BOUTON MODIF NOM D'ACTIVITE APPUYE ------------------------------------------------
		if(request.getParameter("modifActivite") != null) {
			
				//on d�fini le champ sur lequel porte la modification----------------------------------
				String champModif = "activite";
				
				
				//ON LANCE L'UPDATE DANS LA BASE DE DONNES AVEC: nom d'activit� tel qu'� l'ouverture de session, ID du user, champ � modifier, nouveau nom d'activit�---
				userManagement.modifActivite(champModif, user, nouveauUser);
				
				
				//ON CHARGE LE NOUVEAU NOM DE TABLE DANS LA SESSION ET LE BEAN USER--------------------------------------------------------------------------------------
				user.setActivite(nouveauUser.getActivite());
				user.setNomTableUser(nouveauUser.getNomTableUser());
				session.setAttribute("activiteUser", nouveauUser.getActivite());
				
				champModif = "nomTableActivite";
				userManagement.updateUser(user.getNomTableUser(), nouveauUser, champModif);
				
				//ON CHARGE LA REQUETE AVEC LE BEAN UTILISATEUR POUR LECTURE DE LA DB DEPUIS L'ID DE SESSION = AFFICHAGE DES MODIFICATIONS---------------------------------
				request.setAttribute("userBean", userManagement.afficherUserBean(nomUser));	
				
				
				
				//MISE A JOUR DE LA SESSION AVEC LE BEAN USER MODIFIE--------------------------------------------------------------------------------------------------------
				
				session.setAttribute("beanUser", user);
				
				
				
				this.getServletContext().getRequestDispatcher("/WEB-INF/Gestion.jsp").forward(request, response);
			
		}
		
		//SI BOUTON MODIF EMAIL APPUYE--------------------------------------------------------------
		if(request.getParameter("modifEmail") != null) {
			
				String champModif = "email";
				String modif = nouveauUser.getEmail();
				
				//ON LANCE L'UPDATE DANS LA BASE DE DONNES AVEC: nom d'activit� tel qu'� l'ouverture de session, ID du user, champ � modifier, nouveau email---
				userManagement.updateUser(modif, nouveauUser, champModif);
				
				//ON CHARGE LA REQUETE AVEC LE BEAN UTILISATEUR POUR LECTURE DE LA DB DEPUIS L'ID DE SESSION = AFFICHAGE DES MODIFICATIONS---------------------------------
				request.setAttribute("userBean", userManagement.afficherUserBean(nomUser));
				
				//MISE A JOUR DE LA SESSION AVEC LE BEAN USER MODIFIE--------------------------------------------------------------------------------------------------------
				user.setEmail(nouveauUser.getEmail());
				session.setAttribute("beanUser", user);
				
				this.getServletContext().getRequestDispatcher("/WEB-INF/Gestion.jsp").forward(request, response);
			
		}
		
		
		if(request.getParameter("suppVerif") != null && request.getParameter("suppUser") != null) {
			
			userManagement.deleteUser(userManagement.afficherUserBean(nomUser));
			response.sendRedirect("/GetReady/Login");
			request.getSession().invalidate();
		}
		
	}

}
