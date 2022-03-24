package jp.co.rakuten.inventoryapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

/**
 * Exception Handler for the Inventory API
 *
 * @author keegan.keane
 */

@ControllerAdvice
@Slf4j
public class InventoryApiExceptionHandler extends ExceptionHandlerExceptionResolver {

    /**
     * Exception handler for invalid requests.
     *
     * @param ex InvalidRequestException
     * @return 400 (Bad Request)
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorMessage> handleInvalidRequest(InvalidRequestException ex) {
        return handleBadRequest(ex);
    }

    private ResponseEntity<ErrorMessage> handleBadRequest(Exception ex) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);

        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

