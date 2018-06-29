package com.kawnnor.oreo.api;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest
public class ApiApplicationTests {

	private MockMvc mvc;
	@Test
	public void contextLoads() {
		RequestBuilder request = null;
		//request = post("/project").param("name","123").param("instructions","这是一个藐视");
	//	mvc.perform(request).andExpect(content().string(equalTo("success")));
	}

}
