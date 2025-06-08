CREATE TABLE users (
  user_id BIGINT PRIMARY KEY
);

CREATE TABLE livestock_count (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  category VARCHAR(50) NOT NULL,
  male_count INT NOT NULL,
  female_count INT NOT NULL,
  max_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  UNIQUE (user_id, category)
);

CREATE TABLE livestock_event (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  category VARCHAR(50) NOT NULL,
  event_type VARCHAR(50) NOT NULL,
  male_count INT NOT NULL,
  female_count INT NOT NULL,
  event_date DATE NOT NULL,
  sale_price DECIMAL(19,2),
  cost DECIMAL(19,2),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE livestock_expense (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  category VARCHAR(50) NOT NULL,
  expense_category VARCHAR(50) NOT NULL,
  amount DECIMAL(19,2) NOT NULL,
  description VARCHAR(255),
  expense_date DATE NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE livestock_id (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  tag_number VARCHAR(50) NOT NULL,
  category VARCHAR(50) NOT NULL,
  gender VARCHAR(50) NOT NULL,
  status VARCHAR(50) NOT NULL,
  CHECK (status IN ('ALIVE', 'SOLD', 'DECEASED', 'SLAUGHTERED', 'LOST')),
  event_id BIGINT NOT NULL,
  purchase_price DECIMAL(19,2),
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (event_id) REFERENCES livestock_event(id),
  UNIQUE (user_id, tag_number)
);