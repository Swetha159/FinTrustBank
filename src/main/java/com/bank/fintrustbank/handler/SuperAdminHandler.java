package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.fintrustbank.factory.PersonFactory;
import com.bank.fintrustbank.factory.PrivilegedUserFactory;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.model.PrivilegedUser;
import com.bank.fintrustbank.service.AdminService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class SuperAdminHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		String path = request.getPathInfo();
		try {
			if (path.equals("/superadmin")) {
				handleAddSuperAdmin(request, response);
			}
		} catch (IOException | SQLException | ServletException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

	public void handleAddSuperAdmin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, TaskException, SQLException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

		}
		
		String sessionPersonId = (String) session.getAttribute("personId");
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);

		String branchId = rootNode.path("branch_id").asText();
		Person person = PersonFactory.createPerson(sessionPersonId, jsonBody, "ACTIVE", "SUPERADMIN");
		PrivilegedUser privilegedUser = PrivilegedUserFactory.createPrivilegedUser(person.getPersonId(), branchId, sessionPersonId);

		AdminService service = new AdminService();

		if (!service.addNewAdmin(person, privilegedUser)) {
			throw new TaskException("Super Admin not created");
		}

	}




	public void handleEditAdmin(HttpServletRequest request, HttpServletResponse response) {

	}

}
