package com.udea.dainxor.banco2025;

import com.fasterxml.jackson.databind.JsonNode;
import com.udea.dainxor.banco2025.controller.FakerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class Banco2025ApplicationTests {

	@Autowired
	FakerController fakerController;

	@Test
	void health(){
		assertEquals("HEALTH CHECK OK", fakerController.healthCheck());
	}

	@Test
	void version(){
		assertEquals("VERSION IS 1.0.0", fakerController.version());
	}

	@Test
	void nationLength(){
		Integer nationsLength = fakerController.getRandomNations().size();
		assertEquals(10, nationsLength);
	}

	@Test
	void currenciesLength(){
		Integer currenciesLength = fakerController.getRandomCurrencies().size();
		assertEquals(20, currenciesLength);
	}

	@Test
	public void testRandomCurrenciesCodeFormat(){
		FakerController controller = new FakerController();
		JsonNode response = controller.getRandomCurrencies();
		for(int i=0; i< response.size(); i++){
			JsonNode currency = response.get(i);
			String code = currency.get("code").asText();
			assertTrue(code.matches("[A-Z]{3}"));
		}
	}

	@Test
	public void testRandomNationsPerformance() {
		FakerController controller = new FakerController();
		long startTime = System.currentTimeMillis();

		controller.getRandomNations();
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		System.out.println(executionTime);
		assertTrue(executionTime < 2000);
	}

	@Test
	void aviationsLength(){
		Integer aviationsLength = fakerController.getRandomAviation().size();
		assertEquals(20, aviationsLength);
	}
}


