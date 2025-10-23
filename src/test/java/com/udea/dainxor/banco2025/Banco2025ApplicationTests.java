package com.udea.dainxor.banco2025;

import com.fasterxml.jackson.databind.JsonNode;
import com.udea.dainxor.banco2025.controller.CustomerController;
import com.udea.dainxor.banco2025.controller.FakerController;
import com.udea.dainxor.banco2025.dto.CustomerDTO;
import com.udea.dainxor.banco2025.dto.DepositDTO;
import com.udea.dainxor.banco2025.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Banco2025ApplicationTests {

	@Autowired
	FakerController fakerController;
	@Autowired
	CustomerController customerController;

	private CustomerDTO testCustomer;
	private TransactionDTO testTransaction;

	@Test
	void health(){
		assertEquals("HEALTH CHECK OK", fakerController.healthCheck());
	}

	@Test
	void version(){
		assertEquals("VERSION IS 1.0.0", fakerController.version());
	}

	// > Faker tests
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
	void testRandomCurrenciesCodeFormat(){
		FakerController controller = new FakerController();
		JsonNode response = controller.getRandomCurrencies();
		for(int i=0; i< response.size(); i++){
			JsonNode currency = response.get(i);
			String code = currency.get("code").asText();
			assertTrue(code.matches("[A-Z]{3}"));
		}
	}

	@Test
	void testRandomNationsPerformance() {
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

	// > End Faker tests
	// > Customer tests
	@Test
	void createCustomer() {
		testCustomer = new CustomerDTO();
		testCustomer.setFirstName("Test");
		testCustomer.setLastName("User");
		testCustomer.setAccountNumber("1234567890");
		testCustomer.setBalance(1000.0);
		testCustomer.setId(123123123L);
		CustomerDTO createdCustomer = customerController.createCustomer(testCustomer).getBody();

		assertNotNull(createdCustomer);
		assertNotNull(createdCustomer.getId());
		assertNotEquals(123123123L, createdCustomer.getId());
		assertNotNull(createdCustomer.getAccountNumber());
		assertNotEquals("1234567890", createdCustomer.getAccountNumber());
		assertEquals("Test", createdCustomer.getFirstName());
		assertEquals("User", createdCustomer.getLastName());
		assertEquals(0.0, createdCustomer.getBalance());
	}

	@Test
	void getAllCustomersNotNull(){
		List<CustomerDTO> customers = customerController.getAll().getBody();
        assertNotNull(customers);
        assertFalse(customers.isEmpty());
	}

	@Test
	void getByIdNotFound(){
		var response = customerController.getById(9999L);
		assertTrue(response.getStatusCode().is4xxClientError());
	}

	@Test
	void getById(){
		var response = customerController.getById(testCustomer.getId());
		assertEquals(testCustomer, response.getBody());
	}

	@Test
	void getByAccountNumber(){
		var response = customerController.getByAccountNumber(testCustomer.getAccountNumber());
		assertEquals(testCustomer, response.getBody());
	}

	@Test
	void depositMoney(){
		Double depositAmount = 500.0;
		var depositDTO = new DepositDTO();
		depositDTO.setId(testCustomer.getId());
		depositDTO.setAmount(depositAmount);

		var response = customerController.depositMoney(depositDTO);
		assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
		CustomerDTO updatedCustomer = response.getBody();
		assertNotNull(updatedCustomer);
		assertEquals(testCustomer.getBalance() + depositAmount, updatedCustomer.getBalance());
		testCustomer = updatedCustomer;
	}

	// > End Customer tests
	// > Transaction tests

	// > End Transaction tests
}


