package com.getready.servlets;

import java.io.IOException;

import com.getready.userManagement.userManagement;
import com.getready.userbean.userbean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class Lock
 */
@WebServlet({"", "/Login"})
public class Lock extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Lock() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
	}

	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("Connection") != null) {
			
			
			String id = request.getParameter("id");
			
			String passwordEntry = request.getParameter("password");
			
			System.out.println(id + "  " + passwordEntry);
		
			
			
			userManagement control = new userManagement();
			
			boolean verification = control.passwordControl(id, passwordEntry);
			
			
			System.out.println(verification);
	
			
			if(verification == true) {
				
				String messageErreurConnection = "vous êtes identifié(e)";
				
				HttpSession session = request.getSession();
				
				
				session.setAttribute("id", id);
				
				
				request.setAttribute("messageErreur", messageErreurConnection);
				response.sendRedirect("/GetReady/Menu");
				
				
				
			}else {
				
				System.out.println("verification a échoué");
				String messageErreurConnection = "le mot de passe et/ou l'identifiant sont erronés";
				
				request.setAttribute("messageErreur", messageErreurConnection);
				this.getServletContext().getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
				
			}
			
			
		}
		
		if(request.getParameter("ConnectionCreer") != null) {
			
			if(request.getParameter("idCreer") != "" && request.getParameter("passwordCreer") != "" && request.getParameter("activiteName") != "")  {
			//IMPORTATION DES PARAMETRES DU FORM SUR LOGIN.JSP DANS DES VARIABLES
			String nouvelId = request.getParameter("idCreer");
			String nouveauPassword = request.getParameter("passwordCreer");
			String nouvelEmail = request.getParameter("emailUser");
			String nouvelleActivite = request.getParameter("activiteName");
			String nouveauNomTable	= nouvelId+nouvelleActivite;
			
			//CREATION DU BEAN USER AVANT SAUVEGARDE EN DB-----------------------
			userbean user = new userbean();
			
			user.setId(nouvelId);
			user.setPassword(nouveauPassword);
			user.setEmail(nouvelEmail);
			user.setActivite(nouvelleActivite);
			user.setNomTableUser(nouveauNomTable);
			
			//ENVOI DU BEAN USER DANS LE USERMANAGEMENT POUR SAUVEGARDE EN DB----
			userManagement sauverBean = new userManagement();
			
			sauverBean.saveUser(user);
			
			request.setAttribute("messageUser", sauverBean.messageUser);
			
			this.getServletContext().getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
			
			
			}else {
				
				String champsVides = "Enregistrement impossible, des champs sont encore vides!" ;
				request.setAttribute("champsVides", champsVides);
			}
		}
		
			
	}

}
