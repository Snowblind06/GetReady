package com.getready.servlets;

import java.io.IOException;
import java.util.List;

import com.getready.activite.Activite;
import com.getready.avancementTot.AvancementTotal;
import com.getready.beans.Bean_Phase;
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

	
	
    /**
     * Default constructor. 
     */
    public AffichageAvancementPhase() {
   
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//ON CHARGE LA SESSION DEPUIS LA REQUETE-----------------------------------------------------------------------------------------
		HttpSession session = request.getSession();
		
		
		//ON INSTANCIE UN USERBEAN ET LE CONTROLEUR DE THREADS EN LES CHARGEANT DEPUIS LA SESSION-----------------------------------------
		userbean user = (userbean) session.getAttribute("beanUser");
		TimeCalculator timeCalculator = (TimeCalculator) session.getAttribute("timeCalculator");
		AvancementTotal avanceTot = (AvancementTotal) session.getAttribute("avancementTotal");
		
		//ON CHARGE LE USERBEAN AVEC LES GETTERS DES CONTROLEURS DE THREADS------------------------------------------------------------------
		user.setAvancementPhaseBean(timeCalculator.getAvancementPhase());
//		user.setAvancementTotalBean(timeCalculator.getAvanceTotal());
		user.setPhaseEnCoursBean(timeCalculator.getNomPhaseEnCours());
		user.setIndiceEnCours(timeCalculator.getIndice());
		user.setAvancementTotalBean(avanceTot.getAvancementTotal());
		
		//ON RENVOIE LE USERBEAN ET LES CONTROLEURS DE THREADS DANS LA SESSION APRES MISE A JOUR SI BESOIN DANS LE DOPOST PLUS TARD----------
		session.setAttribute("timeCalculator", timeCalculator);
		session.setAttribute("beanUser", user);
		session.setAttribute("avancementTotal", avanceTot);
		
		//ON CHARGE LA REQUETE AVEC LES INFOS DU USERBEAN NECESSAIRES A L'AFFICHAGE--------------------------------------------------------
		request.setAttribute("nomphase", user.getPhaseEnCoursBean());
    	request.setAttribute("avancementTotal", user.getAvancementTotalBean());
    	request.setAttribute("avancement", user.getAvancementPhaseBean());
		request.setAttribute("avancementTotal", user.getAvancementTotalBean());
    	System.out.println("---------timeCalculator GETTERS---------PHASE: "+timeCalculator.getNomPhaseEnCours()+"----------AVANCEMENT: "+timeCalculator.getAvancementPhase());
    	
		this.getServletContext().getRequestDispatcher("/WEB-INF/Run.jsp").forward(request, response);
    	
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	
		//RECUPERATION DE LA SESSION, DU CONTROLEUR DE THREADS ET DU BEAN USER----------------------------------------------------------
		HttpSession session = request.getSession();
		userbean user = (userbean) session.getAttribute("beanUser");
		
		Activite tableauActivite = new Activite();
		
		List<Bean_Phase> listPhasesUserBean = tableauActivite.recupererPhase(user);
		
		
//		//ON INSTANCIE LA LIST DES BEANS DE PHASE DU USER----------------------------------------------------------------------------------
//		List<Bean_Phase> listPhases = tableauActivite.recupererPhase(user);
		
		
		//DEPART ACTIVITE-----------------------------------------------------------------------------------------------------------------
		if(request.getParameter("go") != null) {
			
			//INSTANCE DES THREADS D'AVANCEMENT--------------------------------------------------------------------------------------------
			AvancementTotal avanceTot = new AvancementTotal();
			TimeCalculator timeCalculator = new TimeCalculator();
			//LANCEMENT DU THREAD----------------------------------------------------------------------------------------------------------
			timeCalculator.setListPhasesUser(listPhasesUserBean);			
			timeCalculator.startPhaseThread(0);
			
			avanceTot.startAvanceTotThread(listPhasesUserBean);
			
			
			//CHARGEMENT DU BEAN AVEC LES INFOS SUR LE TRAITEMENT FAIT PAR LE THREAD--------------------------------------------------------
			//ON VERIFIE QU'UNE PHASE EST BIEN EN LECTURE AVANT-----------------------------------------------------------------------------
			if(timeCalculator.getNomPhaseEnCours() != null) {
				user.setPhaseEnCoursBean(timeCalculator.getNomPhaseEnCours());
			
			//VALEUR PAR DEFAUT DE LA STRING PHASE EN COURS--------------------------------------------
			}else {
				user.setPhaseEnCoursBean("-");
			}
			
			//ON CHARGE LES VALEURS ISSUES DU TRAITEMENT DU THREAD DANS LE USERBEAN RECUPERE------------------------------------------------
			user.setAvancementPhaseBean(timeCalculator.getAvancementPhase());
			user.setAvancementTotalBean(avanceTot.getAvancementTotal());
			
			//ON CHARGE LA SESSION AVEC LE BEAN MIS A JOUR ET LE  TIMECALCULATOR----------------------------------------------------------------------------------
			session.setAttribute("beanUser", user);
			session.setAttribute("timeCalculator", timeCalculator);
			session.setAttribute("avancementTotal", avanceTot);
			
			//NOTIFICATION UTILISATEUR SUR LE DEBUT DE L'ACTIVITE---------------------------------------------------------------------------
			String messageServlet = "l'activité a commencé!";
			request.setAttribute("messageServlet", messageServlet);
		}
		
		
		
		//ARRET LECTEUR----------------------------------------------------------------------------------------------------------------------
		if(request.getParameter("stop") != null) {
			
			AvancementTotal avanceTot = (AvancementTotal) session.getAttribute("avancementTotal");
			TimeCalculator timeCalculator = (TimeCalculator) session.getAttribute("timeCalculator");
			//arrêtdes threads---------------------------------------------------------------------------------------------------------------
			timeCalculator.stopPhaseThread();
			avanceTot.stopAvanceTotThread();
			
			
			//CHARGEMENT DU BEAN AVEC LES INFOS SUR LE TRAITEMENT FAIT PAR LE THREAD---------------------------------------------------------------
			if(timeCalculator.getNomPhaseEnCours() != null) {
				user.setPhaseEnCoursBean(timeCalculator.getNomPhaseEnCours());
				
			}else {
				user.setPhaseEnCoursBean("-");
			}
			
			user.setAvancementPhaseBean(timeCalculator.getAvancementPhase());
			user.setAvancementTotalBean(timeCalculator.getAvancementPhase());
			user.setAvancementTotalBean(avanceTot.getAvancementTotal());
			//ON CHARGE LA SESSION AVEC LE BEAN MIS A JOUR--------------------------------------------------------------------------------------------
			session.setAttribute("beanUser", user);
			session.setAttribute("timeCalculator", timeCalculator);
			session.setAttribute("avancementTotal", avanceTot);
			System.out.println("---------timeCalculator GETTERS---------PHASE: "+timeCalculator.getNomPhaseEnCours()+"----------AVANCEMENT: "+timeCalculator.getAvancementPhase());
			
			
			//message à retourner sur la jsp------------------------------------------------------------------------------------------------
			String messageServlet = "Activité interrompue";
	
			//chargement de la requ�te------------------------------------------------------------------------------------------------------
			request.setAttribute("messageServlet", messageServlet);	
			}
		
		
		
		//PHASE SUIVANTE---------------------------------------------------------------------------------------------------------------------
		if(request.getParameter("plus") != null) {
			
			//IMPORTATION DU TIMECALCULATOR DEPUIS LA SESSION ET INSTANCIATION------------------------------------------------------------------
			TimeCalculator timeCalculatorSession = (TimeCalculator) session.getAttribute("timeCalculator");
				
			timeCalculatorSession.stopPhaseThread();
			
			timeCalculatorSession = null;
			
			//IMPORTATION DU TIMECALCULATOR DEPUIS LA SESSION ET INSTANCIATION------------------------------------------------------------------
			TimeCalculator timeCalculator = new TimeCalculator();
			
			//LANCEMENT DU THREAD ET ENVOI DE LA LISTE DE BEAN DE PHASES AU TIMECALCULATOR----------------------------------------------------------------------------------------------------------
			timeCalculator.setListPhasesUser(listPhasesUserBean);
			timeCalculator.phaseChange(user.getIndiceEnCours()+1);
			
			
			//CHARGEMENT DU BEAN AVEC LES INFOS SUR LE TRAITEMENT FAIT PAR LE THREAD---------------------------------------------------------------
			if(timeCalculator.getNomPhaseEnCours() != null) {
				user.setPhaseEnCoursBean(timeCalculator.getNomPhaseEnCours());
				
			}else {
				user.setPhaseEnCoursBean("-");
			}
			
			user.setAvancementPhaseBean(timeCalculator.getAvancementPhase());
			
			System.out.println("---------timeCalculator GETTERS---------PHASE: "+timeCalculator.getNomPhaseEnCours()+"----------AVANCEMENT: "+timeCalculator.getAvancementPhase());
			//ON CHARGE LA SESSION AVEC LE BEAN MIS A JOUR ET LE TIMECALCULATOR--------------------------------------------------------------------------------------------
			session.setAttribute("beanUser", user);
			session.setAttribute("timeCalculator", timeCalculator);
			
			String messageServlet = "Phase suivante commencée" ;
			
			
			request.setAttribute("messageServlet", messageServlet);
						
			}
		
		
		//PHASE PRECEDENTE----------------------------------------------------------------------------------------------------------------------
		if(request.getParameter("moins") != null) {
			
			//IMPORTATION DU TIMECALCULATOR DEPUIS LA SESSION ET INSTANCIATION------------------------------------------------------------------
			TimeCalculator timeCalculatorSession = (TimeCalculator) session.getAttribute("timeCalculator");
				
			timeCalculatorSession.stopPhaseThread();
			
			timeCalculatorSession = null;
			
			//IMPORTATION DU TIMECALCULATOR DEPUIS LA SESSION ET INSTANCIATION------------------------------------------------------------------
			TimeCalculator timeCalculator = new TimeCalculator();
			
			//LANCEMENT DU THREAD----------------------------------------------------------------------------------------------------------
			timeCalculator.setListPhasesUser(listPhasesUserBean);
			timeCalculator.phaseChange(user.getIndiceEnCours()-1);
			
			
			//CHARGEMENT DU BEAN AVEC LES INFOS SUR LE TRAITEMENT FAIT PAR LE THREAD---------------------------------------------------------------
			if(timeCalculator.getNomPhaseEnCours() != null) {
				user.setPhaseEnCoursBean(timeCalculator.getNomPhaseEnCours());
				
			}else {
				user.setPhaseEnCoursBean("-");
			}
			
			user.setAvancementPhaseBean(timeCalculator.getAvancementPhase());
			
			//ON CHARGE LA SESSION AVEC LE BEAN MIS A JOUR--------------------------------------------------------------------------------------------
			session.setAttribute("beanUser", user);
			user.setAvancementPhaseBean(timeCalculator.getAvancementPhase());
			user.setPhaseEnCoursBean(timeCalculator.getNomPhaseEnCours());
			
			System.out.println("---------timeCalculator GETTERS---------PHASE: "+timeCalculator.getNomPhaseEnCours()+"----------AVANCEMENT: "+timeCalculator.getAvancementPhase());
			
			session.setAttribute("beanUser", user);
			session.setAttribute("timeCalculator", timeCalculator);
			String messageServlet = "Phase précédente commencée" ;
			
			
			request.setAttribute("messageServlet", messageServlet);

			}
		
		//IMPORTATION DU TIMECALCULATOR DEPUIS LA SESSION ET INSTANCIATION------------------------------------------------------------------
		TimeCalculator timeCalculator = (TimeCalculator) session.getAttribute("timeCalculator");
		AvancementTotal avanceTot = (AvancementTotal) session.getAttribute("avancementTotal");
		
		user.setAvancementPhaseBean(timeCalculator.getAvancementPhase());
		user.setAvancementTotalBean(avanceTot.getAvancementTotal());
		user.setPhaseEnCoursBean(timeCalculator.getNomPhaseEnCours());
		
		System.out.println("---------timeCalculator GETTERS---------PHASE: "+timeCalculator.getNomPhaseEnCours()+"----------AVANCEMENT: "+timeCalculator.getAvancementPhase());
		
		session.setAttribute("beanUser", user);
		session.setAttribute("timeCalculator", timeCalculator);
		session.setAttribute("avancementTotal", avanceTot);
    	request.setAttribute("nomphase", user.getPhaseEnCoursBean());
    	request.setAttribute("avancementTotal", user.getAvancementTotalBean());
    	request.setAttribute("avancement", user.getAvancementPhaseBean());
		
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/Run.jsp").forward(request, response);
	
		
		
	}

}