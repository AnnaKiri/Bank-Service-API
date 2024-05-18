package ru.kirillova.bankservice.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.kirillova.bankservice.error.AppException;
import ru.kirillova.bankservice.error.DataConflictException;
import ru.kirillova.bankservice.error.ErrorType;
import ru.kirillova.bankservice.error.IllegalRequestDataException;
import ru.kirillova.bankservice.error.NotFoundException;

import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static ru.kirillova.bankservice.error.ErrorType.APP_ERROR;
import static ru.kirillova.bankservice.error.ErrorType.BAD_DATA;
import static ru.kirillova.bankservice.error.ErrorType.BAD_REQUEST;
import static ru.kirillova.bankservice.error.ErrorType.DATA_CONFLICT;
import static ru.kirillova.bankservice.error.ErrorType.FORBIDDEN;
import static ru.kirillova.bankservice.error.ErrorType.NOT_FOUND;
import static ru.kirillova.bankservice.error.ErrorType.UNAUTHORIZED;


@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class RestExceptionHandler {
    public static final String ERR_PFX = "ERR# ";

    static final Map<Class<? extends Throwable>, ErrorType> HTTP_STATUS_MAP = new LinkedHashMap<>() {
        {
            put(NotFoundException.class, NOT_FOUND);
            put(AuthenticationException.class, UNAUTHORIZED);
            put(FileNotFoundException.class, NOT_FOUND);
            put(NoHandlerFoundException.class, NOT_FOUND);
            put(DataConflictException.class, DATA_CONFLICT);
            put(IllegalRequestDataException.class, BAD_REQUEST);
            put(AppException.class, APP_ERROR);
            put(UnsupportedOperationException.class, APP_ERROR);
            put(EntityNotFoundException.class, DATA_CONFLICT);
            put(DataIntegrityViolationException.class, DATA_CONFLICT);
            put(IllegalArgumentException.class, BAD_DATA);
            put(ValidationException.class, BAD_REQUEST);
            put(HttpRequestMethodNotSupportedException.class, BAD_REQUEST);
            put(ServletRequestBindingException.class, BAD_REQUEST);
            put(RequestRejectedException.class, BAD_REQUEST);
            put(AccessDeniedException.class, FORBIDDEN);
        }
    };

    @Getter
    private final MessageSource messageSource;

    @NonNull
    private static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    @ExceptionHandler(BindException.class)
    ProblemDetail bindException(BindException ex, HttpServletRequest request) {
        Map<String, String> invalidParams = getErrorMap(ex.getBindingResult());
        String path = request.getRequestURI();
        log.warn(ERR_PFX + "BindException with invalidParams {} at request {}", invalidParams, path);
        return createProblemDetail(ex, path, BAD_REQUEST, "BindException", Map.of("invalid_params", invalidParams));
    }

    private Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> invalidParams = new LinkedHashMap<>();
        for (ObjectError error : result.getGlobalErrors()) {
            invalidParams.put(error.getObjectName(), getErrorMessage(error));
        }
        for (FieldError error : result.getFieldErrors()) {
            invalidParams.put(error.getField(), getErrorMessage(error));
        }
        return invalidParams;
    }

    private String getErrorMessage(ObjectError error) {
        return error.getCode() == null ? error.getDefaultMessage() :
                messageSource.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail exception(Exception ex, HttpServletRequest request) {
        return processException(ex, request, Map.of());
    }

    ProblemDetail processException(@NonNull Exception ex, HttpServletRequest request, Map<String, Object> additionalParams) {
        String path = request.getRequestURI();
        Class<? extends Exception> exClass = ex.getClass();
        Optional<ErrorType> optType = HTTP_STATUS_MAP.entrySet().stream()
                .filter(
                        entry -> entry.getKey().isAssignableFrom(exClass)
                )
                .findAny().map(Map.Entry::getValue);
        if (optType.isPresent()) {
            log.error(ERR_PFX + "Exception {} at request {}", ex, path);
            return createProblemDetail(ex, path, optType.get(), ex.getMessage(), additionalParams);
        } else {
            Throwable root = getRootCause(ex);
            log.error(ERR_PFX + "Exception " + root + " at request " + path, root);
            return createProblemDetail(ex, path, APP_ERROR, "Exception " + root.getClass().getSimpleName(), additionalParams);
        }
    }

    private ProblemDetail createProblemDetail(Exception ex, String path, ErrorType type, String defaultDetail, @NonNull Map<String, Object> additionalParams) {
        ErrorResponse.Builder builder = ErrorResponse.builder(ex, type.status, defaultDetail);
        ProblemDetail pd = builder
                .title(type.title).instance(URI.create(path))
                .build().updateAndGetBody(messageSource, LocaleContextHolder.getLocale());
        additionalParams.forEach(pd::setProperty);
        return pd;
    }
}