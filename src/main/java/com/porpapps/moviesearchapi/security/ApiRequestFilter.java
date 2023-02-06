package com.porpapps.moviesearchapi.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@Component
public class ApiRequestFilter extends GenericFilterBean {
    @Value("${cross.origin.url}")
    private String clientUrl;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final var req = (HttpServletRequest) request;
        final var path = req.getRequestURI();

        if (!path.startsWith("/api")) {
            chain.doFilter(request, response);
            return;
        }

        final var origin = req.getHeader("origin");
        if (clientUrl.equals(origin)) {
            chain.doFilter(request, response);
            return;
        }

        final var resp = (HttpServletResponse) response;
        final var error = "Invalid API KEY";

        resp.reset();
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentLength(error.length());
        response.getWriter().write(error);

        log.error("{} : {}", error, origin);
    }
}
