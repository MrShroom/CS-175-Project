package webStuff;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MySQLToBagOfWords.BagOfWordUtilites;

/**
 * Servlet implementation class QuestionPage
 */
@WebServlet(description = "Page to Dynamicly ask reviews", urlPatterns = { "/QuestionPage" })
public class QuestionPage extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static ReviewGetter myGetter;
	private static ReviewGetter myGetter2;
       
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
		
		myGetter = new ReviewGetter();
		myGetter2 = new ReviewGetter();
		// TODO Auto-generated method stub
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
		String randomReview = null;
		String genReview = null;
		String title = "Evaluation Pages";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
		
		//record last responses
		if(number > 1 && number <=4)
		{
			myGetter.submittFeedback("DataBaseReviewVsPOSGen",Integer.parseInt(LastAns));
		}
		else if(number > 4 && number <=7)
		{
			myGetter.submittFeedback("DataBaseReviewVsNoPOSGen",Integer.parseInt(LastAns));
		}
		else if(number > 7 && number <=11)
		{
			myGetter.submittFeedback("POSGenVsNoPOSGen",Integer.parseInt(LastAns));
		}
		
		//redirect on last question
		if(number == 11)
		{
			 response.sendRedirect("EndPage.jsp");  
		}
		
		//get reviews
		while (randomReview == null)
		{
			myGetter.setRandomCategory();
			myGetter2.setCurrentCategory(myGetter.getCurrentCategory());
			myGetter.setRandomStarRating();
			myGetter2.setStarRatinge(myGetter.getStarRatinge());
			
			Set<String> temp = new HashSet<String>();
			temp.add(myGetter.getCurrentCategory());
			Set<Integer> temp2 = new HashSet<Integer>();
			temp2.add(myGetter.getStarRatinge());
			
			if(BagOfWordUtilites.countSetOfReviews(temp,temp2) < 100)
				continue;			
			randomReview = myGetter.getRandomReviewFromDB();
		}
		
		if(number >= 1 && number < 4)
		{
			myGetter.usePOS(true);
		}
		else if(number >= 4 && number < 7)
		{
			myGetter.usePOS(false);
		}
		else if(number >= 7 && number <=10)
		{
			myGetter2.usePOS(true);
			myGetter.usePOS(false);
			
			randomReview = myGetter2.getGenerateReview();
			if(randomReview == null)
				randomReview = myGetter2.generateReview();
		}		
		
		genReview = myGetter.getGenerateReview();
		if(genReview == null)
			genReview = myGetter.generateReview();		
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();	
		out.println(docType +
			"<html>\n" +
			"<head><title>" + title + "</title></head>\n" +
			"<body bgcolor=\"#f0f0f0\">\n" +
			"<h1 align=\"center\">" + title + "</h1>\n" +
			"<h2 align=\"center\">Question" + number + "</h2>\n" +
			"<FORM ACTION=\"QuestionPage\" METHOD=\"post\">\n"+
			"<fieldset>\n" +
			"<legend>Review 1:</legend>\n" +
			"<p>" + randomReview + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"1\" CHECKED>\n" +
			"Review 1\n" +
			"<BR>\n" +
			"</fieldset>\n" +
			"<fieldset>\n" +
			"<legend>Review 2:</legend>\n" +
			"<p>" + genReview + "</p>\n" +
			"<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"2\">\n" +
			"Review 2\n" +
			"<BR>\n" +
			"</fieldset>\n" +
			"<INPUT type=\"hidden\" NAME=\"questNum\" VALUE=\"" + (number+1) + "\" checked/>" +
			"<INPUT TYPE=\"submit\" VALUE=\"Submit\">\n" +
			"</FORM>\n" +	   
			//"<p>" + request.getParameter("reviewChoice") + "</p>"+
			"</body></html>");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
