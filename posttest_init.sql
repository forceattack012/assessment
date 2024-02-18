DROP TABLE IF EXISTS tickets CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_ticket CASCADE;

CREATE TABLE lotteries(
    id SERIAL PRIMARY KEY,
    ticket VARCHAR(6) UNIQUE NOT NULL,
    price INTEGER NOT NULL DEFAULT 0,
    amount INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE users(
    user_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE user_ticket(
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(10) NOT NULL,
    ticket VARCHAR(6) NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY(ticket) REFERENCES lotteries(ticket) ON DELETE CASCADE
);

CREATE INDEX index_lotteries_ticket ON lotteries (ticket);
CREATE INDEX index_users_id ON users (user_id);
CREATE INDEX index_user_ticket ON user_ticket (user_id, ticket);

INSERT INTO lotteries(ticket, price, amount) VALUES('000001', 80, 1);
INSERT INTO lotteries(ticket, price, amount) VALUES('000002', 80, 1);

INSERT INTO users(user_id, name) VALUES('0123456789', 'Jim');
INSERT INTO users(user_id, name) VALUES('1123456789', 'B');

--INSERT INTO user_ticket(user_id, ticket) VALUES('0123456789', '000001');
--INSERT INTO user_ticket(user_id, ticket) VALUES('0123456789', '000002');
--INSERT INTO user_ticket(user_id, ticket) VALUES('1123456789', '000001');
--INSERT INTO user_ticket(user_id, ticket) VALUES('1123456789', '000001');