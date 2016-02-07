CREATE DATABASE IF NOT EXISTS cs175DB;

use cs175DB

DROP TABLE IF EXISTS  is_in_catagory, reviews, businesses;
	
CREATE TABLE IF NOT EXISTS businesses(
	business_id VARCHAR(767) PRIMARY KEY, 
    name VARCHAR(767),
	stars DOUBLE,
	review_count INT 
	);

CREATE TABLE IF NOT EXISTS is_in_catagory(
	business_id VARCHAR(767) , 
    category VARCHAR(767),
	FOREIGN KEY (business_id) REFERENCES businesses(business_id)
        ON DELETE CASCADE,
	PRIMARY KEY(business_id, category)
	);

CREATE TABLE IF NOT EXISTS reviews(
	review_id VARCHAR(767) PRIMARY KEY,
	review_type VARCHAR(767),
	business_id VARCHAR(767),
	stars INT,
	review_text LONGTEXT,
	FOREIGN KEY (business_id) REFERENCES businesses(business_id)
        ON DELETE CASCADE		
	);
