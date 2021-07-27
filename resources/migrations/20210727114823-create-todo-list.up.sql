CREATE TABLE IF NOT EXISTS todo_list (
  id            SERIAL                  PRIMARY KEY
, title         VARCHAR(255)  NOT NULL
, description   VARCHAR(2000)     NULL
, is_complete   BOOL          NOT NULL
);
