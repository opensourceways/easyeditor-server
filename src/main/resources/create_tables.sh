HOSTNAME="127.0.0.1"
PORT="3306"
USERNAME="root"
PASSWORD="root"

DBNAME="easyedit"

mysql -h${HOSTNAME} -P${PORT} -u${USERNAME} -p${PASSWORD} -e "CREATE DATABASE ${DBNAME} DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

use ${DBNAME};

-- ------------------------
-- Table structure for page
-- ------------------------
CREATE TABLE page(
  id INT NOT NULL AUTO_INCREMENT,
  path varchar(255) NOT NULL,
  name varchar(255) DEFAULT NULL,
  hash varchar(255) DEFAULT NULL,
  title varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  is_private tinyint(1) NOT NULL,
  is_published tinyint(1) NOT NULL,
  publish_start_date varchar(255) NOT NULL,
  publish_end_date varchar(255) NOT NULL,
  content longtext DEFAULT NULL,
  content_type varchar(255) NOT NULL,
  created_at varchar(255) NOT NULL,
  updated_at varchar(255) NOT NULL,
  author_name varchar(255) NOT NULL,
  creator_name varchar(255) NOT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ----------------------------
-- Table structure for page tree
-- ----------------------------
CREATE TABLE page_tree(
  id INT NOT NULL AUTO_INCREMENT,
  path varchar(255) NOT NULL,
  depth INT NOT NULL,
  title varchar(255) NOT NULL,
  is_event tinyint(1) NOT NULL,
  is_private tinyint(1) NOT NULL,
  is_folder tinyint(1) NOT NULL,
  parent INT DEFAULT NULL,
  page_id INT DEFAULT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





echo "Database successfully created .... "

echo "Good bye!"

exit
