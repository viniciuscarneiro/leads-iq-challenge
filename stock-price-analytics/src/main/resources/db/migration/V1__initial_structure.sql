CREATE TABLE company (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         symbol VARCHAR(10) UNIQUE NOT NULL,
                         name VARCHAR(255),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_price (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            company_id INT NOT NULL,
                            date DATE NOT NULL,
                            open DECIMAL(10, 2),
                            close DECIMAL(10, 2),
                            high DECIMAL(10, 2),
                            low DECIMAL(10, 2),
                            volume BIGINT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (company_id) REFERENCES company(id)
);
