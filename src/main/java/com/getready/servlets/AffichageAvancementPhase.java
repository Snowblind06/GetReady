package com.getready.servlets;

import java.io.IOException;

import com.getready.avancementTot.AvancementTotal;
import com.getready.timecalculator.TimeCalculator;
import com.getready.userbean.userbean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
/**
 * Servlet implementation class AffichageAvancementPhase
 */


@WebServlet({"/Run"})
public class AffichageAvancementPhase extends HttpServlet {
	private static final long serialVersionUID = 1L;


	//instance du TimeCalculator
	TimeCalculator timeCalculator = new TimeCalculator();

	//AVANCEMENT TOTAL
	AvancementTotal avanceTot = new AvancementTotal();
	
	
	
	
    /**
     * Default constructor. 
     */
    public AffichageAvancementPhase() {
   
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String activiteUser = (String) session.getAttribute("activiteUser");
		request.setAttribute("activiteUser", activiteUser);
	
		request.setAttribute("nomphase", timeCalculator.nomPhaseEnCours);
		request.setAttribute("avancement", timeCalculator.getAvancementPhase());
		request.setAttribute("avancementTotal", avanceTot.avancementTotal);

		
		this.getServletContext().getRequestDispatcher("/WEB-INF/Run.jsp").forward(request, response);
		
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		HttpSession session = request.getSession();
		userbean user = (userbean) session.getAttribute("beanUser");
		
		
		String nomTable = user.getNomTableUser();
		
	
		
				
		//DEPART ACTIVITE--------------------------------------------------------------------------------------------------------------

		if(request.getParameter("go") != null) {

		
			timeCalculator.indice = 0;
			timeCalculator.startPhaseThread(nomTable);
			
			//Affichage message user
			String messageServlet = "l'activité a commencé!";
			
			//Lancement AVCEMENT TOTAL
			avanceTot.startAvanceTotThread(nomTable);
			
			
			request.setAttribute("messageServlet", messageServlet);

			
		 
		}
		
		
		//ARRET LECTEUR----------------------------------------------------------------------------------------------------------------------
		if(request.getParameter("stop") != null) {
			
			
			
			//arrêtdes threads----------------------------------------------------------------------------------------------------------	
			timeCalculator.stopPhaseThread(0, nomTable);
			avanceTot.stopAvanceTotThread();
			
			
			//message � retourner sur la jsp------------------------------------------------------------------------------------------------
			String messageServlet = "Activité interrompue";
			
			//chargement de la requ�te------------------------------------------------------------------------------------------------------
			request.setAttribute("messageServlet", messageServlet);	
			request.setAttribute("nomphase", timeCalculator.nomPhaseEnCours);
			
		}
		
		
		
		//PHASE SUIVANTE---------------------------------------------------------------------------------------------------------------------
		if(request.getParameter("plus") != null) {
			
			timeCalculator.stopPhaseThread(+1, nomTable);
			timeCalculator.phaseChange(nomTable);
			

			String messageServlet = "Phase suivante commencée" ;
			
			
			request.setAttribute("messageServlet", messageServlet);
			request.setAttribute("nomphase", timeCalculator.nomPhaseEnCours);
			
			}
		
		
		//PHASE PRECEDENTE----------------------------------------------------------------------------------------------------------------------
		if(request.getParameter("moins") != null) {
			
			timeCalculator.stopPhaseThread(-1, nomTable);
			timeCalculator.phaseChange(nomTable);
				
			
			String messageServlet = "Phase précédente commencée" ;
			
			
			request.setAttribute("messageServlet", messageServlet);
			request.setAttribute("nomphase", timeCalculator.nomPhaseEnCours);
			
			}
		

		
		request.setAttribute("nomphase", timeCalculator.nomPhaseEnCours);
		request.setAttribute("avancement", timeCalculator.getAvancementPhase());
		request.setAttribute("avancementTotal", avanceTot.avancementTotal);
		
//		System.out.println("------DOPOST METHOD----------AVANCEMENT PHASE=---------"+timeCalculator.getAvancementPhase());
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/Run.jsp").forward(request, response);
	
		
	}

}