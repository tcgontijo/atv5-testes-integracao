package com.iftm.client.tests.integration;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;
import com.iftm.client.tests.factory.ClientFactory;

@SpringBootTest
@Transactional
public class ClientServiceIT {
	
	@Autowired
	private ClientService service;
	

	private long existingId;
	private long nonExistingId;
	private long countClientByIncome;
	private long countTotalClients;
	private PageRequest pageRequest;
	
	
	@BeforeEach
	void setUp() throws Exception {
	
		existingId = 1L;
		nonExistingId = 1000L;	
		countClientByIncome = 5L;
		countTotalClients = 12L;
		pageRequest = PageRequest.of(0, 6);
		
	}
	
	/**********************************************
	 ************** Feito em sala *****************
	 **********************************************/
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExistis() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void findByIncomeShouldReturnClientsWhenClientIncomeIsGreaterThanOrEqualsToValue() {
		
		Double income = 4000.0;
		
		Page<ClientDTO> result = service.findByIncome(income, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countClientByIncome, result.getTotalElements());
	}
	
	@Test
	public void findAllShouldReturnAllClients() {
		
		List<ClientDTO> result = service.findAll();
		
		Assertions.assertEquals(countTotalClients, result.size());
	}
	
	
	/*************************************************
	 **************** ATIVIDADE 05 *******************
	 *************************************************/
	
	@Test
	public void deleteShouldDeleteClientWhenIdExist() {
		
		service.delete(existingId);
		
		List<ClientDTO> result = service.findAll();
		
		Assertions.assertEquals(result.size(), countTotalClients-1);
		
	}
	
	@Test
	public void findByIdShouldReturnClientWhenIdExists() {
		
		String expectedName = "Conceição Evaristo";
		String expectedCPF = "10619244881";
		
		ClientDTO clientDTO = service.findById(existingId);
		
		Assertions.assertNotNull(clientDTO);
		Assertions.assertEquals(clientDTO.getName(), expectedName);
		Assertions.assertEquals(clientDTO.getCpf(), expectedCPF);
		
	}
	
	@Test
	public void insertShouldInsertClient() {
		
		ClientDTO clientDTO = ClientFactory.createEmptyClientDTO();
		
		service.insert(clientDTO);
		
		List<ClientDTO> result = service.findAll();
				
		Assertions.assertEquals(result.size(), countTotalClients+1);
		
	}
	
	@Test
	public void updateShouldUpdateClientDataWhenIdExists() {
		
		
		ClientDTO clientDTO = service.findById(existingId);
		
		String updatedName = "Conceição Evaristo da Silva";
		Double updatedIncome = 10000.00;
		Integer updatedChildren = 5;
		
		clientDTO.setName(updatedName);
		clientDTO.setIncome(updatedIncome);
		clientDTO.setChildren(updatedChildren);
		
		ClientDTO clientDTOUpdated = service.update(existingId, clientDTO);
		
		Assertions.assertEquals(clientDTOUpdated.getName(),updatedName);
		Assertions.assertEquals(clientDTOUpdated.getIncome(),updatedIncome);
		Assertions.assertEquals(clientDTOUpdated.getChildren(),updatedChildren);
		
	}
	
}
