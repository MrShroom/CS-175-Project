package webStuff;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import MySQLToBagOfWords.BagOfWordUtilites;
import textgen.Generator;
import textgen.GeneratorNoPOS;
import textgen.GeneratorPOS;

public class reviewGreneratortoDB {

	public static void main(String[] args) {
		Connection dBConnects = BagOfWordUtilites.setMySQLDB();
		Set<String> cats = BagOfWordUtilites.getSetOfCatagories(0);
		Generator myGenerator = new GeneratorPOS();
		String genReview;

		for (String currentCat : cats) {
			for (int i = 1; i <= 5; i++) {
				try {
					myGenerator.setCategory(currentCat);
					myGenerator.setStar(i);
					myGenerator.setReviewCount(1000);
					myGenerator.setNgramSize(3);
					myGenerator.train();
					genReview = myGenerator.generateReview();
					String statement = "INSERT INTO GeneratedReview (Stars , Category, POS , review_text)"
							+ " VALUES ( ?, ?, TRUE , ? );";
					try {
						java.sql.PreparedStatement preparedStatement = dBConnects.prepareStatement(statement);
						preparedStatement.setLong(1, i);
						preparedStatement.setString(2, currentCat);
						preparedStatement.setString(3, genReview);
						preparedStatement.executeUpdate();

					} catch (SQLException e) {
						e.printStackTrace();
					}
				} catch (Exception ex) {
					continue;
				}

			}
		}
		
		myGenerator = new GeneratorNoPOS();
		for (String currentCat : cats) {
			for (int i = 1; i <= 5; i++) {
				try {
					myGenerator.setCategory(currentCat);
					myGenerator.setStar(i);
					myGenerator.setReviewCount(1000);
					myGenerator.setNgramSize(3);
					myGenerator.train();
					genReview = myGenerator.generateReview();
					String statement = "INSERT INTO GeneratedReview (Stars , Category, POS , review_text)"
							+ " VALUES ( ?, ?, FALSE , ? );";
					try {
						java.sql.PreparedStatement preparedStatement = dBConnects.prepareStatement(statement);
						preparedStatement.setLong(1, i);
						preparedStatement.setString(2, currentCat);
						preparedStatement.setString(3, genReview);
						preparedStatement.executeUpdate();

					} catch (SQLException e) {
						e.printStackTrace();
					}
				} catch (Exception ex) {
					continue;
				}

			}
		}

		try {
			dBConnects.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
