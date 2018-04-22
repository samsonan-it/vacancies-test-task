package com.samsonan.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.samsonan.domain.Vacancy;

@XmlRootElement (name = "vacancies")
@XmlAccessorType (XmlAccessType.FIELD)
public class VacancyContainer {

    private List<Vacancy> vacancies;

    public VacancyContainer() {
    }

    public VacancyContainer(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }
    
    
    
}
