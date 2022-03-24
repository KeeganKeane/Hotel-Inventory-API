package jp.co.rakuten.inventoryapi.controller;

import jp.co.rakuten.inventoryapi.validator.InventoryValidator;
import jp.co.rakuten.inventoryapi.dto.IdEntity;
import jp.co.rakuten.inventoryapi.dto.SuccessEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jp.co.rakuten.inventoryapi.entity.Inventory;
import jp.co.rakuten.inventoryapi.service.InventoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Inventory Controller containing endpoints of Inventory related API Calls
 *
 * @author keegan.keane
 */
@RestController
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    /**
     * End point to get all Inventories in the database
     *
     * @return list of Inventories
     */
    @GetMapping(value = "/inventories", produces = "application/json")
    public List<Inventory> getInventoryList() {
        return inventoryService.getAllInventories();
    }

    /**
     * End point to get user specified Inventory
     *
     * @param id Integer
     * @return inventory Inventory
     */
    @GetMapping(value = "/inventories/{id}", produces = "application/json")
    public Inventory getInventory(@PathVariable Integer id) {
        InventoryValidator.validateId(id);
        return inventoryService.getInventory(id);
    }


    /**
     * End point to get list of Inventories available between user specified dates
     *
     * @param from String, to String
     * @return list of Inventories
     */
    //Request Param gets values listed after the question mark
    @GetMapping(value = "/inventories/availabilitySearch", produces = "application/json")
    public List<Inventory> getInventory(@RequestParam("dateFrom") String from, @RequestParam("dateTo") String to) {
        InventoryValidator.validateDates(from, to);
        return inventoryService.getAvailable(from, to);
    }

    /**
     * End point to get update user specified inventory.
     *
     * @param inventory Inventory object
     * @return successEntity
     */
    @PatchMapping(value = "/inventories", produces = "application/json")
    public SuccessEntity patchInventory(@RequestBody Inventory inventory) {
        InventoryValidator.validateInventoryPATCH(inventory);
        return inventoryService.patchInventory(inventory);
    }

    /**
     * End point to save a user specified inventory
     *
     * @param inventory Inventory
     * @return idEntity
     */
    @PostMapping(value = "/inventories", produces = "application/json")
    public IdEntity saveInventory(@RequestBody Inventory inventory) {
        InventoryValidator.validateInventoryPOST(inventory);
        return inventoryService.saveInventory(inventory);
    }

    /**
     * End point to delete a user specified inventory
     *
     * @param id Integer
     * @return SuccessEntity
     */
    @DeleteMapping(value = "/inventories/{id}", produces = "application/json")
    public SuccessEntity deleteInventory(@PathVariable Integer id) {
        InventoryValidator.validateId(id);
        return inventoryService.deleteInventory(id);
    }

}
