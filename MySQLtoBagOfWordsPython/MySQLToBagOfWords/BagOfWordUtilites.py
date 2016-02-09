import mysql.connector
import mysql.connector

config = {
  'user': 'user2',
  'password': 'password',
  'host': 'shaunmcthomas.me',
  'database': 'cs175DB',
  'raise_on_warnings': True,
}


def getBagOfWords(categories, stars, maxNumberOfReviewToUse):
    pass

def getBagOfWordsNormilaized(categories, stars, maxNumberOfReviewToUse):
    pass

def getSetOfReviews(categories, stars, maxNumberOfReviewToUse):
    statement = "SELECT review_text FROM reviews "
    whereAdded = False
    if( stars):
        statement += statement + " WHERE "
        whereAdded = True
        for strs in stars:
            statement = statement + " reviews.stars=" + strs + " AND "
        statement = statement.substring(0,statement.lastIndexOf(" AND "))
    if(categories ):
        if (not whereAdded):
            statement = statement + " WHERE "
        else:
			statement = statement + " AND "
        statement = statement + " business_id IN (SELECT business_id FROM is_in_catagory WHERE "
        whereAdded = True
        for cat in categories:
            statement = statement + " is_in_catagory.category=\'" + cat + "\' AND "
        statement = statement.substring(0,statement.lastIndexOf(" AND "))
        statement = statement + " )"
	if(maxNumberOfReviewToUse > 0):
            statement = statement + " LIMIT " + maxNumberOfReviewToUse
	statement = statement + " ;"
    cnx = mysql.connector.connect(**config)
    cursor = cnx.cursor()
    cursor.execute(statement)
    output = set()
    for (review_text) in cursor:
        output.add(review_text)
    cnx.close()
    return output

def getSetOfCatagories(numberOfCategories):
    statement = "SELECT category, count(business_id) AS cnt  FROM is_in_catagory Group BY category ORDER BY cnt DESC"
    if (numberOfCategories > 0):
        statement = statement + " LIMIT " + str ( numberOfCategories)
    statement = statement + ";"
    cnx = mysql.connector.connect(**config)
    cursor = cnx.cursor()
    cursor.execute(statement)
    output = set()
    for (category) in cursor:
        output.add(category)
    cnx.close()
    return output



if __name__ == "__main__":
    print (getSetOfCatagories(1))
    # print(getSetOfReviews(getSetOfCatagories(2), {1}, 2))