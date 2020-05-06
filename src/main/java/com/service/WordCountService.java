package com.service;

import com.bean.SearchInput;
import com.bean.SearchResult;

/**
 * Word count service interface
 * @author Echo Wu
 *
 */
public interface WordCountService {
	
	/**
	 * Return the top count as CSV format string
	 * @param resourceName resource file contains word data
	 * @param top numbers of top words
	 * @return CSV format string
	 */
	public String topCounts(String resourceName, Integer top);
	
	

	/**
	 * retrieves the occurrences of the requested words
	 * 
	 * @param resourceName  Resource file contains word data
	 * @param input search input from the request
	 * @return search result as JSON data mapper
	 */
	public  SearchResult search(String resourceName , SearchInput input);
	
}
