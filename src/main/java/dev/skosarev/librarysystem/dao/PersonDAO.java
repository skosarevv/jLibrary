package dev.skosarev.librarysystem.dao;

import dev.skosarev.librarysystem.model.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
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

    public List<Person> index() {
        List<Person> people = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM person;");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setFullName(resultSet.getString("full_name"));
                person.setBirthYear(resultSet.getInt("birth_year"));

                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return people;
    }

    public void save(Person person) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO person(full_name, birth_year) VALUES(?, ?)");

            preparedStatement.setString(1, person.getFullName());
            preparedStatement.setInt(2, person.getBirthYear());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person show(int id) {
        Person person = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM person WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            person = new Person();

            person.setId(resultSet.getInt("id"));
            person.setFullName(resultSet.getString("full_name"));
            person.setBirthYear(resultSet.getInt("birth_year"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }

    public Person show(String fullName) {
        Person person = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM person WHERE full_name = ?");
            preparedStatement.setString(1, fullName);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setFullName(resultSet.getString("full_name"));
                person.setBirthYear(resultSet.getInt("birth_year"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }

    public void update(int id, Person updatedPerson) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE person SET full_name = ?, birth_year = ? WHERE id = ?");

            preparedStatement.setString(1, updatedPerson.getFullName());
            preparedStatement.setInt(2, updatedPerson.getBirthYear());
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM person WHERE id = ?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person getByBook(int id) {
        Person person = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM person WHERE id = (SELECT person_id FROM book WHERE id = ?)");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setFullName(resultSet.getString("full_name"));
                person.setBirthYear(resultSet.getInt("birth_year"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }
}
