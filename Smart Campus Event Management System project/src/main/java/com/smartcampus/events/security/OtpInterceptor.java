package com.smartcampus.events.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class OtpInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        
        // Skip static resources, api, and auth paths
        if (uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/") 
            || uri.startsWith("/h2-console") || uri.equals("/") || uri.equals("/login") 
            || uri.startsWith("/register") || uri.startsWith("/login/otp")) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            HttpSession session = request.getSession();
            Boolean otpVerified = (Boolean) session.getAttribute("loginOtpVerified");
            if (otpVerified == null || !otpVerified) {
                if (!uri.startsWith("/login/otp")) {
                    response.sendRedirect("/login/otp");
                    return false;
                }
            }
        }
        return true;
    }
}
