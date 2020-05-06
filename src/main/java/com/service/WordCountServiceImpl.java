package com.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.bean.SearchInput;
import com.bean.SearchResult;

/**
 * Word count service implementation
 * 
 * @author Echo Wu
 *
 */
@Service
public class WordCountServiceImpl implements WordCountService {

    /**Cache container**/ 
    private ConcurrentHashMap<String, Map<String, Integer>> resultCache = new ConcurrentHashMap<String, Map<String, Integer>>();
    
    /**Utility for loading the data file**/
	@Autowired
	ResourceLoader loader;
	
    
	@Override
	public String topCounts(String resourceName,Integer top) {
		
		StringBuilder result = new StringBuilder(); 
		generateStatisticData(resourceName).entrySet().stream()
		.sorted((a,b)->b.getValue().compareTo(a.getValue()))
		.limit(top)
		.forEach(
			a->{
			//construct the 
			result.append(a.getKey());
			result.append("|");
			result.append(a.getValue());
			result.append("\n");			
		});
		return result.toString();
		
	}


	@Override
	public SearchResult search(String resourceName, SearchInput input) {
		Map<String, Integer> result=generateStatisticData(resourceName);
		//Use linked hashMap the preserver the key order
		Map<String, Integer> resultMap=new LinkedHashMap<>();
		//search each input word and the occurrences 
		input.getSearchText().stream().forEach(
				x->{
					Integer count=result.get(x.toLowerCase());
					//if word not found set the count to 0
					if(count==null) count=0;
					resultMap.put(x, count);
				}

		);
		SearchResult searchResult=new SearchResult();
		//convert the the <key,value> array according to the output format
		searchResult.setCounts(resultMap.entrySet().toArray());
		return searchResult;
	}

	/**
	 * Perform the word count statistic and cache the result
	 * @param resourceName
	 * @return
	 */
	private Map<String, Integer> generateStatisticData(String resourceName){
		//return the stored result if find in cache
		if(resultCache.containsKey(resourceName)) {
			System.out.println("Cached result for " + resourceName + " found");
			return resultCache.get(resourceName);
		}
		//read from resource file
		ConcurrentMap<String, Integer> statResult=null;
		try(BufferedReader br = new BufferedReader(
				new  InputStreamReader(
				loader.getResource("classpath:"+resourceName).getInputStream()))){
			
			//perform word count statistic	
			statResult = br.lines()
					.flatMap(line -> Stream.of(line.toLowerCase().split("[\\W]+")))
					.collect(Collectors.toConcurrentMap(x -> x, x -> 1, Integer::sum));
			//save in cache
			resultCache.put(resourceName, statResult);			
			
		} catch(FileNotFoundException e){
			System.out.println("File not found exception occured!");
		} catch (IOException e) {
			System.out.println("IO Exception occured");
		}
		return statResult;		
	}
	
}
