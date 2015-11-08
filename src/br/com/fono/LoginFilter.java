package br.com.fono;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.fono.mb.LoginMB;

public class LoginFilter implements Serializable, Filter {
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		LoginMB loginMB = (LoginMB)((HttpServletRequest)request).getSession().getAttribute("loginMB");

        if (loginMB == null || !loginMB.isLoggedIn()) {
            String contextPath = ((HttpServletRequest)request).getContextPath();
            ((HttpServletResponse)response).sendRedirect(contextPath + "/login.jsf");
        }
        chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
