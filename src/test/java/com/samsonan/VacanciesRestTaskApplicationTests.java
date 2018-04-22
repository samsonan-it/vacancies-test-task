package com.samsonan;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.samsonan.dao.VacancyDao;
import com.samsonan.domain.Vacancy;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class VacanciesRestTaskApplicationTests {

    private MediaType contentType = MediaType.APPLICATION_XML;

    private MockMvc mockMvc;
    
    @Autowired
    private VacancyDao vacancyDao;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
	// test: PUT - new object
    @Test
    public void putNewVacancy() throws Exception {
        Vacancy vacancy = new Vacancy(1L, "new name", null, null, null);
        mockMvc.perform(put("/vacancy/")
                .content(this.toXml(vacancy))
                .contentType(contentType))
                .andExpect(status().isOk());
        
        assertEquals(vacancy, vacancyDao.findById(1L));
    }    

    // test: PUT - existing object
    @Test
    public void putExistingVacancy() throws Exception {

        Vacancy existingVacancy = new Vacancy(2L, "new name", "321", "exp 1", "city 1");
        vacancyDao.save(existingVacancy);
        
        Vacancy newVacancy = new Vacancy(2L, "new name", "123", "exp 2", "city 2");
        
        mockMvc.perform(put("/vacancy/")
                .content(this.toXml(newVacancy))
                .contentType(contentType))
                .andExpect(status().isOk());
        
        assertEquals(newVacancy, vacancyDao.findById(2L));
    }    

    // test: GET by ID - not found
    @Test
    public void getNotFound() throws Exception {
        mockMvc.perform(get("/vacancy/1"))
                .andExpect(status().isNotFound());
    }    
    
    // test: GET by ID - OK
    @Test
    public void getByIdOk() throws Exception {
        Vacancy existingVacancy = new Vacancy(3L, "new name", "321", "exp 1", "city 1");
        vacancyDao.save(existingVacancy);

        String expectedXml = this.toXml(existingVacancy);
        
        mockMvc.perform(get("/vacancy/3"))
                .andExpect(status().isOk())
//                .andExpect(content().contentType(contentType))
                .andExpect(content().xml(expectedXml));
        
    }    
    
    // TODO: other things to test:
    
    // test: PUT - existing object
	// test: PUT - empty ID error
    // test: PUT - empty NAME error
	// test: PUT - negative salary
	// test: PUT - wrong field length
	// test: DELETE - OK
	// test: DELETE - object doesn't exist
	// test: GET all - empty list
	// test: GET all - multiple elements
    // test: POST - not supported
	
    protected String toXml(Object o) throws HttpMessageNotWritableException, IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(o);
    }
    

}
