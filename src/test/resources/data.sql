-- Sample data for integration tests

-- Users
INSERT INTO librarymanagement.user (id, name, username, email, password, role, user_status, created_date, modified_date) VALUES
  (1, 'alice' , 'alice', 'alice@example.com', 'password123', 'PATRON', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'bob' , 'bob',   'bob@example.com',   'securePass',  'LIBRARIAN', 'ACTIVE',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Books
INSERT INTO librarymanagement.book (id, title, author, isbn, publication_date, genre, book_status, created_date, modified_date) VALUES
  (1, 'The Hobbit', 'J.R.R. Tolkien', '9780547928227', '1937-09-21', 'FANTASY', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, '1984', 'George Orwell',  '9780451524935', '1949-06-08', 'DYSTOPIA', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'Clean Code', 'Robert C. Martin','9780132350884','2008-08-11','TECHNICAL', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Borrow history
INSERT INTO librarymanagement.borrow_history (id, user_id, book_id, borrow_date, due_date, return_date, is_returned, created_date, modified_date) VALUES
  -- Active borrow: not yet returned
  (100, 1, 1, '2025-04-20', '2025-05-04', NULL, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  -- Returned on time
  (101, 1, 2, '2025-04-01', '2025-04-15', '2025-04-10', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  -- Overdue (due date passed without return)
  (102, 2, 3, '2025-03-01', '2025-03-15', NULL, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
