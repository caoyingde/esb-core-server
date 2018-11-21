package org.esb.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public interface Handler {
    /**
     * invoke.
     *
     * @param request  request.
     * @param response response.
     * @throws IOException
     * @throws ServletException
     */
    void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
