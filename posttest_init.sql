DROP TABLE IF EXISTS tickets CASCADE;

CREATE TABLE lotteries(
    id SERIAL PRIMARY KEY,
    ticket VARCHAR(6) UNIQUE NOT NULL,
    price INTEGER NOT NULL DEFAULT 0,
    amount INTEGER NOT NULL DEFAULT 0
);

