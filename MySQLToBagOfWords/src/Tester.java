import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import MySQLToBagOfWords.BagOfWordUtilites;;

public class Tester {

	public static void main(String[] args) 
	{
		Set<String> categories = new HashSet<String>();
		categories.add("Restaurants");
		Set<Integer> stars = new HashSet<Integer>();
		stars.add(1);
		Integer maxNumberOfReviewToUse = 0;
		Set<String> temp = BagOfWordUtilites.getSetOfReviews(categories, stars, maxNumberOfReviewToUse);
		System.out.println(temp.size());
		Map<String, Integer> temp2 = BagOfWordUtilites.getBagOfWords(categories, stars, maxNumberOfReviewToUse);
		System.out.println(temp2.size());
		Map<String, Double> temp3 = BagOfWordUtilites.getBagOfWordsNormilaized(categories, stars, maxNumberOfReviewToUse);
		System.out.println(temp3.size());
		temp = BagOfWordUtilites.getSetOfCatagories(0);
		System.out.println(temp.size());
		// TODO Auto-generated method stub

	}

}
