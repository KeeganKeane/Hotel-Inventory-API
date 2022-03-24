package jp.co.rakuten.inventoryapi.validator;

import jp.co.rakuten.inventoryapi.entity.Reservations;
import jp.co.rakuten.inventoryapi.exception.InvalidRequestException;
import jp.co.rakuten.inventoryapi.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static jp.co.rakuten.inventoryapi.constant.ErrorMessages.*;

/**
 * Reservation Validator that ensures user inputs are correctly formatted
 *
 * @author keegan.keane
 */
@Slf4j
public class ReservationValidator {
    @Autowired
    InventoryRepository inventoryRepository;

    /**
     * Validator for the Reservations POST call
     *
     * @param Reservations
     * @return void
     */
    public static void validateReservationPOST(Reservations reservations) {
        validateDates(reservations.getCheckIn(), reservations.getCheckOut());
        validateGuest(reservations.getGuests());
    }

    /**
     * Validator for ids
     *
     * @param Integer
     * @return void
     */
    public static void validateId(Integer id) {
        if (id == 0) {
            throw new InvalidRequestException(INVALID_INVENTORY_ID);
        }
    }

    /**
     * Validator for check in and check out dates
     *
     * @return void
     * @params String, String
     */
    public static void validateDates(String checkIn, String checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new InvalidRequestException(INVALID_DATE);
        }
        if (validateDateFormat(checkIn) && validateDateFormat(checkOut)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                boolean orderIsValid = sdf.parse(checkIn).before(sdf.parse(checkOut));
                if (!orderIsValid)
                    throw new InvalidRequestException(INVALID_DATE_ORDER);
            } catch (ParseException e) {
                log.debug("Invalid date comparison");
            }
        } else {
            throw new InvalidRequestException(INVALID_DATE);
        }

    }

    /**
     * Validator for date format (yyyy-MM-DD)
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
            return false;
        }
        return true;
    }

    /**
     * Validator for the guest number
     *
     * @param Integer
     * @return void
     */
    public static void validateGuest(Integer guests) {
        if (guests == null || guests <= 0) {
            throw new InvalidRequestException(INVALID_GUESTS);
        }
    }

}
