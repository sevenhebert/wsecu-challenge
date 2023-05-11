SET TIME ZONE 'UTC';

CREATE TABLE vend.product
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(255)  NOT NULL,
    price    DECIMAL(5, 2) NOT NULL,
    quantity INTEGER       NOT NULL,
    created  TIMESTAMP     NOT NULL DEFAULT now(),
    updated  TIMESTAMP     NOT NULL DEFAULT now(),
    deleted  BOOLEAN       NOT NULL DEFAULT FALSE
);

CREATE TABLE vend.transaction
(
    id          SERIAL PRIMARY KEY,
    amount_paid DECIMAL(5, 2) NOT NULL,
    created     TIMESTAMP     NOT NULL DEFAULT now(),
    updated     TIMESTAMP     NOT NULL DEFAULT now(),
    deleted     BOOLEAN       NOT NULL DEFAULT FALSE
);

CREATE TABLE vend.ledger
(
    transaction_id INTEGER   NOT NULL REFERENCES vend.transaction (id),
    product_id     INTEGER   NOT NULL REFERENCES vend.product (id),
    quantity       INTEGER   NOT NULL,
    created        TIMESTAMP NOT NULL DEFAULT now(),
    updated        TIMESTAMP NOT NULL DEFAULT now(),
    deleted        BOOLEAN   NOT NULL DEFAULT FALSE,
    PRIMARY KEY (transaction_id, product_id)
);

CREATE TABLE vend.payment
(
    id             SERIAL PRIMARY KEY,
    transaction_id INTEGER       NOT NULL REFERENCES vend.transaction (id),
    amount         DECIMAL(5, 2) NOT NULL,
    created        TIMESTAMP     NOT NULL DEFAULT now(),
    updated        TIMESTAMP     NOT NULL DEFAULT now(),
    deleted        BOOLEAN       NOT NULL DEFAULT FALSE
);

INSERT INTO vend.product (name, price, quantity) VALUES ('Coke', 0.95, 10);
INSERT INTO vend.product (name, price, quantity) VALUES ('Skittles', 0.60, 10);
INSERT INTO vend.product (name, price, quantity) VALUES ('Doritos', 0.99, 10);

