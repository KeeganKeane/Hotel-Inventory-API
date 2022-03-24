package jp.co.rakuten.inventoryapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for the Error Messages
 *
 * @author keegan.keane
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private String message;
}