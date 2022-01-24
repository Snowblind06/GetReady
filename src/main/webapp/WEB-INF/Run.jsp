<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<!DOCTYPE html>

<html>

<head>
	<link type="text/css" rel="stylesheet" href="/GetReady/style.css"/>

	<meta charset="utf-8" HTTP-EQUIV="Refresh" CONTENT="2">
	<title>GetReady ${nomphase}</title>	
</head>



<body>

<c:out value="${alerte}"/>

<div id="page-wrap">

	<div class="horizon">
		
 		<h1>${nomphase}</h1>

  		<pre>${avancement}%</pre>
  		
  		<form method="post" action="Run">	

			<div class="frame">
				<input class="btn" style="font-family: FontAwesome" type="submit" name="moins" id="moins" value="&#xf049;"/>
				<input class="btn" style="font-family: FontAwesome" type="submit" name="go" id="go" value="&#xf04b;" />
				<input class="btn" style="font-family: FontAwesome" type="submit" name="stop" id="stop" value="&#xf04d;"/>
				<input class="btn" style="font-family: FontAwesome" type="submit" name="plus" id="plus" value="&#xf050;"/>
			</div>
			
		</form>
		
	</div>

	<div class="meter animate">
  		<span style="width: ${avancement}%"><span></span></span>
	</div>


  	<h2><c:out value="${messageServlet}" /></h2>
  	
</div>
 

<div id="page-wrap">
	<div class="meter2 animate">

    	<span style="width: ${avancementTotal}%" ><span></span></span>
    
	  
	</div>
</div>

<a href="http://localhost:8080/GetReady/Menu"><button class="bn28">Menu</button></a>


</body>
</html>