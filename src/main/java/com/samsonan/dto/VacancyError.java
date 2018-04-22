package com.samsonan.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "vacancy_errors")
@XmlAccessorType (XmlAccessType.FIELD)
public class VacancyError {

    private List<String> errors;

    public VacancyError() {
    }

    public VacancyError(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    
    
}
