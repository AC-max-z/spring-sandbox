package com.maxzamota.spring_sandbox.util.helpers;

import com.maxzamota.spring_sandbox.dto.CustomerDto;
import com.maxzamota.spring_sandbox.dto.UserDto;
import com.maxzamota.spring_sandbox.mappers.UserMapper;
import com.maxzamota.spring_sandbox.model.CustomerEntity;
import com.maxzamota.spring_sandbox.util.generators.UserGenerator;
import io.netty.handler.logging.LogLevel;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.util.*;
import java.util.stream.Collectors;

public class IntegrationTestHelpers {
    private static final String APP_BASE_URI = "http://localhost";
    private static final String CUSTOMER_PUBLIC_API_URI = "/api/v1/customer";
    private static final String USER_PUBLIC_API_URI = "/api/v1/user";
    private static final ThreadLocal<String> AUTH_HEADER = new ThreadLocal<>();
    private static final UserMapper MAPPER = new UserMapper(new ModelMapper());

    public static WebTestClient getWebTestClient(Integer port) {
        var webClient = WebTestClient
                .bindToServer()
                .baseUrl(Objects.nonNull(port) ? APP_BASE_URI + ":" + port : APP_BASE_URI)
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient
                                        .create()
                                        // This will enable info level logging to underlying HttpClient
                                        // to see requests and responses data
                                        .wiretap(
                                                "HTTP_CLIENT",
                                                LogLevel.INFO,
                                                AdvancedByteBufFormat.TEXTUAL
                                        )
                        )
                )
                .defaultHeader(HttpHeaders.USER_AGENT, "Spring Web Client")
                .build();
        var generatedUser = new UserGenerator().generate();
        postUser(webClient, MAPPER.toDto(generatedUser));
        AUTH_HEADER.set("Basic " + Base64.getEncoder()
                .encodeToString((generatedUser.getEmail() + ":" + generatedUser.getPassword())
                        .getBytes()));
        return webClient;
    }

    @Step("Create new User")
    public static void postUser(@NotNull WebTestClient webClient, UserDto userDto) {
        webClient
                .post()
                .uri(USER_PUBLIC_API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-request-id", String.valueOf(UUID.randomUUID()))
                .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER.get())
                .body(Mono.just(userDto), UserDto.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(new ParameterizedTypeReference<EntityModel<UserDto>>() {
                });
    }

    @Step("Create new Customer")
    public static CustomerDto postCustomer(@NotNull WebTestClient webClient, CustomerDto customerDto) {
        return Objects.requireNonNull(webClient
                        .post()
                        .uri(CUSTOMER_PUBLIC_API_URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-request-id", String.valueOf(UUID.randomUUID()))
                        .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER.get())
                        .body(Mono.just(customerDto), CustomerDto.class)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<EntityModel<CustomerDto>>() {
                        })
                        .returnResult()
                        .getResponseBody())
                .getContent();
    }

    @Step("Get all customers")
    public static List<CustomerEntity> getAllCustomers(@NotNull WebTestClient webClient) {
        return Objects.requireNonNull(webClient
                        .get()
                        .uri(CUSTOMER_PUBLIC_API_URI + "/all")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("x-request-id", String.valueOf(UUID.randomUUID()))
                        .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER.get())
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(new ParameterizedTypeReference<PagedModel<EntityModel<CustomerEntity>>>() {
                        })
                        .returnResult()
                        .getResponseBody())
                .getContent()
                .stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());
    }

    @Step("Get customer by id")
    public static CustomerDto getCustomerById(WebTestClient webClient, Integer id) {
        return Objects.requireNonNull(webClient
                        .get()
                        .uri(CUSTOMER_PUBLIC_API_URI + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("x-request-id", String.valueOf(UUID.randomUUID()))
                        .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER.get())
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(new ParameterizedTypeReference<EntityModel<CustomerDto>>() {
                        })
                        .returnResult()
                        .getResponseBody())
                .getContent();
    }

    @Step("Delete customer by id")
    public static void deleteCustomerById(WebTestClient webClient, Integer id) {
        webClient
                .delete()
                .uri(CUSTOMER_PUBLIC_API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-request-id", String.valueOf(UUID.randomUUID()))
                .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER.get())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Step("Get customer by id not found")
    public static Object getCustomerByIdNotFound(WebTestClient webClient, Integer id) {
        return webClient
                .get()
                .uri(CUSTOMER_PUBLIC_API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-request-id", String.valueOf(UUID.randomUUID()))
                .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER.get())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(Object.class)
                .returnResult()
                .getResponseBody();
    }

    @Step("Update customer")
    public static CustomerDto putCustomer(WebTestClient webClient, CustomerDto customerDto) {
        return Objects.requireNonNull(webClient
                        .put()
                        .uri(CUSTOMER_PUBLIC_API_URI + "/{id}", customerDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-request-id", String.valueOf(UUID.randomUUID()))
                        .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER.get())
                        .body(Mono.just(customerDto), CustomerEntity.class)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(new ParameterizedTypeReference<EntityModel<CustomerDto>>() {
                        })
                        .returnResult()
                        .getResponseBody())
                .getContent();
    }
}
