package jp.co.rakuten.inventoryapi.validator;

import jp.co.rakuten.inventoryapi.entity.Inventory;
import jp.co.rakuten.inventoryapi.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static jp.co.rakuten.inventoryapi.constant.ErrorMessages.*;

/**
 * Inventory Validator that ensure user inputs are correctly formatted
 *
 * @author keegan.keane
 */
@Slf4j
public class InventoryValidator {

    /**
     * Validator for the Inventory POST call
     *
     * @param Inventory
     * @return void
     */
    public static void validateInventoryPOST(Inventory inventory) {
        validateName(inventory.getName());
        validateType(inventory.getType());
        validateDates(inventory.getAvailableFrom(), inventory.getAvailableTo());
    }

    /**
     * Validator for the Inventory PATCH call
     *
     * @param Inventory
     * @return void
     */
    public static void validateInventoryPATCH(Inventory inventory) {
        validateId(inventory.getId());
        validateName(inventory.getName());
        validateType(inventory.getType());
        validateDates(inventory.getAvailableFrom(), inventory.getAvailableTo());
    }

    /**
     * Validator for the Inventory ID
     *
     * @param Integer
     * @return void
     */
    public static void validateId(Integer id) {
        if (id == 0 || id == null) {
            throw new InvalidRequestException(INVALID_INVENTORY_ID);
        }
    }

    /**
     * Validator for the Inventory name
     *
     * @param String
     * @return void
     */
    public static void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new InvalidRequestException(INVALID_NAME);
        }
    }

    /**
     * Validator for available from and available to order
     *
     * @return void
     * @params String, String
     */
    public static void validateDates(String from, String to) {
        //Checks to see if one of the dates are null and throws an exception if only one date has been entered.
        if (StringUtils.hasText(from) != StringUtils.hasText(to)) {
            throw new InvalidRequestException(INVALID_DATE_NULL_VALUES);
        } else if (StringUtils.hasText(from) && StringUtils.hasText(to)) {
            //If the dates contain a string value then proceed.
            if (validateDateFormat(from) && validateDateFormat(to)) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    boolean orderIsValid = sdf.parse(from).before(sdf.parse(to));
                    if (!orderIsValid) {
                        throw new InvalidRequestException(INVALID_DATE_ORDER);
                    }
                } catch (ParseException e) {
                    log.debug("Invalid date comparison");
                }
            } else {
                throw new InvalidRequestException(INVALID_DATE);
            }
        } else {
            //Both dates are null, and therefore valid so no operation is performed
        }


    }

    /**
     * Validator for the date format (yyyy-MM-DD)
     *
     * @param String
     * @return boolean
     */
    private static boolean validateDateFormat(String date) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            log.debug("Invalid date " + date);
            throw new InvalidRequestException(INVALID_DATE);
        }
        return true;
    }

    /**
     * Validator for the Inventory type
     *
     * @param String
     * @return void
     */
    public static void validateType(String type) {
        if (type == null || !Arrays.asList("DELUXE", "LUXURY", "SUITE").contains(type)) {
            throw new InvalidRequestException(INVALID_TYPE);
        }
    }

}
