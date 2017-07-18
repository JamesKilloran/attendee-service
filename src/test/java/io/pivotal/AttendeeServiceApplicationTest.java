package io.pivotal;

import io.pivotal.enablement.attendee.model.Attendee;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AttendeeServiceApplicationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @Before
  public void setup() {

    RestAssured.port = port;

    Attendee attendee = Attendee.builder()
        .firstName("Bob")
        .lastName("Builder")
        .address("1234 Fake St")
        .city("Detroit")
        .state("Michigan")
        .zipCode("80202")
        .phoneNumber("555-7890")
        .emailAddress("bob@example.com")
        .build();

    ResponseEntity<String> responseEntity = restTemplate.postForEntity("/attendees", attendee, null);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  public void serviceReturnsCollectionOfAttendees() throws Exception {
    when()
        .get("/attendees")
      .then()
        .statusCode(200)
        .body("_embedded.attendees", hasSize(1))
        .body("_embedded.attendees[0].firstName", equalTo("Bob"))
        .body("_embedded.attendees[0].lastName", equalTo("Builder"))
        .body("_embedded.attendees[0].address", equalTo("1234 Fake St"))
        .body("_embedded.attendees[0].city", equalTo("Detroit"))
        .body("_embedded.attendees[0].state", equalTo("Michigan"))
        .body("_embedded.attendees[0].zipCode", equalTo("80202"))
        .body("_embedded.attendees[0].phoneNumber", equalTo("555-7890"))
        .body("_embedded.attendees[0].emailAddress", equalTo("bob@example.com"));
  }

}

