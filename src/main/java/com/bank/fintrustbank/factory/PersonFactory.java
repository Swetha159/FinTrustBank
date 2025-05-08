package com.bank.fintrustbank.factory;

import java.io.IOException;

import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.util.IdCreation;
import com.bank.fintrustbank.util.Password;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class PersonFactory {

	private static final ObjectMapper mapper = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();

	public static Person createPerson(String modifiedBy, String jsonBody, String status  ,String role)
			throws TaskException, IOException {

		Person person = mapper.readValue(jsonBody, Person.class);
		String personId = IdCreation.createPersonId();
		person.setPersonId(personId);

		String password = person.getPassword();
		password = password != null ? password : person.getDob().toString() + person.getAadhar().toString();
		person.setPassword(Password.createHash(password));

		person.setModifiedBy(modifiedBy!=null?modifiedBy :personId);
		person.setRole(role);             
		person.setCreatedAt(System.currentTimeMillis());
		person.setModifiedAt((Long)null);
		person.setStatus(status);

		return person;
	}

}
