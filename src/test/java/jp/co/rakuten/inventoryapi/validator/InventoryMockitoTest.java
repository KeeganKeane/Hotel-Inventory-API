package jp.co.rakuten.inventoryapi.validator;

import jp.co.rakuten.inventoryapi.dto.IdEntity;
import jp.co.rakuten.inventoryapi.entity.Inventory;
import jp.co.rakuten.inventoryapi.repository.InventoryRepository;
import jp.co.rakuten.inventoryapi.service.InventoryService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Inventory Tester that uses Mockito to perform integration testing.
 *
 * @author keegan.keane
 */
public class InventoryMockitoTest {

    @InjectMocks
    InventoryService inventoryService;

    @Mock
    InventoryRepository inventoryRepository;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllInventoriesTest() {
        List<Inventory> list = new ArrayList<>();
        Inventory inventory1 = new Inventory();
        inventory1.setName("Hotel 1");
        inventory1.setDescription("Hotel 1 is the number 1 hotel in the world.");
        inventory1.setAvailableFrom("2020-01-01");
        inventory1.setAvailableTo("2021-01-01");
        inventory1.setType("LUXURY");

        Inventory inventory2 = new Inventory();
        inventory1.setName("Hotel 2");
        inventory1.setDescription("Hotel 2 is the number 2 hotel in the world.");
        inventory1.setAvailableFrom("2020-01-01");
        inventory1.setAvailableTo("2021-01-01");
        inventory1.setType("SUITE");

        list.add(inventory1);
        list.add(inventory2);

        when(inventoryRepository.findAll()).thenReturn(list);

        //test
        List<Inventory> inventoryList = inventoryService.getAllInventories();

        assertEquals(2, inventoryList.size());
        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    public void inventoryPOSTTest() {
        Inventory inventory1 = new Inventory();
        inventory1.setName("Hotel 1");
        inventory1.setDescription("Hotel 1 is the number 1 hotel in the world.");
        inventory1.setAvailableFrom("2020-01-01");
        inventory1.setAvailableTo("2021-01-01");
        inventory1.setType("LUXURY");

        IdEntity idEntity = new IdEntity();
        idEntity.setId(0);

        when(inventoryRepository.save(inventory1)).thenReturn(inventory1);

        //test
        IdEntity idEntityResponse = inventoryService.saveInventory(inventory1);

        assertEquals(idEntity, idEntityResponse);
        verify(inventoryRepository, times(1)).save(inventory1);
    }
}
