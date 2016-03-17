package webStuff;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import MySQLToBagOfWords.BagOfWordUtilites;
import textgen.generators.*;

public class reviewGreneratortoDB {

	public static void main(String[] args) {
		Connection dBConnects;
		Set<String> cats = BagOfWordUtilites.getSetOfCatagories(0);
		Generator myGenerator = new GeneratorPOS2();
		Generator myGeneratorNoPOS = new GeneratorNoPOS();

		String genReview, genReviewNoPOS;
		ReviewGetter myGetter = new ReviewGetter();
		for (int j = 0; j < 5; j++) {
			for (String currentCat : cats) {
				for (int i = 1; i <= 5; i++) {
					Set<String> temp = new HashSet<String>();
					temp.add(currentCat);
					Set<Integer> temp2 = new HashSet<Integer>();
					temp2.add(i);
					if (BagOfWordUtilites.countSetOfReviews(temp, temp2) < 100)
						continue;

					try {
						myGenerator.setCategory(currentCat);
						myGenerator.setStar(i);
						myGenerator.setReviewCount(0);
						myGenerator.setNgramSize(3);
						myGenerator.train();
						genReview = myGenerator.generateReview();

						myGeneratorNoPOS.setCategory(currentCat);
						myGeneratorNoPOS.setStar(i);
						myGeneratorNoPOS.setReviewCount(0);
						myGeneratorNoPOS.setNgramSize(3);
						myGeneratorNoPOS.train();
						genReviewNoPOS = myGeneratorNoPOS.generateReview();

						myGetter.setCurrentCategory(currentCat);
						myGetter.setStarRatinge(i);

						String statement = "INSERT INTO GeneratedReview2 (Stars , Category, gen_review_text_withPOS , gen_review_text_NoPOS, rand_review_text )"
								+ " VALUES ( ?, ?, ? , ?, ? );";
						try {
							dBConnects = BagOfWordUtilites.setMySQLDB();
							java.sql.PreparedStatement preparedStatement = dBConnects.prepareStatement(statement);
							preparedStatement.setLong(1, i);
							preparedStatement.setString(2, currentCat);
							preparedStatement.setString(3, genReview);
							preparedStatement.setString(4, genReviewNoPOS);
							preparedStatement.setString(5, myGetter.getRandomReviewFromDB());
							preparedStatement.executeUpdate();
							dBConnects.close();

						} catch (SQLException e) {
							e.printStackTrace();
						}
					} catch (Exception ex) {
						continue;
					}

				}
			}

		}

	}
}
