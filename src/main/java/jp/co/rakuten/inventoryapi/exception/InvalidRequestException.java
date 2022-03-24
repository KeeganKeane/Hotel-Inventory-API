package jp.co.rakuten.inventoryapi.exception;

/**
 * Invalid Request Exceptions for the Inventory API
 *
 * @author keegan.keane
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}