CREATE SEQUENCE IF NOT EXISTS order_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE "order"
(
    id                  BIGINT      NOT NULL,
    sell_product_id     BIGINT      NOT NULL,
    sell_product_amount INT         NOT NULL,
    buy_product_id      BIGINT      NOT NULL,
    buy_product_amount  INT         NOT NULL,
    user_id             BIGINT      NOT NULL,
    status              VARCHAR(32) NOT NULL,
    created_at          TIMESTAMP   NOT NULL,
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE product
(
    id   BIGINT  NOT NULL,
    name VARCHAR NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT       NOT NULL,
    email    VARCHAR      NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE product
    ADD CONSTRAINT uc_product_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_SELL_PRODUCT FOREIGN KEY (sell_product_id) REFERENCES product (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_BUY_PRODUCT FOREIGN KEY (buy_product_id) REFERENCES product (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);