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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class InventoryControllerDeleteIT {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate = null;

    @BeforeAll
    public static void init() {
    }

    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        baseUrl = baseUrl.concat(":").concat(port + "");
    }

    @Test
    public void deleteInventory() {

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

        IdEntity idEntity = restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost, IdEntity.class);
        restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost1, IdEntity.class);

        Integer id = idEntity.getId();

        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        restTemplate.delete(baseUrl.concat("/inventories/{id}"), params);

        List<Inventory> inventoryGetAll = restTemplate.getForObject(baseUrl.concat("/inventories"), List.class);

        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.getForObject(baseUrl.concat("/inventories/{id}"), Inventory.class, id);
        });
        assertNotNull(inventoryGetAll);
        assertEquals(1, inventoryGetAll.size());
    }


}
