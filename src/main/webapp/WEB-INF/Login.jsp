<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>

<meta charset="utf-8"/>
<link type="text/css" rel="stylesheet" href="/GetReady/style.css"/>

<title>Get Ready: Connection</title>
</head>


<body>

	
<div class="login">	
	
<form method="post" action="Login">

	<table class="wrapper">
	
		<tr  class="title2">
			<th>CONNEXION</th>
		</tr>	
		
		<tr class="row2">
			<td> </td>
		</tr>
		
		<tr class="texteChamps">
			<td>Identifiant </td>			
		</tr>
	
		<tr class="texteChamps">
			<td><input type="text" name="id"/></td>
		</tr>
		
		<tr class="texteChamps">
			<td>Mot de Passe</td>	
		</tr>
		
		<tr class="texteChamps">
			<td><input class="password" type="password" name="password"/></td>
		</tr>
		
		<tr class="row2">
			<td> </td>
		</tr>
		
		<tr>
			<td class="boutonsConn"><input type="submit" name="Connection"	value="Connexion" class="bn26"/></td>
		</tr>
				
	
		
		<tr class="title2">
			<th>CREER COMPTE</th>
		</tr>
		
		
		<tr class="row2">
			<td> </td>
		</tr>
		
	
		<tr class="texteChamps">
			<td>Identifiant</td>			
		</tr>

		<tr class="texteChamps">
			<td>
				<input type="text" name="idCreer"/>
			</td>	
		</tr>
	
		<tr class="texteChamps">
			<td >Mot de Passe</td>
		</tr>
		
		<tr class="texteChamps">
			<td>
				<input class="password" type="password" name="passwordCreer"/>
			</td>
		</tr>
		
		<tr class="texteChamps">
			<td>Nom de votre Activite</td>
		</tr>
		
		<tr class="texteChamps">
			<td><input type="text" name="activiteName"/></td>
		</tr>
		
		<tr class="texteChamps">
			<td>Votre Email</td>
		</tr>
		
		<tr class="texteChamps">
			<td><input type="email" name="emailUser"/></td>
		</tr>
		
		<tr class="row2">
			<td> </td>
		</tr>
		
		
		<tr>
			<td class="boutonsConn"><input type="submit" name="ConnectionCreer"	value="S'enregistrer" class="bn26"/></td>
		</tr>
		

	</table>
	</form>	

	<div class="message">
		<p><c:out value="${messageErreur}"/></p>
		<p><c:out value="${messageUser}"/></p>
		<p><c:out value="${champsVides }"/></p>
	</div>	
</div>

</body>

</html>