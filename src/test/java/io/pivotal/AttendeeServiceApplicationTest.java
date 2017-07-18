package io.pivotal;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.pivotal.enablement.attendee.model.Attendee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AttendeeServiceApplicationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Before
  public void setup() {

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
    String attendeeListJSON = restTemplate.getForObject("/attendees", String.class);
    DocumentContext response = JsonPath.parse(attendeeListJSON);
    List<Object> attendees = response.read("$._embedded.attendees");
    assertThat(attendees.size()).isEqualTo(1);

    assertThat(response.<String>read("$._embedded.attendees[0].firstName")).isEqualTo("Bob");
    assertThat(response.<String>read("$._embedded.attendees[0].lastName")).isEqualTo("Builder");
    assertThat(response.<String>read("$._embedded.attendees[0].address")).isEqualTo("1234 Fake St");
    assertThat(response.<String>read("$._embedded.attendees[0].city")).isEqualTo("Detroit");
    assertThat(response.<String>read("$._embedded.attendees[0].state")).isEqualTo("Michigan");
    assertThat(response.<String>read("$._embedded.attendees[0].zipCode")).isEqualTo("80202");
    assertThat(response.<String>read("$._embedded.attendees[0].phoneNumber")).isEqualTo("555-7890");
    assertThat(response.<String>read("$._embedded.attendees[0].emailAddress")).isEqualTo("bob@example.com");
  }

}

