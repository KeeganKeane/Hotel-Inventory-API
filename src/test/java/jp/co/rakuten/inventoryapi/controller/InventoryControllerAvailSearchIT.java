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
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class InventoryControllerAvailSearchIT {

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
    public void getAvailabilitySearch() {
        Inventory inventoryPost = new Inventory();
        inventoryPost.setName("Hotel 2020");
        inventoryPost.setType("LUXURY");
        inventoryPost.setAvailableFrom("2020-01-01");
        inventoryPost.setAvailableTo("2021-01-01");

        Inventory inventoryPost1 = new Inventory();
        inventoryPost1.setName("Hotel Limited");
        inventoryPost1.setType("LUXURY");
        inventoryPost1.setAvailableFrom("2020-10-01");
        inventoryPost1.setAvailableTo("2021-01-01");

        restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost, IdEntity.class);
        restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost1, IdEntity.class);

        String from = "2020-01-31";
        String to = "2020-12-01";

        List<Inventory> inventoryAvailGet = restTemplate.getForObject(baseUrl.concat("/inventories/availabilitySearch?dateFrom=2020-01-31&dateTo=2020-03-01"), List.class);

        System.out.println("List: " + inventoryAvailGet);
        assertNotNull(inventoryAvailGet);
        assertEquals(1, inventoryAvailGet.size());
    }


}
