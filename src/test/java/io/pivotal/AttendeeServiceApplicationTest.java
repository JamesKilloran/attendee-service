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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
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

    assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
  }

  @Test
  public void serviceReturnsCollectionOfAttendees() throws Exception {
    String attendeeListJSON = restTemplate.getForObject("/attendees", String.class);
    DocumentContext parsedResponse = JsonPath.parse(attendeeListJSON);

    List<Object> attendees = parsedResponse.read("$._embedded.attendees");
    assertThat(attendees.size(), equalTo(1));

    String firstName = parsedResponse.read("$._embedded.attendees[0].firstName");
    String lastName = parsedResponse.read("$._embedded.attendees[0].lastName");
    String address = parsedResponse.read("$._embedded.attendees[0].address");
    String city = parsedResponse.read("$._embedded.attendees[0].city");
    String state = parsedResponse.read("$._embedded.attendees[0].state");
    String zipCode = parsedResponse.read("$._embedded.attendees[0].zipCode");
    String phoneNumber = parsedResponse.read("$._embedded.attendees[0].phoneNumber");
    String emailAddress = parsedResponse.read("$._embedded.attendees[0].emailAddress");

    assertThat(firstName, equalTo("Bob"));
    assertThat(lastName, equalTo("Builder"));
    assertThat(address, equalTo("1234 Fake St"));
    assertThat(city, equalTo("Detroit"));
    assertThat(state, equalTo("Michigan"));
    assertThat(zipCode, equalTo("80202"));
    assertThat(phoneNumber, equalTo("555-7890"));
    assertThat(emailAddress, equalTo("bob@example.com"));
  }

}

