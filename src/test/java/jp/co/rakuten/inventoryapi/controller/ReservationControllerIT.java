package jp.co.rakuten.inventoryapi.controller;

import jp.co.rakuten.inventoryapi.dto.IdEntity;
import jp.co.rakuten.inventoryapi.entity.Inventory;
import jp.co.rakuten.inventoryapi.entity.Reservations;
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

@Transactional
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationControllerIT {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate = null;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "");
    }

    @Test
    public void saveReservation() {
        Inventory inventory = new Inventory();
        inventory.setName("Hotel 1");
        inventory.setType("LUXURY");
        inventory.setAvailableFrom("2020-01-01");
        inventory.setAvailableTo("2022-01-01");

        IdEntity inventoriesIdEntity = restTemplate.postForObject(baseUrl.concat("/inventories"), inventory, IdEntity.class);

        Reservations reservations = new Reservations();
        reservations.setInventoryId(inventoriesIdEntity.getId());
        reservations.setGuests(5);
        reservations.setCheckIn("2020-05-01");
        reservations.setCheckOut("2020-07-01");

        IdEntity reservationsIdEntity = restTemplate.postForObject(baseUrl.concat("/reservations"), reservations, IdEntity.class);

        assertNotNull(inventoriesIdEntity);
        assertNotNull(reservationsIdEntity);
        assert inventoriesIdEntity.getId() > 0;
        assert reservationsIdEntity.getId() > 0;
        assertEquals(inventoriesIdEntity.getId(), reservations.getInventoryId());
    }

    @Test
    public void getReservationsById() {
        Inventory inventory = new Inventory();
        inventory.setName("Hotel 1");
        inventory.setType("LUXURY");
        inventory.setAvailableFrom("2020-01-01");
        inventory.setAvailableTo("2022-01-01");

        IdEntity inventoriesIdEntity = restTemplate.postForObject(baseUrl.concat("/inventories"), inventory, IdEntity.class);

        Reservations reservations = new Reservations();
        reservations.setInventoryId(inventoriesIdEntity.getId());
        reservations.setGuests(5);
        reservations.setCheckIn("2020-05-01");
        reservations.setCheckOut("2020-07-01");

        IdEntity reservationsIdEntity = restTemplate.postForObject(baseUrl.concat("/reservations"), reservations, IdEntity.class);

        Integer id = reservationsIdEntity.getId();

        Reservations reservationsGet = restTemplate.getForObject(baseUrl.concat("/reservations/{id}"), Reservations.class, id);

        assertNotNull(reservationsGet);
        assertEquals(id, reservationsGet.getId());
    }

    @Test
    public void getAllReservations() {
        Inventory inventoryPost = new Inventory();
        inventoryPost.setName("Hotel 1");
        inventoryPost.setType("LUXURY");
        inventoryPost.setAvailableFrom("2020-01-01");
        inventoryPost.setAvailableTo("2021-01-01");

        Inventory inventoryPost1 = new Inventory();
        inventoryPost1.setName("Hotel 2");
        inventoryPost1.setType("LUXURY");
        inventoryPost1.setAvailableFrom("2020-01-01");
        inventoryPost1.setAvailableTo("2021-01-01");

        IdEntity idEntity1 = restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost, IdEntity.class);
        IdEntity idEntity2 = restTemplate.postForObject(baseUrl.concat("/inventories"), inventoryPost1, IdEntity.class);

        Reservations reservations1 = new Reservations();
        reservations1.setInventoryId(idEntity1.getId());
        reservations1.setGuests(5);
        reservations1.setCheckIn("2020-05-01");
        reservations1.setCheckOut("2020-07-01");

        Reservations reservations2 = new Reservations();
        reservations2.setInventoryId(idEntity2.getId());
        reservations2.setGuests(5);
        reservations2.setCheckIn("2020-05-01");
        reservations2.setCheckOut("2020-07-01");

        restTemplate.postForObject(baseUrl.concat("/reservations"), reservations1, IdEntity.class);
        restTemplate.postForObject(baseUrl.concat("/reservations"), reservations2, IdEntity.class);

        List<Reservations> reservationsGetAll = restTemplate.getForObject(baseUrl.concat("/reservations"), List.class);

        assertNotNull(reservationsGetAll);
        assertEquals(2, reservationsGetAll.size());
    }

}
