package com.indo.game.controller.sgwin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SGWinLogin
 */
@WebServlet("/SGWinLogin")
public class SGWinLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SGWinLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      APIUserController userController = new APIUserController();
	      try {
	        
	        String loginResponse = userController.login();
	        
	        //if login success, the response is {"success":true,"session":"8c517ed44e5f7112e876dab6096fd40adba34881"}
	        if(loginResponse.indexOf("success")>0){
	          response.sendRedirect("http://rd.localhost:8080/member/index?_OLID_="+loginResponse.substring(27, 67));
	          
	        }
	        
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	      
	}

}
