<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
	pageEncoding="ISO-8859-1"%>
	
<%-- <%@ page import="WebStuff.ReviewGetter" %> --%>

<%-- <% ReviewGetter mygetter = new ReviewGetter();%> --%>
<!--% mygetter.setRandomCategory();%-->
<!--% mygetter.setRandomStarRating();%-->


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Evaluation Page</title>
	</head>	
	<body>			
		<FORM ACTION="formAction.jsp" METHOD="post">
		<fieldset>
    		<legend>Review 1:</legend>
<%--     		 <p><% out.print(mygetter.getRandomReviewFromDB());%></p> --%>
	             <INPUT TYPE="radio" NAME="radios" VALUE="review1" CHECKED>
	             Review 1
	            <BR>
	    </fieldset>
	    <fieldset>
    		<legend>Review 1:</legend>
<%--     		 <p><% out.print(mygetter.generateReview());%></p> --%>
	            <INPUT TYPE="radio" NAME="radios" VALUE="review2">
	             Review 2
	            <BR>
	            <INPUT TYPE="submit" VALUE="Submit">
	   	</fieldset>
        </FORM>
	
	</body>	
</html>