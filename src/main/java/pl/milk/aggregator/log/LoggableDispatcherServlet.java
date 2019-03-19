package pl.milk.aggregator.log;

import org.jboss.logging.LogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;
import pl.milk.aggregator.AggregatorApplication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.StringJoiner;

public class LoggableDispatcherServlet extends DispatcherServlet {

  private static final Logger logger = LoggerFactory.getLogger("ReqRspLogger");
  private static final String DELIMITER = "; ";

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        HandlerExecutionChain handler = getHandler(request);

        try {
            super.doDispatch(request, response);
        } finally {
            log(request, response, handler);
            updateResponse(response);
        }
    }

    private void log(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain handler) {
        StringJoiner stringJoiner = new StringJoiner(DELIMITER);
        stringJoiner.add("Status: " + response.getStatus());
        stringJoiner.add("Method: " + request.getMethod());
        stringJoiner.add("Request URI: " + request.getRequestURI());
        stringJoiner.add("Client IP: " + request.getRemoteAddr());
        stringJoiner.add("Java method: " + handler.toString());
        stringJoiner.add("Headers: ["+getHeadersFromRequest(request)+"]");
        logger.debug(stringJoiner.toString());
    }

    private String getHeadersFromRequest(HttpServletRequest requestToCache) {
        StringJoiner stringJoiner = new StringJoiner(DELIMITER);
        Collections.list(requestToCache.getHeaderNames())
                .stream()
                .forEach(h -> stringJoiner.add(prepareHeader(h, requestToCache)));
        return stringJoiner.toString();
    }

    private String prepareHeader(String header, HttpServletRequest requestToCache){
        //no need to use string builder for simple concatenation

        return header + ": " + requestToCache.getHeader(header);
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        responseWrapper.copyBodyToResponse();
    }

}
