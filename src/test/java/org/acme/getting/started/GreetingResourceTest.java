package org.acme.getting.started;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/verruckt-nietzsche/morale")
                .then()
                .statusCode(200)
                .body(is("{\"situation\":null,\"goodEvil\":{\"message\":\"Empty\",\"color\":\"#4D5656\"},\"requestId\":null}"));
    }

}