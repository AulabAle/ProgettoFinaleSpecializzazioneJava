CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50),
    subtitle VARCHAR(50),
    body TEXT,
    publish_date CHAR(8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    category_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE users_roles (
    user_id BIGINT,
    role_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
