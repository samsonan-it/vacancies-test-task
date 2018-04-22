package com.samsonan.dao.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.samsonan.dao.VacancyDao;
import com.samsonan.domain.Vacancy;

@Repository
public class JdbcVacancyDao implements VacancyDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcNPTemplate;
    
    @Override
    public List<Vacancy> findAll() {
        return jdbcNPTemplate.query("SELECT ID, NAME, SALARY, EXPERIENCE, CITY FROM VACANCIES ORDER BY NAME ASC", 
                (rs, i) -> new Vacancy(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
    }

    @Override
    public Vacancy findById(Long vacancyId) {
        List<Vacancy> vacancies = jdbcNPTemplate.query("SELECT ID, NAME, SALARY, EXPERIENCE, CITY FROM VACANCIES WHERE ID = :id",
                Collections.singletonMap("id", vacancyId), 
                (rs, i) -> new Vacancy(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));

        if (vacancies.isEmpty()) {
            return null; // or Optional?
        }
        
        // there cannot be multiple results since id is PK

        return vacancies.get(0);
    }

    @Override
    public void save(Vacancy vacancy) {
        int updated = jdbcNPTemplate.update("INSERT INTO VACANCIES (ID, NAME, SALARY, EXPERIENCE, CITY) "
                + "VALUES (:id, :name, :salary, :experience, :city)",  
                initParametersMap(vacancy));

        if (updated != 1) {
            throw new IllegalStateException("insert returned " + updated);
        }
        
        System.out.println("after update:" + findById(vacancy.getId()));

    }

    @Override
    public void deleteById(Long vacancyId) {

        jdbcNPTemplate.update("DELETE FROM VACANCIES WHERE ID = :id", Collections.singletonMap("id", vacancyId));
        
    }
    
    private Map<String, Object> initParametersMap(Vacancy vacancy) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", vacancy.getId());
        map.put("name", vacancy.getName());
        map.put("salary", vacancy.getSalary());
        map.put("experience", vacancy.getExperience());
        map.put("city", vacancy.getCity());
        
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void update(Vacancy vacancy) {
        int updated = jdbcNPTemplate.update("UPDATE VACANCIES SET NAME = :name, SALARY = :salary, EXPERIENCE = :experience, CITY = :city "
                + "WHERE ID = :id",
                initParametersMap(vacancy));

        if (updated != 1) {
            throw new IllegalStateException("update returned " + updated);
        }
    }
    
}
