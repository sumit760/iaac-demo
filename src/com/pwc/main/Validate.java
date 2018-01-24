package com.pwc.main;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Validate
 */
//@WebServlet("/Validate")
public class Validate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Validate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("uname");
		String password = request.getParameter("psw");

//		DatabseValidation dbVali = new DatabseValidation();

		 if ((name.equals("admin")) && (password.equals("admin")))
		{
			request.setAttribute("Name", name);
			request.getRequestDispatcher("Welcome.jsp").forward(request, response);
		}

		 else {
		 request.getRequestDispatcher("Failure.jsp").forward(request, response);
		
		 }

	}

}
