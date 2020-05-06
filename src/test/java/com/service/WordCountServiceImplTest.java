package com.service;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.bean.SearchInput;
import com.bean.SearchResult;

@WebAppConfiguration
public class WordCountServiceImplTest {
	
	@Mock
	ResourceLoader loader;
	
	@Mock
	Resource resource;
	
	@InjectMocks
	@Spy
	WordCountServiceImpl wordCountService;
	

	@Before
	public void setUp() throws IOException {
		
		MockitoAnnotations.initMocks(this);
		when(loader.getResource(anyString())).thenReturn(resource);
		
		String data="abc efg abc 12 abc efg";
		InputStream is=new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
		when(resource.getInputStream()).thenReturn(is);
		
		
	}
	
	/**
	 * Test search count's correctiveness according the input
	 * @throws IOException
	 */
	@Test
	@SuppressWarnings("all")
	public void testSearchCount(){
		SearchInput input=new SearchInput();
		input.setSearchText(Arrays.asList("abc","efg","123"));
		SearchResult result=wordCountService.search("", input);
		
		
		assertEquals("abc",((Map.Entry)result.getCounts()[0]).getKey());
		assertEquals(3, ((Map.Entry)result.getCounts()[0]).getValue());
		
		assertEquals("efg",((Map.Entry)result.getCounts()[1]).getKey());
		assertEquals(2, ((Map.Entry)result.getCounts()[1]).getValue());
		
		assertEquals("123",((Map.Entry)result.getCounts()[2]).getKey());
		assertEquals(0, ((Map.Entry)result.getCounts()[2]).getValue());
		
	}
	
	/**
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testCachedMachanism() throws IOException{
		SearchInput input=new SearchInput();
		input.setSearchText(Arrays.asList("abc","efg","123"));
		
		//perform search three times
		wordCountService.search("", input);
		wordCountService.search("", input);
		wordCountService.search("", input);
		
		//expect load resource only call once as cached
		verify(wordCountService,times(3)).search(anyString(), any(SearchInput.class));
		verify(resource,times(1)).getInputStream();

		
	}
	
	@Test
	public void testListTopCount() throws IOException {

		assertEquals("abc|3\nefg|2\n",wordCountService.topCounts("", 2));		
		
	}
	

}
