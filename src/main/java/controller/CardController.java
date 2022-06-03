package controller;


import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.CardListAction;
import db.JdbcUtil;
import vo.ActionForward;


@WebServlet("*.bo")
public class CardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        hole(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        hole(request, response);
    }

    private void hole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String uri = request.getRequestURI();
        int pos = uri.lastIndexOf('/');
        int length = uri.length();
        String path = uri.substring(pos + 1, length - 3);
        ActionForward forward = null;
        Action action = null;
        
        if (path.equals("cardList")) {
            action = new CardListAction();
            try {
                forward = action.execute(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (path.equals("")) {

        }
        
        if (forward != null) {
        	if (forward.isRedirect()) {
        		response.sendRedirect(forward.getPath());
        	} else {
        		RequestDispatcher rd = request.getRequestDispatcher(forward.getPath());
        		rd.forward(request, response);
        	}
        }
    }

}