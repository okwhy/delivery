package dev.delivery;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.delivery.dtos.*;
import dev.delivery.dtos.auth.JwtResponce;
import dev.delivery.enums.Status;
import dev.delivery.services.InitializerService;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BaseTest {
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    private InitializerService initializerService;

    private static String managerToken;
    private static String packerToken;
    private static String courierToken;

   

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17.2")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    @BeforeAll
    void setUp() {
        postgres.start();
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());
        initializerService.init();
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    private HttpEntity<?> userWithTokenRequest(String token, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<?> courierWithTokenRequest(Object body) {
        return userWithTokenRequest(courierToken, body);
    }

    private HttpEntity<?> packerWithTokenRequest(Object body) {
        return userWithTokenRequest(packerToken, body);
    }

    private HttpEntity<?> managerWithTokenRequest(Object body) {
        return userWithTokenRequest(managerToken, body);
    }

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    private static final String TEST_PHONE = "+7(900) 123-45-67";
    private static final String TEST_FIO = "Иван Иванов";

    @Test
    @Order(1)
    void testClientRegistration() {
        // Создаем DTO клиента для регистрации
        ClientDto newClient = new ClientDto(
                TEST_FIO, // ФИО
                TEST_PHONE // Номер телефона
        );

        // Отправляем POST запрос на регистрацию клиента
        HttpEntity<?> request = new HttpEntity<>(newClient);
        ResponseEntity<ClientDto> response = restTemplate.exchange(
                "/api/register",
                HttpMethod.POST,
                request,
                ClientDto.class
        );
        // Проверяем статус и ответ
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFio()).isEqualTo(newClient.getFio());
        assertThat(response.getBody().getPhoneNumber()).isEqualTo(newClient.getPhoneNumber());
    }

    @Test
    @Order(2)
    void testClientLogin() {
        // Успешный логин клиента, который был зарегистрирован в предыдущем тесте
        String phone = TEST_PHONE;

        // Отправляем POST запрос на логин
        ResponseEntity<ClientDto> response = restTemplate.exchange(
                "/api/login?phone={phone}",
                HttpMethod.POST,
                null, // Тело запроса не требуется
                ClientDto.class,
                phone
        );
        log.info("Response body: {}", response.getBody());
        // Проверяем статус и ответ
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPhoneNumber()).isEqualTo(phone);
        assertThat(response.getBody().getFio()).isEqualTo(TEST_FIO);
    }

    @Test
    @Order(3)
    void testGetAllTags() {
        // Отправляем GET запрос на получение всех тегов
        ResponseEntity<List<TagDto>> response = restTemplate.exchange(
                "/api/products/tags",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // Проверяем статус и ответ
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThan(0); // Убедимся, что теги есть
    }

    @Test
    @Order(4)
    void testGetAllProductsWithoutTags() {
        // Отправляем GET запрос на получение всех продуктов (без фильтрации по тегам)
        ResponseEntity<PageImpl<ProductInCatalogDto>> response = restTemplate.exchange(
                "/api/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // Проверяем статус и ответ
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent().size()).isGreaterThan(0); // Продукты должны быть в каталоге
    }

    @Test
    @Order(5)
    void testGetProductsWithTags() {
        // Получаем список всех тегов
        ResponseEntity<List<TagDto>> tagsResponse = restTemplate.exchange(
                "/api/products/tags",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(tagsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(tagsResponse.getBody()).isNotNull();
        List<TagDto> tags = tagsResponse.getBody();

        // Выбираем первый тег для фильтрации
        List<TagDto> selectedTags = tags.subList(0, 1);

        // Формируем параметры запроса
        String tagQuery = selectedTags.stream()
                .map(TagDto::getTag)
                .reduce((t1, t2) -> t1 + "," + t2)
                .orElse("");

        // Отправляем GET запрос на получение продуктов по указанным тегам
        ResponseEntity<PageImpl<ProductInCatalogDto>> response = restTemplate.exchange(
                "/api/products?tags=" + tagQuery,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageImpl<ProductInCatalogDto>>() {
                }
        );

        // Проверяем статус и ответ
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent().size()).isGreaterThan(0); // Должны быть продукты с указанными тегами
    }

    @Test
    @Order(6)
    void testCreateOrder() {
        OrderForClientDto testOrder = new OrderForClientDto();
        testOrder.setClient(new ClientDto(TEST_FIO, TEST_PHONE));

        AddressInfoDto address = new AddressInfoDto();
        address.setLatitude(55.7558);
        address.setLongitude(37.6173);
        address.setAddress("Москва, ул. Тестовая, д.1");
        testOrder.setAddress(address);

        ProductItemInfoDto productItem = new ProductItemInfoDto();
        productItem.setQuantity(1);
        productItem.setProduct(new ProductInOrderDto(1L));  // Продукт с id = 1
        Set<ProductItemInfoDto> products = new HashSet<>();
        products.add(productItem);
        testOrder.setProducts(products);
        HttpEntity<OrderForClientDto> request = new HttpEntity<>(testOrder);
        ResponseEntity<OrderForClientWithStatusDto> response = restTemplate.exchange(
                "/api/order",
                HttpMethod.POST,
                request,
                OrderForClientWithStatusDto.class
        );
    }

    @Test
    @Order(7)
    void testAuthManager() {
        HttpEntity<CredentialDto> credential = new HttpEntity<>(new CredentialDto("aaaa@gmail.com", "1234"));
        ResponseEntity<JwtResponce> response = restTemplate.exchange(
                "/api/employee/login",
                HttpMethod.POST,
                credential,
                JwtResponce.class
        );
        managerToken = Objects.requireNonNull(response.getBody()).token();
        log.info("managerToken : {}", managerToken);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(8)
    void testAuthPacker() {
        HttpEntity<CredentialDto> credential = new HttpEntity<>(new CredentialDto("bbbb@gmail.com", "1234"));
        ResponseEntity<JwtResponce> response = restTemplate.exchange(
                "/api/employee/login",
                HttpMethod.POST,
                credential,
                JwtResponce.class
        );
        packerToken = Objects.requireNonNull(response.getBody()).token();
        log.info("packerToken : {}", packerToken);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(8)
    void testAuthCourier() {
        HttpEntity<CredentialDto> credential = new HttpEntity<>(new CredentialDto("cccc@gmail.com", "1234"));
        ResponseEntity<JwtResponce> response = restTemplate.exchange(
                "/api/employee/login",
                HttpMethod.POST,
                credential,
                JwtResponce.class
        );
        courierToken = Objects.requireNonNull(response.getBody()).token();
        log.info("CourierToken : {}", courierToken);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(9)
    void testOrderProcessing() {
        log.info("managerToken : {}", managerToken);
        log.info("packerToken : {}", packerToken);
        log.info("CourierToken : {}", courierToken);
        OrderActivityCreateDto activity = new OrderActivityCreateDto(1L, Status.CREATED, "a");
        HttpEntity<?> request = managerWithTokenRequest(activity);

        ResponseEntity<OrderActivityDto> response = restTemplate.exchange(
                "/api/employee/activities/add",
                HttpMethod.POST,
                request,
                OrderActivityDto.class
        );
        log.info("request : {}", response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        activity = new OrderActivityCreateDto(1L, Status.PENDING_PACKED, "a");
        request = managerWithTokenRequest(activity);
        response = restTemplate.exchange(
                "/api/employee/activities/add",
                HttpMethod.POST,
                request,
                OrderActivityDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        activity = new OrderActivityCreateDto(1L, Status.PACKED, "a");
        request = packerWithTokenRequest(activity);
        response = restTemplate.exchange(
                "/api/employee/activities/add",
                HttpMethod.POST,
                request,
                OrderActivityDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        activity = new OrderActivityCreateDto(1L, Status.PENDING_DELIVERY, "a");
        request = managerWithTokenRequest(activity);
        response = restTemplate.exchange(
                "/api/employee/activities/add",
                HttpMethod.POST,
                request,
                OrderActivityDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        activity = new OrderActivityCreateDto(1L, Status.DELIVERED, "a");
        request = courierWithTokenRequest(activity);
        response = restTemplate.exchange(
                "/api/employee/activities/add",
                HttpMethod.POST,
                request,
                OrderActivityDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        activity = new OrderActivityCreateDto(1L, Status.COMPLETED, "a");
        request = managerWithTokenRequest(activity);
        response = restTemplate.exchange(
                "/api/employee/activities/add",
                HttpMethod.POST,
                request,
                OrderActivityDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
}
