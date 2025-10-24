package com.udea.dainxor.banco2025;

import com.fasterxml.jackson.databind.JsonNode;
import com.udea.dainxor.banco2025.controller.CustomerController;
import com.udea.dainxor.banco2025.controller.FakerController;
import com.udea.dainxor.banco2025.dto.CustomerDTO;
import com.udea.dainxor.banco2025.dto.DepositDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Banco2025ApplicationTests {

	@Autowired
	FakerController fakerController;
	@Autowired
	CustomerController customerController;

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
	void testCreateCustomer() {
		var testCustomer = new CustomerDTO();
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

	CustomerDTO createCustomer(){
		var testCustomer = new CustomerDTO();
		testCustomer.setFirstName("Test");
		testCustomer.setLastName("User");
		return customerController.createCustomer(testCustomer).getBody();
	}

	@Test
	void testGetAllCustomers(){
		var testCustomers = List.of(createCustomer(), createCustomer());

		List<CustomerDTO> customers = customerController.getAll().getBody();
        assertNotNull(customers);
        assertFalse(customers.isEmpty());
		assertTrue(customers.size() >= testCustomers.size());

		for(var testCustomer : testCustomers){
			assertTrue(customers.stream().anyMatch(c -> c.getId().equals(testCustomer.getId())));
		}
	}

	@Test
	void getByIdNotFound(){
		var response = customerController.getById(9999L);
		assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));
	}

	@Test
	void getById(){
		var testCustomer = createCustomer();
		var response = customerController.getById(testCustomer.getId());
		assertNotNull(response.getBody());
		assertEquals(testCustomer.getId(), response.getBody().getId());
	}

	@Test
	void getByAccountNumber(){
		var testCustomer = createCustomer();
		var response = customerController.getByAccountNumber(testCustomer.getAccountNumber());
		assertNotNull(response.getBody());
		assertEquals(testCustomer.getId(), response.getBody().getId());
	}

	@Test
	void depositMoney(){
		var testCustomer = createCustomer();

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


