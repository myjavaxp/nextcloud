package com.yibo.oauth.config;

import com.yibo.entity.common.ResponseEntity;
import com.yibo.entity.common.Status;
import com.yibo.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler implements ErrorController {
    private static final String ERROR_PATH = "/error";
    private final ErrorAttributes errorAttributes;

    @Autowired
    public GlobalExceptionHandler(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @GetMapping(ERROR_PATH)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> errorApiHandler(HttpServletRequest request, Exception e) {
        if (e instanceof CommonException) {
            CommonException commonException = (CommonException) e;
            e.printStackTrace();
            return new ResponseEntity<>(commonException.getCode(), commonException.getMessage());
        }
        if (e instanceof NullPointerException) {
            e.printStackTrace();
            return new ResponseEntity<>(Status.NULL_POINTER_EXCEPTION);
        }
        if (e instanceof BadSqlGrammarException) {
            e.printStackTrace();
            return new ResponseEntity<>(Status.NOT_VALID_SQL);
        }
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest, false);
        Integer statusCode = getStatus(request);
        e.printStackTrace();
        Object error = errorAttributes.get("error");
        if (error != null && !"None".equals(String.valueOf(error))) {
            return new ResponseEntity<>(statusCode, String.valueOf(error));
        }
        return new ResponseEntity<>(statusCode, String.valueOf(errorAttributes.getOrDefault("message", "error")));
    }

    private Integer getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return INTERNAL_SERVER_ERROR.value();
        }
        return statusCode;
    }
}