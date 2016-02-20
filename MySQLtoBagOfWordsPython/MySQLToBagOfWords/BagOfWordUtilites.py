import mysql.connector
import mysql.connector
import nltk

config = {
  'user': 'user2',
  'password': 'password',
  'host': 'shaunmcthomas.me',
  'database': 'cs175DB',
  'raise_on_warnings': True,
}


def getBagOfWords(categories, stars, maxNumberOfReviewToUse):
    reviews = getSetOfReviews(categories, stars, maxNumberOfReviewToUse)
    output = {} #string:int
    for currentReview in reviews:
        for token in nltk.tokenize(currentReview):
            if token in output.keys():
                output[token]+=1
            else:
                output[token] = 1
    return output

def getBagOfWordsNormilaized(categories, stars, maxNumberOfReviewToUse):
    bow = getBagOfWords(categories, stars, maxNumberOfReviewToUse)
    totalNumberOfWords = 0
    for i in range(len(bow.keys())):
        totalNumberOfWords+=1
    for word in bow:
        bow[word] = bow[word]/totalNumberOfWords
    return bow

def getSetOfReviews(categories, stars, maxNumberOfReviewToUse):
    statement = "SELECT review_text FROM reviews "
    whereAdded = False
    if(stars):
        statement += " WHERE "
        whereAdded = True
        for strs in stars:
            statement = statement + " reviews.stars=" + str(strs) + " AND "
        statement = statement[0:statement.rfind(" AND ")]
    if(categories):
        if (not whereAdded):
            statement += " WHERE "
        else:
            statement += " AND "
        statement += " business_id IN (SELECT business_id FROM is_in_catagory WHERE "
        whereAdded = True
        for cat in categories:
            statement = statement + " is_in_catagory.category=\"" + str(cat) + "\" AND "
        statement = statement[0:statement.rfind(" AND ")]
        statement += " )"
    if(maxNumberOfReviewToUse > 0):
            statement = statement + " LIMIT " + str(maxNumberOfReviewToUse)
    statement += " ;"
    cnx = mysql.connector.connect(**config)
    cursor = cnx.cursor()
    print(statement)
    cursor.execute(statement)
    output = set()
    for (review_text) in cursor:
        output.add(review_text)
    cnx.close()
    return output

def getSetOfCatagories(numberOfCategories):
    statement = "SELECT category, count(business_id) AS cnt FROM is_in_catagory Group BY category ORDER BY cnt DESC"
    if (numberOfCategories > 0):
        statement = statement + " LIMIT " + str (numberOfCategories)
    statement += ";"
    cnx = mysql.connector.connect(**config)
    cursor = cnx.cursor()
    cursor.execute(statement)
    output = set()
    for (category) in cursor:
        output.add(category)
    cnx.close()
    return output



if __name__ == "__main__":
    #print (getSetOfCatagories(1))
    #print(getSetOfReviews(getSetOfCatagories(2), {1}, 2))
    print(getBagOfWords(getSetOfCatagories(3), {1,2,3,4,5}, 5))