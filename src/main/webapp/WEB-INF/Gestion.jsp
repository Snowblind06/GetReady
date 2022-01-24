<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>

<html>

<head>

<meta charset="utf-8"/>
<link type="text/css" rel="stylesheet" href="/GetReady/style.css"/>
<title>Get Ready: Gestion</title>


</head>
<body>

<form method="post" action="Gestion">
<div class="login">
	
	
	
		<table class="wrapper">
			
			<tr class="title2">
				<th></th>
				<th>Actuel</th>
				<th>Nouveau</th>
			</tr>
			
			
			<tr>
				<td>Nom d'Utilisateur</td>
				<td ><c:out value="${userBean.id}"></c:out></td>
				<td></td>
			</tr>
			
			
			<tr>
				<td>Activité</td>
				<td><c:out value="${userBean.activite}"></c:out></td>
				<td>
					<input type="text" name="nouveauActivite"/>
					<input type="submit" name="modifActivite" value="Modifier"/>
				</td>
			</tr>
			

			<tr>
				<td>Mot de passe</td>
				<td></td>
				<td>
					<input type="password" name="nouveauPassword"/>
					<input type="submit" name="modifPassword" value="Modifier"/>
				</td>
			</tr>
			
			
			<tr>
				<td>Email</td>
				<td><c:out value="${userBean.email}"></c:out></td>
				<td>
					<input type="email" name="nouveauEmail"/>
					<input type="submit" name="modifEmail" value="Modifier"/>
				</td>
			</tr>
			
			
			<tr>
				<td colspan="3"	> </td>
			</tr>
			
			
			<tr>
				<td colspan="3"	></td>
			</tr>
			
			
			<tr>
				<td colspan="3"	> </td>
			</tr>

		</table>
</div>	

	<p>${messageUser }</p>

</form>

<div class="supp">
	<form method="post" action="Gestion">
		<p>Voulez-vous supprimer votre compte? Si oui, cochez la case puis acceptez</p>
		<p><input type="checkbox" name="suppVerif"/>
		<input type="submit" name="suppUser" value="Supprimer"/></p>
	</form>
</div>

<p><a href="http://localhost:8080/GetReady/Menu"><button class="bn28">OK</button></a></p>


</body>
</html>