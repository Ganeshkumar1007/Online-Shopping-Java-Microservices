package com.ganesh.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganesh.productservice.dto.request.ProductRequest;
import com.ganesh.productservice.dto.response.ProductResponse;
import com.ganesh.productservice.model.Product;
import com.ganesh.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc

class ProductServiceApplicationTests {

//	@Container
//	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongodb:4.4.2");
@Container
	 static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("mongodb://localhost:27017/product-service", mongoDBContainer::getReplicaSetUrl);
	}
	@Test
	void testToCreateProduct() throws Exception, JsonProcessingException {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product").contentType(MediaType.APPLICATION_JSON).content(productRequestString))
				.andExpect(status().isCreated());
//		Assertions.assertEquals(1,productRepository.findAll().size());

		// Mvc is used to do the endpoints request.
		//Mvc is used to send the post request with endpoint ("/api/product") with contentType JSON format and
		// content as ProductRequest and Expect the return status as CREATED
	}

	@Test
	void testToGetAllProducts(){
		List<Product> productResponseList = productRepository.findAll();
		List<ProductResponse> productResponses = new ArrayList<>();
//		for()
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder().name("Ipad Pro").description("Ipad Pro").price(BigDecimal.valueOf(70000)).build();
	}

}
