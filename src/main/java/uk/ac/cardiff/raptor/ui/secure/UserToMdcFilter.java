package uk.ac.cardiff.raptor.ui.secure;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.qos.logback.classic.ClassicConstants;

public class UserToMdcFilter implements Filter {

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {

			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof RaptorUser) {

				final RaptorUser user = (RaptorUser) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				MDC.put("user", user.getUsername());
			}
		}

		insertIntoMDC(request);

		try {
			chain.doFilter(request, response);
		} finally {
			MDC.remove("user");
		}
	}

	void insertIntoMDC(final ServletRequest request) {

		MDC.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, request.getRemoteHost());

		if (request instanceof HttpServletRequest) {
			final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			MDC.put(ClassicConstants.REQUEST_REQUEST_URI, httpServletRequest.getRequestURI());
			final StringBuffer requestURL = httpServletRequest.getRequestURL();
			if (requestURL != null) {
				MDC.put(ClassicConstants.REQUEST_REQUEST_URL, requestURL.toString());
			}
			MDC.put(ClassicConstants.REQUEST_METHOD, httpServletRequest.getMethod());
			MDC.put(ClassicConstants.REQUEST_QUERY_STRING, httpServletRequest.getQueryString());
			MDC.put(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY, httpServletRequest.getHeader("User-Agent"));
			MDC.put(ClassicConstants.REQUEST_X_FORWARDED_FOR, httpServletRequest.getHeader("X-Forwarded-For"));
		}

	}

	@Override
	public void destroy() {
		// do nothing
	}

	@Override
	public void init(final FilterConfig fc) throws ServletException {
		// do nothing

	}

}
