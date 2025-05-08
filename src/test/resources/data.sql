-- Sample data for integration tests

-- Users
INSERT INTO librarymanagement.user (name, username, email, password, role, user_status, created_date, modified_date) VALUES
  ('alice' , 'alice', 'alice@example.com', '$2b$12$GsEm2WzCoON5TMvIzBzp4OWcJYyA9sXEkIbNzBCV4zTNp3Z1ttIHG', 'PATRON', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('bob' , 'bob',   'bob@example.com',   '$2b$12$BgZdvp5ttanwZ7rXEblfcuiL.aJAJvz7qJF2nM/UvmkOZM3JevCve',  'LIBRARIAN', 'ACTIVE',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('mark' , 'mark',   'mark@example.com',   '$2b$12$BgZdvp5ttanwZ7rXEblfcuiL.aJAJvz7qJF2nM/UvmkOZM3JevCve',  'LIBRARIAN', 'ACTIVE',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Books
INSERT INTO librarymanagement.book (title, author, isbn, publication_date, genre, book_status, created_date, modified_date) VALUES
  ('The Hobbit', 'J.R.R. Tolkien', '9780547928227', '1937-09-21', 'FANTASY', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('1984', 'George Orwell',  '9780451524935', '1949-06-08', 'DYSTOPIA', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Clean Code', 'Robert C. Martin','9780132350884','2008-08-11','TECHNICAL', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Borrow history
INSERT INTO librarymanagement.borrow_history (user_id, book_id, borrow_date, due_date, return_date, is_returned, created_date, modified_date) VALUES
  -- Active borrow: not yet returned
  (1, 1, '2025-04-20', '2025-05-04', NULL, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  -- Returned on time
  (1, 2, '2025-04-01', '2025-04-15', '2025-04-10', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  -- Overdue (due date passed without return)
  (2, 3, '2025-03-01', '2025-03-15', NULL, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
