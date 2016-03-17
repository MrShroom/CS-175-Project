use cs175DB

CREATE TABLE IF NOT EXISTS EvaluateTable(
	Id VARCHAR(255) PRIMARY KEY,
	review1 INT,
	review2 INT
	);

drop TABLE GeneratedReview;
CREATE TABLE IF NOT EXISTS GeneratedReview(
	Id INT PRIMARY KEY AUTO_INCREMENT,
	Stars INT,
	Category VARCHAR(767),
	POS BOOLEAN,
	review_text LONGTEXT
	);
