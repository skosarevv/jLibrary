package dev.skosarev.librarysystem.dao;

import dev.skosarev.librarysystem.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


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

//        FIXME почему-то не работает
//        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(title, author, year) VALUES(?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getYear());
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id = ?", new BeanPropertyRowMapper<>(Book.class), id)
                .stream().findAny().orElse(null);
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE book SET person_id = ?, title = ?, author = ?, year = ? WHERE id = ?",
                updatedBook.getPersonId(), updatedBook.getTitle(), updatedBook.getAuthor(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    public List<Book> getByPerson(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE person_id = ?", new BeanPropertyRowMapper<>(Book.class), id);
    }

    public void setFree(int id) {
        jdbcTemplate.update("UPDATE book SET person_id = null WHERE id = ?", id);
    }
}
