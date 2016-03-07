package webStuff;

import java.io.IOException;
import java.io.PrintWriter;

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
	private static ReviewGetter myGetter;
       
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String randomReview = null;
		while (randomReview == null)
		{
			myGetter.setRandomCategory();
			myGetter.setRandomStarRating();
			randomReview = myGetter.getRandomReviewFromDB();
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();	
		String title = "Evaluation Pages";
		  String docType =
		  "<!doctype html public \"-//w3c//dtd html 4.0 " +
		  "transitional//en\">\n";
		  out.println(docType +
	            "<html>\n" +
		        "<head><title>" + title + "</title></head>\n" +
		        "<body bgcolor=\"#f0f0f0\">\n" +
		        "<h1 align=\"center\">" + title + "</h1>\n" +
		        "<FORM ACTION=\"QuestionPage\" METHOD=\"post\">\n"+
				"<fieldset>\n" +
				"<legend>Review 1:</legend>\n" +
		        "<p>" + randomReview + "</p>\n" +
		        "<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"dbReview\" CHECKED>\n" +
		        "Review 1\n" +
		        "<BR>\n" +
		        "</fieldset>\n" +
		        "<fieldset>\n" +
		        "<legend>Review 2:</legend>\n" +
		        "<p>" + myGetter.generateReview() + "</p>\n" +
		        "<INPUT TYPE=\"radio\" NAME=\"reviewChoice\" VALUE=\"geReview\">\n" +
		        "Review 2\n" +
		        "<BR>\n" +
		        "<INPUT TYPE=\"submit\" VALUE=\"Submit\">\n" +
		        "</fieldset>\n" +
		        "</FORM>\n" +	                
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
