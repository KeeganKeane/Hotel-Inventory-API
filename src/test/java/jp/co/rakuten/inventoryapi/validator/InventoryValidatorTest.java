package jp.co.rakuten.inventoryapi.validator;

import jp.co.rakuten.inventoryapi.entity.Inventory;
import jp.co.rakuten.inventoryapi.exception.InvalidRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InventoryValidatorTest {

    @Test
    public void validateName_Valid() {
        String name = "Hilton";
        Assertions.assertDoesNotThrow(() -> InventoryValidator.validateName(name));
    }

    @Test
    public void validateName_InValid1() {
        String name = null;
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateName(name));
    }

    @Test
    public void validateName_InValid2() {
        String name = "     ";
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateName(name));
    }

    @Test
    public void validateDates_ValidCase() {
        String from = "2020-01-01";
        String to = "2021-01-01";
        Assertions.assertDoesNotThrow(() -> InventoryValidator.validateDates(from, to));
    }

    @Test
    public void validateDates_ValidCase2() {
        String from = " ";
        String to = " ";
        Assertions.assertDoesNotThrow(() -> InventoryValidator.validateDates(from, to));
    }

    @Test
    public void validateDates_InValid1() {
        String from = "2022-01-01";
        String to = "2021-01-01";
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateDates(from, to));
    }

    @Test
    public void validateDates_InValid2() {
        String from = " ";
        String to = "2021-01-01";
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateDates(from, to));
    }

    @Test
    public void validateDates_InValid3() {
        String from = "01-01-2020";
        String to = "2021-01-01";
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateDates(from, to));
    }

    @Test
    public void validateDates_InValid4() {
        String from = "hello";
        String to = "2021-01-01";
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateDates(from, to));
    }

    @Test
    public void validateType_ValidCase1() {
        String type = "DELUXE";
        Assertions.assertDoesNotThrow(() -> InventoryValidator.validateType(type));
    }

    @Test
    public void validateType_ValidCase2() {
        String type = "LUXURY";
        Assertions.assertDoesNotThrow(() -> InventoryValidator.validateType(type));
    }

    @Test
    public void validateType_ValidCase3() {
        String type = "SUITE";
        Assertions.assertDoesNotThrow(() -> InventoryValidator.validateType(type));
    }

    @Test
    public void validateType_Invalid1() {
        String type = "hello";
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateType(type));
    }

    @Test
    public void validateType_Invalid2() {
        String type = "   ";
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateType(type));
    }

    @Test
    public void validateType_Invalid3() {
        String type = null;
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateType(type));
    }

    @Test
    void validateInventoryPOST_ValidCase1() {
        Inventory inventory = new Inventory();
        inventory.setName("Hilton");
        inventory.setAvailableFrom("2020-01-01");
        inventory.setAvailableTo("2021-01-01");
        inventory.setType("LUXURY");
        Assertions.assertDoesNotThrow(() -> InventoryValidator.validateInventoryPOST(inventory));
    }

    @Test
    void validateInventoryPOST_ValidCase2() {
        Inventory inventory = new Inventory();
        inventory.setName("Hilton");
        inventory.setType("LUXURY");
        Assertions.assertDoesNotThrow(() -> InventoryValidator.validateInventoryPOST(inventory));
    }

    @Test
    void validateInventoryPOST_InValidCase1() {
        Inventory inventory = new Inventory();
        inventory.setName(" ");
        inventory.setAvailableFrom("2020-01-01");
        inventory.setAvailableTo("2021-01-01");
        inventory.setType("LUXURY");
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateInventoryPOST(inventory));
    }

    @Test
    void validateInventoryPOST_InValidCase2() {
        Inventory inventory = new Inventory();
        inventory.setName("Hilton");
        inventory.setAvailableFrom(" ");
        inventory.setAvailableTo("2021-01-01");
        inventory.setType("LUXURY");
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateInventoryPOST(inventory));
    }

    @Test
    void validateInventoryPOST_InValidCase3() {
        Inventory inventory = new Inventory();
        inventory.setName("Hilton");
        inventory.setAvailableFrom("2024-01-01");
        inventory.setAvailableTo("2021-01-01");
        inventory.setType("LUXURY");
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateInventoryPOST(inventory));
    }

    @Test
    void validateInventoryPOST_InValidCase4() {
        Inventory inventory = new Inventory();
        inventory.setName("Hilton");
        inventory.setAvailableFrom("2020-01-01");
        inventory.setAvailableTo("2021-01-01");
        inventory.setType("hotel");
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateInventoryPOST(inventory));
    }

    @Test
    void validateInventoryPOST_InValidCase5() {
        Inventory inventory = new Inventory();
        inventory.setName("Hilton");
        inventory.setAvailableFrom("2020-01-01");
        inventory.setAvailableTo("2021-01-01");
        inventory.setType(null);
        Assertions.assertThrows(InvalidRequestException.class, () -> InventoryValidator.validateInventoryPOST(inventory));
    }

}
