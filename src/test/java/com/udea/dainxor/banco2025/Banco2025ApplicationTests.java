package com.udea.dainxor.banco2025;

import com.fasterxml.jackson.databind.JsonNode;
import com.udea.dainxor.banco2025.controller.CustomerController;
import com.udea.dainxor.banco2025.controller.FakerController;
import com.udea.dainxor.banco2025.controller.TransactionController;
import com.udea.dainxor.banco2025.dto.CustomerDTO;
import com.udea.dainxor.banco2025.dto.DepositDTO;
import com.udea.dainxor.banco2025.dto.TransactionDTO;
import com.udea.dainxor.banco2025.dto.TransactionRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Banco2025ApplicationTests {

	@Autowired
	FakerController fakerController;
	@Autowired
	CustomerController customerController;
	@Autowired
	TransactionController transactionController;

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
	}

	// > End Customer tests
	// > Transaction tests
	@Test
	void testCreateTransaction(){
		var c1 = createCustomer();
		var c2 = createCustomer();

		customerController.depositMoney(new DepositDTO(c1.getId(), 1000.0));

		var testTransaction = new TransactionRequestDTO();
		testTransaction.setSenderAccountNumber(c1.getAccountNumber());
		testTransaction.setReceiverAccountNumber(c2.getAccountNumber());
		testTransaction.setAmount(250.0);

		var timestampBefore = ChronoLocalDateTime.from(java.time.LocalDateTime.now());
		var response = transactionController.create(testTransaction);
		var timestampAfter = ChronoLocalDateTime.from(java.time.LocalDateTime.now());
		// assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());

		if (response.getStatusCode().isError()) {
			fail(response.getBody() instanceof String msg ? msg : "Transaction creation failed with unknown error");
		}
		else if (response.getBody() instanceof TransactionDTO newTransaction) {
			assertNotNull(newTransaction);
			assertNotNull(newTransaction.getId());
			assertEquals(c1.getAccountNumber(), newTransaction.getSenderAccountNumber());
			assertEquals(c2.getAccountNumber(), newTransaction.getReceiverAccountNumber());
			assertEquals(250.0, newTransaction.getAmount());
			assertTrue(newTransaction.getTimestamp().isAfter(timestampBefore));
			assertTrue(newTransaction.getTimestamp().isBefore(timestampAfter));
		}


	}

	TransactionDTO createTransactionFor(CustomerDTO sender, CustomerDTO receiver, double amount){
		if (sender.getBalance() < amount)
		customerController.depositMoney(new DepositDTO(sender.getId(), amount + 100.0));

		var testTransaction = new TransactionRequestDTO();
		testTransaction.setSenderAccountNumber(sender.getAccountNumber());
		testTransaction.setReceiverAccountNumber(receiver.getAccountNumber());
		testTransaction.setAmount(amount);

		var response = transactionController.create(testTransaction).getBody();
		return (TransactionDTO) response;
	}
	TransactionDTO createTransaction(){
		var c1 = createCustomer();
		var c2 = createCustomer();

		return createTransactionFor(c1, c2, 150);
	}

	@Test
	void testGetTransactionByID(){
		var testTransaction = createTransaction();

		var response = transactionController.getTransactionsByID(testTransaction.getId());
		assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
		var fetchedTransaction = response.getBody();
		assertNotNull(fetchedTransaction);
		assertEquals(testTransaction.getId(), fetchedTransaction.getId());
		assertEquals(testTransaction.getSenderAccountNumber(), fetchedTransaction.getSenderAccountNumber());
		assertEquals(testTransaction.getReceiverAccountNumber(), fetchedTransaction.getReceiverAccountNumber());
		assertEquals(testTransaction.getAmount(), fetchedTransaction.getAmount());
		assertEquals(testTransaction.getTimestamp(), fetchedTransaction.getTimestamp());
	}

	@Test
	void testGetTransactionsBySenderAccountNumber(){
		var senderCustomer = createCustomer();
		var testTransactions = List.of(
				createTransactionFor(senderCustomer, createCustomer(), 200.0),
				createTransactionFor(senderCustomer, createCustomer(), 300.0),
				createTransactionFor(senderCustomer, createCustomer(), 1500.0)
		);

		var response = transactionController.getTransactionsByAccountNumber(senderCustomer.getAccountNumber());
		assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

		var fetchedTransactions = response.getBody();
		assertNotNull(fetchedTransactions);
        assertEquals(fetchedTransactions.size(), testTransactions.size());
		for(var testTransaction : testTransactions){
			assertTrue(fetchedTransactions.stream().anyMatch(t -> t.getId().equals(testTransaction.getId())));
		}
	}

	@Test
	void testGetTransactionsByReceiverAccountNumber(){
		var receiverCustomer = createCustomer();
		var testTransactions = List.of(
				createTransactionFor(createCustomer(), receiverCustomer, 500.0),
				createTransactionFor(createCustomer(), receiverCustomer, 100.0),
				createTransactionFor(createCustomer(), receiverCustomer, 750.0)
		);

		var response = transactionController.getTransactionsByAccountNumber(receiverCustomer.getAccountNumber());
		assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

		var fetchedTransactions = response.getBody();
		assertNotNull(fetchedTransactions);
		assertEquals(fetchedTransactions.size(), testTransactions.size());
		for(var testTransaction : testTransactions){
			assertTrue(fetchedTransactions.stream().anyMatch(t -> t.getId().equals(testTransaction.getId())));
		}
	}

	// > End Transaction tests
}


