use cs175DB
drop table GeneratedReview2;
CREATE TABLE IF NOT EXISTS GeneratedReview2(
	Id INT PRIMARY KEY AUTO_INCREMENT,
	Stars INT,
	Category VARCHAR(767),
	gen_review_text_withPOS LONGTEXT,
	gen_review_text_NoPOS LONGTEXT,
	rand_review_text LONGTEXT
	);
