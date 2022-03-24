/**
 * Collection of Error Messages for Invalid Requests
 *
 * @author keegan.keane
 */
package jp.co.rakuten.inventoryapi.constant;

public final class ErrorMessages {
    public static final String INVALID_NAME = "Invalid Name: Name cannot be null";
    public static final String INVALID_TYPE = "Invalid Type: Must be one of the following [DELUXE, LUXURY, SUITE]";

    public static final String INVALID_DATE = "Invalid date: Please input dates in yyyy-MM-dd format";
    public static final String INVALID_DATE_ORDER = "Invalid date: Start date must be before end date";
    public static final String INVALID_RESERVATION_DATES = "Invalid Reservation Dates: The check in and/or check out dates are outside the available dates.";
    public static final String INVALID_DATE_OVERLAP = "Invalid Date: The entered date overlaps with an already registered reservation.";
    public static final String INVALID_DATE_NULL_VALUES = "Invalid Date: If a date is entered, both the available to and the available from must be provided.";
    public static final String EMPTY_INVENTORY_DATES = "Invalid Inventory: A reservation can not be made as the user specified inventory does not have available dates.";
    public static final String INVALID_DATE_CHANGE_NULL = "Invalid Date Change: Inventory contains a reservation, therefore null inventory dates cannot be entered.";

    public static final String INVALID_INVENTORY_ID = "Invalid Inventory ID: Inventory ID can not be null";
    public static final String INVALID_INVENTORY_IN_RESERVATION = "Invalid Inventory ID: The Inventory ID does not exist";
    public static final String INVALID_ID_EXISTENCE = "Invalid ID: The id entered does not exist";
    public static final String INVALID_GUESTS = "Invalid Guests: Guests must be a non-zero number";

    public static final String PARSE_ERROR = "Internal Error: A parsing error occurred.";

    public static final String INVALID_INVENTORY_DELETE = "Invalid Request: Cannot delete inventory as there are active reservations.";
    public static final String INVALID_INVENTORY_UPDATE = "Invalid Inventory Update: Cannot update user specified inventory as the new dates conflict with an active reservation.";

    public static final String INVALID_RESERVATION_DUPLICATE = "Duplicate Value: A Reservation with this ID already exists";
}

