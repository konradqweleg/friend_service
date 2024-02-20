DROP SCHEMA IF EXISTS friends_schema CASCADE;
CREATE SCHEMA IF NOT EXISTS friends_schema;

CREATE TABLE IF NOT EXISTS friends_schema.friends (   id_friends SERIAL PRIMARY KEY,
                                                      id_first_friend SERIAL NOT NULL,
                                                      id_second_friend SERIAL NOT NULL

);