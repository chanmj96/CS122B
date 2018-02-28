USE moviedb;
ALTER TABLE movies ADD FULLTEXT INDEX m_entry (title);
ALTER TABLE stars ADD FULLTEXT INDEX s_entry (name);
