<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>

<html>

<head>
<meta name=”viewport” content=”width=device-width, initial-scale=1″>
<meta charset="utf-8"/>
<link type="text/css" rel="stylesheet" href="/GetReady/style.css"/>
<title>Get Ready: Menu</title>


</head>


<body>



<nav class="bn28">

	<form method="post" action="Menu">
		<input class="bn27" type="submit" value="Gérer le compte" name="gestion"/>
		<input class="bn27" type="submit" value="Déconnexion" name="deconnexion"/>
	</form>

</nav>


	<div class="titreMenu">
		<h1>${activiteName}</h1><br>
	</div>
	
	
	<!-- TABLEAU AFFICHAGE DB-->	
	<section class="wrapper">
	
    <!-- Row title -->
    <main class="row title">
    
    <!-- LIGNE DES TITRES DE COLONNES -->
      <ul>
        <li>N° Etape</li>
        <li>Etape</li>
        <li>Debut</li>
        <li>Fin</li>
        <li>Durée</li>
      </ul>
    </main>
	
	
	<!--  BOUCLE D'AFFICHE DES LIGNES DE LA DB -->
	<c:forEach items="${activite}" var="phase" varStatus="compte">
	
	<!-- CORPS DU TABLEAU DYNAMIQUE -->
	  <section class="row-fadeOut-wrapper">
      <article class="row fadeIn nfl">
      
      <!-- INTEGRATION DES LIGNES DE LA DB -->
        <ul>
          <li><c:out value="${phase.indice}"/></li>
          <li><c:out value="${phase.leNom}"/></li>
          <li><c:out value="${phase.debut}"/></li>
          <li><c:out value="${phase.fin}"/></li>
          <li><c:out value="${phase.dureephase}"/></li>
         </ul>
         
        <!-- BOUTON SUPPRESSION DE LIGNE -->
        <ul class="more-content">
          <li><form method="post" action="Menu" >
          <input id="boutonsModifSuppTable" type="submit" name="suppr" value="Supp. phase: ${phase.indice}"></form></li>
        </ul>
         
     </article>
    </section>
    <!-- FIN TABLEAU DYNAMIQUE -->
	</c:forEach>
	
	<!-- TEST AJOUT LIGNE FIXE -->
	<section class="row-fadeIn-wrapper">
	<article class="row fadeOut nfl">
	  <form method="post" action="Menu" id="formParamMenu">
	  <ul>
        <li><input type="number" name="numero" id="numero" required min="1"/></li>
        <li><input type="text" name="NomPhase" id="NomPhase" required /></li>
        <li><input type="time" name="DebutPhase" id="DebutPhase" value="now" required/></li>
        <li><input type="time" name="FinPhase" id="FinPhase" value="now" required/></li>
        <li><input type="submit" value="OK" name="phaseForm" id="ajoutModif" required/></li>
      </ul>
      </form>
      </article>
	</section>
	</section>
	
	<!-- MESSAGE D'ERREUR DE SAISIE FORMULAIRE -->
	<div class="messageSaisie">
		<c:out value="${messageSaisie }"/>
	</div>
	
	<a href="http://www.alexandre-piquelin.fr/GetReady/Run"><button class="bn29">GO!</button></a>

<p></p>




</body>




</html>