package com.samsonan.dao;

import java.util.List;

import com.samsonan.domain.Vacancy;

public interface VacancyDao {
 
    List<Vacancy> findAll();
    Vacancy findById(Long vacancyId);
    
    void save(Vacancy vacancy);
    void update(Vacancy vacancy);

    void deleteById(Long vacancyId);
    
}
