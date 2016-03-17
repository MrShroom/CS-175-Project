from MySQLtoBagOfWordsPython.MySQLToBagOfWords import BagOfWordUtilites as bowu
import matplotlib.pyplot as plt
import numpy as np
import operator

def createAnddisplayCatHisto():
    data = bowu.getSetOfCatagoriesWithCounts(10)
    X = np.arange(len(data))
    plt.bar(X, data.values(), align='center', width=0.5)
    plt.xticks(X, data.keys())
    ymax = max(data.values()) + 1
    plt.ylim(0, ymax)
    plt.title("Number of Review per Categorys ")
    plt.xlabel("Category")
    plt.ylabel("Number of Reviews")
    plt.show()


if __name__ == "__main__":
    createAnddisplayCatHisto()