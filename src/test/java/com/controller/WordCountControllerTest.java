package com.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * End to end test cases
 * @author Echo Wu
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WordCountControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Sanity test for the end point /counter-api/search
     * Expect 
     * @throws Exception
     */
    @Test
    public void testSearchWordCount() throws Exception {
          	
    	String output=mvc.perform(MockMvcRequestBuilders.post("/counter-api/search").header("Authorization", "Basic b3B0dXM6Y2FuZGlkYXRlcw==")
    			.content("{\"searchText\":[\"Duis\", \"Sed\", \"Donec\", \"Augue\", \"Pellentesque\", \"123\"]}")
    			.contentType("application/json").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    
    	assertEquals("{\"counts\":[{\"Duis\":11},{\"Sed\":16},{\"Donec\":8},{\"Augue\":7},{\"Pellentesque\":6},{\"123\":0}]}",output);

    }
    
    /**
     * Sanity test for the end point /counter-api/top/{number}
     * @throws Exception
     */
    @Test
    public void testListTopWordCount() throws Exception {
          	
    	String output=mvc.perform(MockMvcRequestBuilders.get("/counter-api/top/5").header("Authorization", "Basic b3B0dXM6Y2FuZGlkYXRlcw==")
    			.header("Accept", "text/csv"))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    	
    assertEquals("vel|17\n" +
            "eget|17\n" +
            "sed|16\n" +
            "in|15\n" +
            "et|14\n" , output);
    }
}
