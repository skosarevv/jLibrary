package dev.skosarev.librarysystem.dao;

import dev.skosarev.librarysystem.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(full_name, birth_year) VALUES(?, ?)", person.getFullName(), person.getBirthYear());
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new BeanPropertyRowMapper<>(Person.class), id)
                .stream().findAny().orElse(null);
    }

    public Person show(String fullName) {
        return jdbcTemplate.query("SELECT * FROM person WHERE full_name = ?", new BeanPropertyRowMapper<>(Person.class), fullName)
                .stream().findAny().orElse(null);
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET full_name = ?, birth_year = ? WHERE id = ?",
                updatedPerson.getFullName(),updatedPerson.getBirthYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id = ?", id);
    }

    public Person getPersonByBook(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = (SELECT person_id FROM book WHERE id = ?)",
                new BeanPropertyRowMapper<>(Person.class), id).stream().findAny().orElse(null);
    }
}
