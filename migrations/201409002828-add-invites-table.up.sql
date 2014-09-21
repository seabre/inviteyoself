CREATE TABLE invites (
 id serial PRIMARY KEY,
 email TEXT NOT NULL DEFAULT '',
 invited BOOLEAN NOT NULL DEFAULT 'f');
