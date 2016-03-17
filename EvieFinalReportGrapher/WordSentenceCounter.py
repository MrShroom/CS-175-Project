import matplotlib.pyplot as plt
import MySQLtoBagOfWordsPython.MySQLToBagOfWords.BagOfWordUtilites as bow




def wordCount(text):
    return len(text.split())
def sentenceCount(text):
    period_split = text.split(".")
    exclamation_split = []
    [exclamation_split.extend(s.split("!")) for s in period_split]
    question_split = []
    [question_split.extend(s.split("?")) for s in exclamation_split]
    return len(question_split)-1


if __name__ == "__main__":
    stars = (1,2,3,4,5)
    number_of_reviews = 5
    word_averages = []
    sentence_averages = []
    for star in stars:
        reviews = bow.getReviews(number_of_reviews, star)
        word_average = 0
        sentence_average = 0
        for r in reviews:
            word_average += wordCount(r)
            sentence_average += sentenceCount(r)
        word_averages.append(word_average / number_of_reviews)
        sentence_averages.append(sentence_average / number_of_reviews)
    print(word_averages)
    print(sentence_averages)

    bar_width = .75

    plt.title("Average Word Count Per Review by Star Rating")
    plt.xlabel("Star Rating")
    plt.ylabel("Number of Words")
    plt.xticks(tuple(x+.5*bar_width for x in stars), ("1", "2", "3", "4", "5"))
    plt.bar(tuple(x for x in stars), word_averages, bar_width, color="b")
    plt.show()
    plt.cla()

    plt.title("Average Sentence Count Per Review by Star Rating")
    plt.xlabel("Star Rating")
    plt.ylabel("Number of Sentences")
    plt.xticks(tuple(x+.5*bar_width for x in stars), ("1", "2", "3", "4", "5"))
    plt.bar(tuple(x for x in stars), sentence_averages, bar_width, color="b")
    plt.show()
    plt.cla()