package com.samsonan.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.samsonan.dao.VacancyDao;
import com.samsonan.domain.Vacancy;
import com.samsonan.dto.VacancyContainer;
import com.samsonan.dto.VacancyError;

@RestController
public class VacancyRestController {

    private static Logger log = LoggerFactory.getLogger(VacancyRestController.class);
    
    private final VacancyDao vacancyDao;

    @Autowired
    public VacancyRestController(VacancyDao vacancyDao) {
        this.vacancyDao = vacancyDao;
    }


    /**
     * PUT /vacancy    
     * Создание новой вакансии
     */
    @PutMapping(value="/vacancy",
            consumes = "application/xml",
            produces = "application/xml")
    @ResponseBody
    public ResponseEntity<?> putVacancy(@Valid @RequestBody Vacancy vacancy, BindingResult bindingResult) {
        
        log.debug("PUT vacancy: {} errors: {}", vacancy, bindingResult.getErrorCount());

        if (bindingResult.hasErrors()) {
            log.debug("PUT errors: {}", bindingResult.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new VacancyError(bindingResult.getAllErrors()
                            .stream()
                            .map(e -> e.toString())
                            .collect(Collectors.toList())));
        }
        
        Vacancy existingVacancy = vacancyDao.findById(vacancy.getId());
        
        log.debug("existingVacancy: {}", existingVacancy);
        
        if (existingVacancy != null) {
            vacancyDao.update(vacancy);
        } else {        
            vacancyDao.save(vacancy);
        }
        
        return ResponseEntity.ok().build();
    }    
        
    /**
     * GET /vacancy    
     * Список вакансий отсортированных по наименованию
     */
    @GetMapping(value="/vacancy", 
            produces = "application/xml")
    @ResponseBody
    public ResponseEntity<VacancyContainer> getVacancies() {

        List<Vacancy> vacancies = vacancyDao.findAll();
       
        return ResponseEntity.ok().body(new VacancyContainer(vacancies));
    }
    
    /**
     * GET /vacancy/{id}   
     * Вакансия с ID {id}
     */
    @GetMapping(value = "/vacancy/{id}", 
            produces = "application/xml")
    @ResponseBody
    public ResponseEntity<Vacancy> getVacancy(@PathVariable("id") Long vacancyId) {

        Vacancy vacancy = vacancyDao.findById(vacancyId);
        
        if (vacancy == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        return ResponseEntity.ok().body(vacancy);
    }
    
    /**
     * DELETE /vacancy/{id}    
     * удаление вакансии с ID {id}
     */
    @DeleteMapping({"/vacancy/{id}"})
    @ResponseBody
    public ResponseEntity<?> deleteVacancy(@PathVariable("id") Long vacancyId) {
        try {
            Vacancy vacancy = vacancyDao.findById(vacancyId);
            
            if (vacancy == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            vacancyDao.deleteById(vacancyId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
}
