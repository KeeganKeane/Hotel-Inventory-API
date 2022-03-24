package jp.co.rakuten.inventoryapi.controller;

import jp.co.rakuten.inventoryapi.dto.IdEntity;
import jp.co.rakuten.inventoryapi.dto.SuccessEntity;
import jp.co.rakuten.inventoryapi.entity.Inventory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class InventoryControllerIT {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate = null;

    @BeforeAll
    public static void init() {
//        restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        baseUrl = baseUrl.concat(":").concat(port + "");
    }

    @Test
    public void saveInventory() {
        Inventory inventory = new Inventory();
        inventory.setName("Hotel 1");
        inventory.setType("LUXURY");

        IdEntity idEntity = restTemplate.postForObject(baseUrl.concat("/inventories"), inventory, IdEntity.class);

        assertNotNull(idEntity);
        assert idEntity.getId() > 0;
    }

    @Test
    public void getInventoryById() {
        Inventory inventoryPost = new Inventory();
        inventoryPost.setName("Prince Hotel");
        inventoryPost.setType("LUXURY");

        IdEntity idEntity = restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost, IdEntity.class);

        Integer id = idEntity.getId();

        Inventory inventoryGet = restTemplate.getForObject(baseUrl.concat("/inventories/{id}"), Inventory.class, id);

        assertNotNull(inventoryGet);
        assertEquals(id, inventoryGet.getId());
        assertEquals(inventoryPost.getName(), inventoryGet.getName());
        assertEquals(inventoryPost.getType(), inventoryGet.getType());
    }

    @Test
    public void patchInventory() {
        Inventory inventoryPost = new Inventory();
        inventoryPost.setName("Hotel to Patch");
        inventoryPost.setType("LUXURY");

        IdEntity idEntity = restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost, IdEntity.class);

        Integer id = idEntity.getId();

        Inventory sentInventoryPatch = new Inventory();
        sentInventoryPatch.setId(id);
        sentInventoryPatch.setName("Hotel is Patched");
        sentInventoryPatch.setType("LUXURY");
        sentInventoryPatch.setDescription("This hotel is patched now!");

        SuccessEntity successEntity = restTemplate.patchForObject(baseUrl.concat("/inventories"), sentInventoryPatch, SuccessEntity.class);

        Inventory inventoryGet = restTemplate.getForObject(baseUrl.concat("/inventories/{id}"), Inventory.class, id);

        assertEquals(true, successEntity.isSuccess());

        assertNotNull(inventoryGet);
        assertEquals(inventoryGet.getId(), sentInventoryPatch.getId(), inventoryPost.getId());
        assertEquals(inventoryGet.getName(), sentInventoryPatch.getName());
        assertNotEquals(inventoryGet.getName(), inventoryPost.getName());
        assertNotEquals(inventoryGet.getDescription(), inventoryPost.getDescription());
    }

    @Test
    public void getAllInventories() {
        Inventory inventoryPost = new Inventory();
        inventoryPost.setName("Hotel 1");
        inventoryPost.setType("LUXURY");
        inventoryPost.setAvailableFrom("2020-01-01");
        inventoryPost.setAvailableTo("2021-01-01");

        Inventory inventoryPost1 = new Inventory();
        inventoryPost1.setName("Hotel 2");
        inventoryPost1.setType("SUITE");
        inventoryPost1.setAvailableFrom("2020-10-01");
        inventoryPost1.setAvailableTo("2021-01-01");

        Inventory inventoryPost2 = new Inventory();
        inventoryPost2.setName("Hotel 3");
        inventoryPost2.setType("DELUXE");
        inventoryPost2.setAvailableFrom("2019-01-01");
        inventoryPost2.setAvailableTo("2022-01-01");

        restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost, IdEntity.class);
        restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost1, IdEntity.class);
        restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost2, IdEntity.class);

        List<Inventory> inventoryGetAll = restTemplate.getForObject(baseUrl.concat("/inventories"), List.class);

        assertNotNull(inventoryGetAll);
        assertEquals(3, inventoryGetAll.size());
    }

}
