package com.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bean.SearchInput;
import lombok.extern.slf4j.Slf4j;
import com.service.WordCountService;

/**
 * Controller for all the restful endpoints
 * @author Echo Wu
 *
 */
@RestController
@Slf4j
@RequestMapping("/counter-api")
public class WordCountController {
	
	@Autowired
	WordCountService wordCountService;
	
	private static final String RESOURCE_NAME="paragraph.txt";
    
    /**
     *  Search the input texts and will return the counts respectively <BR>
     *  Result will be in JSON format  <BR>
     *  
     *  Example input, run the below curl command 
     *   <pre>
		 * 		curl http://localhost:8080/counter-api/search -H "Authorization: Basic b3B0dXM6Y2FuZGlkYXRlcw=="
		 * 		-H "Content-Type: application/json" -d'{"searchText":["Duis", "Sed", "Donec", "Augue", "Pellentesque", "123"]}' -H"Content- Type: application/json" -X POST
		 *
		 *
     *  sample result:
     *  		{"counts": [{"Duis": 11}, {"Sed": 16}, {"Donec": 8}, {"Augue": 7}, {"Pellentesque": 6},
	 *		{"123": 0}]}
     * </pre>
     * 
     * @param wordList  String list
     * @param authToken Authorization token
     * @return JSON format string
     * @throws IOException
     */
    @PostMapping("/search")
    @ResponseBody
    public  Object searchWordCount(
	    		@RequestBody SearchInput wordList,
	    		@RequestHeader("Authorization") String authToken
    		) {

			log.debug("authToken:"+authToken);
    		
    		return wordCountService.search(RESOURCE_NAME, wordList);
    		
    }
    
    
    /**
     * Return the top n word count which has the highest counts in the resource file <br>
     * 
     * Sample request:
     * <pre>
     * curl http://localhost:8080/counter-api/top/5 -H "Authorization: Basic b3B0dXM6Y2FuZGlkYXRlcw==" -H "Accept: text/csv"
     * 
     * Sample result:
     *  text1|100
	 *  text2|91 
	 *  text3|80 
	 *  text4|70 
	 *  text5|60
	 *  </pre>
     * 
     * @param number Path variable to indicate the top n word
     * @param authToken Authorization token
     * @param response JSON format with text/csv as header
     * @throws IOException
     */
    @RequestMapping("/top/{number}")
    public void listTopWordCount(
	    		@PathVariable int number,
	    		@RequestHeader("Authorization") String authToken,
	    		HttpServletResponse response
	    		) throws IOException {
    	
    		log.debug("authToken:"+authToken);
    		
    		response.setContentType("text/csv");
    		response.getWriter().write(wordCountService.topCounts(RESOURCE_NAME, number));
    }

    
}
