package io.pivotal;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static io.pivotal.support.AttendeeJSONBuilder.attendeeJSONBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class AttendeeServiceApplicationTest {

  private RestTemplate restTemplate = new RestTemplate();

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Value("${local.server.port}")
  private int port;

  private String url;

  @Before
  public void setup() {
    jdbcTemplate.execute("create table if not exists attendees (" +
        "id bigint " +
        ", first_name varchar(255)" +
        ", last_name varchar(255)" +
        ", address varchar(255)" +
        ", city varchar(255)" +
        ", state varchar(255)" +
        ", zip_code varchar(255)" +
        ", phone_number varchar(255)" +
        ", email_address varchar(255)" +
        ");");

    url = "http://localhost:" + port + "/attendees";

    String attendeeJSON = attendeeJSONBuilder()
        .firstName("Bob")
        .lastName("Builder")
        .address("1234 Fake St")
        .city("Detroit")
        .state("Michigan")
        .zipCode("80202")
        .phoneNumber("555-7890")
        .emailAddress("bob@example.com")
        .build();

    ResponseEntity<String> responseEntity = postJSON(attendeeJSON, url);
    if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
      throw new RuntimeException("Unable to create attendee");
    }
  }

  @Test
  public void serviceReturnsCollectionOfAttendees() throws Exception {
    String attendeeListJSON = restTemplate.getForObject(url, String.class);
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

  private ResponseEntity<String> postJSON(String attendeeJSON, String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> attendeeHttpEntity = new HttpEntity<>(attendeeJSON, headers);
    return restTemplate.postForEntity(url, attendeeHttpEntity, String.class);
  }
}

