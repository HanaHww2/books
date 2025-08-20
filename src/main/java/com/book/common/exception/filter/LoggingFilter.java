package com.book.common.exception.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    long start = System.currentTimeMillis();
    try {
      filterChain.doFilter(requestWrapper, responseWrapper);

      long end = System.currentTimeMillis();
      LoggingData.of(requestWrapper, responseWrapper, (end - start) / 1000.0,
              responseWrapper.getStatus(), null)
          .log();
    } catch (Exception e) {
      long end = System.currentTimeMillis();

      HttpStatus status = HttpStatus.resolve(responseWrapper.getStatus());
      if (status == null || status.is2xxSuccessful()) status = HttpStatus.INTERNAL_SERVER_ERROR;
      LoggingData.of(requestWrapper, responseWrapper, (end - start) / 1000.0,
              status.value(), e)
          .log();
      throw e;
    } finally {
      responseWrapper.copyBodyToResponse();
    }
  }

  record LoggingData(
      String requestUri,
      String method,
      int status,
      Map<String, String> headers,
      Map<String, String> queryString,
      String requestBody,
      String responseBody,
      double elapsedTime,
      String errorClass,
      String errorMessage,
      String rootErrorClass,
      String rootErrorMessage,
      Throwable error // 5xx일 때 스택트레이스 포함용
  ) {

    static LoggingData of(ContentCachingRequestWrapper requestWrapper,
        ContentCachingResponseWrapper responseWrapper,
        double elapsedTime,
        int status,
        Throwable error) {

      Throwable root = rootCause(error);

      return new LoggingData(
          requestWrapper.getRequestURI(),
          requestWrapper.getMethod(),
          status,
          getHeaders(requestWrapper),
          getQueryParameter(requestWrapper),
          contentBody(requestWrapper.getContentAsByteArray()),
          contentBody(responseWrapper.getContentAsByteArray()),
          elapsedTime,
          error != null ? error.getClass().getSimpleName() : null,
          error != null ? singleLine(error.getMessage()) : null,
          root != null ? root.getClass().getSimpleName() : null,
          root != null ? singleLine(root.getMessage()) : null,
          error
      );
    }

    private static Map<String, String> getHeaders(HttpServletRequest request) {
      Map<String, String> headerMap = new HashMap<>();
      Enumeration<String> headerArray = request.getHeaderNames();
      while (headerArray.hasMoreElements()) {
        String headerName = headerArray.nextElement();
        headerMap.put(headerName, request.getHeader(headerName));
      }
      return headerMap;
    }

    private static Map<String, String> getQueryParameter(HttpServletRequest request) {
      Map<String, String> queryMap = new HashMap<>();
      request.getParameterMap()
          .forEach((key, values) -> queryMap.put(key, String.join(";", values)));
      return queryMap;
    }

    private static String contentBody(final byte[] contents) {
      return new String(contents);
    }

    private static Throwable rootCause(Throwable e) {
      if (e == null) return null;
      Throwable cur = e, next = e.getCause();
      while (next != null && next != cur) { cur = next; next = cur.getCause(); }
      return cur;
    }

    private static String singleLine(String s) {
      if (s == null) return null;
      return s.replaceAll("[\\r\\n\\t]+", " ").trim();
    }

    void log() {

      String tmpl = "HTTP method={} uri={} status={} elapsed={}s ex={} msg={} rootEx={} rootMsg={} headers={} query={} req={} res={}";

      if (status >= 500) {
        log.error(tmpl, method, requestUri, status, elapsedTime,
            errorClass, errorMessage, rootErrorClass, rootErrorMessage,
            headers, queryString, requestBody, responseBody, error);
      } else if (status >= 400) {
        log.warn(tmpl, method, requestUri, status, elapsedTime,
            errorClass, errorMessage, rootErrorClass, rootErrorMessage,
            headers, queryString, requestBody, responseBody);
      } else {
        log.info(tmpl, method, requestUri, status, elapsedTime,
            errorClass, errorMessage, rootErrorClass, rootErrorMessage,
            headers, queryString, requestBody, responseBody);
      }
    }
  }
}