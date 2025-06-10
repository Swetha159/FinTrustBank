package com.bank.fintrustbank.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zoho.training.exceptions.TaskException;

public class DashboardHandler implements HttpRequestHandler {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		String path = request.getPathInfo();
		try {
			if (path.equals("/customer/dashboard")) {

				request.getRequestDispatcher("/WEB-INF/dashboard/customerdashboard.jsp").forward(request, response);

			} else if (path.equals("/admin/dashboard")) {
				request.getRequestDispatcher("/WEB-INF/admindashboard/admindashboard.jsp").forward(request, response);

			} else if (path.equals("/superadmin/dashboard")) {

				request.getRequestDispatcher("/WEB-INF/admindashboard/superadmindashboard.jsp").forward(request, response);
			}
		} catch (ServletException | IOException e) {

			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		String path = request.getPathInfo();
		try {
			if (path.equals("/customer/dashboard")) {

				request.getRequestDispatcher("/WEB-INF/dashboard/customerdashboard.jsp").forward(request, response);

			} else if (path.equals("/admin/dashboard")) {
				request.getRequestDispatcher("/WEB-INF/admindashboard/admindashboard.jsp").forward(request, response);

			} else if (path.equals("/superadmin/dashboard")) {

				request.getRequestDispatcher("/WEB-INF/admindashboard/superadmindashboard.jsp").forward(request, response);
			}
		} catch (ServletException | IOException e) {

			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

}
