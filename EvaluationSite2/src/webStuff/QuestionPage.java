package webStuff;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QuestionPage
 */
@WebServlet(description = "Page to Dynamicly ask reviews", urlPatterns = { "/QuestionPage" })
public class QuestionPage extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static ReviewGetterVersion2 myGetter = new ReviewGetterVersion2();
	private static Random rnd = new Random();  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionPage() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException 
	{
		
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String questNum = request.getParameter("questNum");
		String LastAns = request.getParameter("reviewChoice"); 
		int number = questNum != null ? Integer.parseInt(questNum.toString()):1;
		ReviewCompariosnClass reviews = myGetter.getGenerateReview(); 
		String title = "Evaluation Pages";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
		
		//record last responses
		if(number > 1 && number <=4)
		{
			myGetter.submittFeedback("DataBaseReviewVsPOSGenV3",Integer.parseInt(LastAns));
		}
		else if(number > 4 && number <=7)
		{
			myGetter.submittFeedback("DataBaseReviewVsNoPOSGenV3",Integer.parseInt(LastAns));
		}
		else if(number > 7 && number <=11)
		{
			myGetter.submittFeedback("POSGenVsNoPOSGenV3",Integer.parseInt(LastAns));
		}
		
		//redirect on last question
		if(number == 11)
		{
			 response.sendRedirect("EndPage.jsp");  
		};		
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();	
		out.println(docType +
			"<html>\n" +
			"<head><title>" + title + "</title></head>\n" +
			"<body bgcolor=\"#f0f0f0\">\n" +
			"<h1 align=\"center\">" + title + "</h1>\n" +
			"<h2 align=\"center\">Question " + number + " of 10</h2>\n" +
			"Category = \"" + reviews.getCatergory() + "\", Star Rating = " + reviews.getStarRating() +
			"<FORM ACTION=\"QuestionPage\" METHOD=\"post\">\n"+
			"<fieldset>\n" +
			"<legend><b>Review 1:</b></legend>\n");
		if(rnd.nextBoolean())
			printReviewOneFirst(out, number, reviews);
		else
			printReviewTwoFirst(out, number, reviews);
			out.print("<b>Review 2</b>\n" +
			"<BR>\n" +
			"</fieldset>\n" +
			"<INPUT type=\"hidden\" NAME=\"questNum\" VALUE=\"" + (number+1) + "\" checked/>" +
			"<INPUT TYPE=\"submit\" VALUE=\"Submit\">\n" +
			"</FORM>\n" +
			"</body></html>");
		
	}
	
	public void printReviewOneFirst(PrintWriter out, int number, ReviewCompariosnClass reviews)
	{
		
		if(number < 7)
		{
			out.println("<p>" + reviews.getReviewYelp() + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"1\" CHECKED>\n");
		}
		else 
		{		
			out.println("<p>" + reviews.getReviewPOS() + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"1\" CHECKED>\n");
		}
		out.print("<b>Review 1</b>\n" +
				"<BR><BR>\n" +
				"</fieldset>\n" +
				"<fieldset>\n" +
				"<legend><b>Review 2:</b></legend>\n");
		if(number < 4 )
		{
			out.println("<p>" + reviews.getReviewPOS() + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"2\" CHECKED>\n");
		}
		else if(number < 7 )
		{		
			out.println("<p>" + reviews.getReviewNoPOS() + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"2\" CHECKED>\n");
		}
		else
		{
			out.println("<p>" + reviews.getReviewNoPOS() + "</p>\n" +
		"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"2\" CHECKED>\n");
		}
		
	}
	
	public void printReviewTwoFirst(PrintWriter out, int number, ReviewCompariosnClass reviews)
	{
		if(number < 4 )
		{
			out.println("<p>" + reviews.getReviewPOS() + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"2\" CHECKED>\n");
		}
		else if(number < 7 )
		{		
			out.println("<p>" + reviews.getReviewNoPOS() + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"2\" CHECKED>\n");
		}
		else
		{
			out.println("<p>" + reviews.getReviewNoPOS() + "</p>\n" +
		"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"2\" CHECKED>\n");
		}
		out.print("<b>Review 1</b>\n" +
				"<BR><BR>\n" +
				"</fieldset>\n" +
				"<fieldset>\n" +
				"<legend><b>Review 2:</b></legend>\n");
		if(number < 7)
		{
			out.println("<p>" + reviews.getReviewYelp() + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"1\" CHECKED>\n");
		}
		else 
		{		
			out.println("<p>" + reviews.getReviewPOS() + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"1\" CHECKED>\n");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
