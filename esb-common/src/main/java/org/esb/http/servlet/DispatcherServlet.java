package org.esb.http.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esb.http.Handler;

public class DispatcherServlet extends HttpServlet{
private static final long serialVersionUID = 5766349180380479888L;
	
	private static DispatcherServlet INSTANCE;

    private static final Map<Integer, Handler> handlers = new ConcurrentHashMap<Integer, Handler>();

    public static void addHttpHandler(int port, Handler processor) {
        handlers.put(port, processor);
    }

    public static void removeHttpHandler(int port) {
        handlers.remove(port);
    }
    
    public static DispatcherServlet getInstance() {
    	return INSTANCE;
    }
    
    public DispatcherServlet() {
    	DispatcherServlet.INSTANCE = this;
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
        Handler handler = handlers.get(request.getLocalPort());
        if( handler == null ) {// service not found.
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service not found.");
        } else {
            handler.handle(request, response);
        }
    }
}
