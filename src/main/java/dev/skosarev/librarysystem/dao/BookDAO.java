package dev.skosarev.librarysystem.dao;

import dev.skosarev.librarysystem.model.Book;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/library";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "pass";

    private final static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> index() {
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM book");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();

                book.setId(resultSet.getInt("id"));
                book.setPersonId(resultSet.getInt("person_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setYear(resultSet.getInt("year"));

                books.add(book);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;
    }

    public void save(Book book) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO book(title, author, year) VALUES(?, ?, ?)");

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getYear());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book show(int id) {
        Book book = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM book WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            book = new Book();

            book.setId(resultSet.getInt("id"));
            book.setPersonId(resultSet.getInt("person_id"));
            book.setTitle(resultSet.getString("title"));
            book.setAuthor(resultSet.getString("author"));
            book.setYear(resultSet.getInt("year"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return book;
    }

    public void update(int id, Book updatedBook) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE book SET person_id = ?, title = ?, author = ?, year = ? WHERE id = ?");
            if (updatedBook.getPersonId() == 0) {
                preparedStatement.setNull(1, Types.NULL);
            } else {
                preparedStatement.setInt(1, updatedBook.getPersonId());
            }
            preparedStatement.setString(2, updatedBook.getTitle());
            preparedStatement.setString(3, updatedBook.getAuthor());
            preparedStatement.setInt(4, updatedBook.getYear());
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM book WHERE id = ?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> getByPerson(int id) {
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM book WHERE person_id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();

                book.setId(resultSet.getInt("id"));
                book.setPersonId(resultSet.getInt("person_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setYear(resultSet.getInt("year"));

                books.add(book);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;
    }

    public void setFree(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE book SET person_id = null WHERE id = ?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
