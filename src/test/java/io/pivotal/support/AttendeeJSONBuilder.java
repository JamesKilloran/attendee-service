package io.pivotal.support;

public class AttendeeJSONBuilder {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private String emailAddress;

    public static AttendeeJSONBuilder attendeeJSONBuilder() {
        return new AttendeeJSONBuilder();
    }

    public AttendeeJSONBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AttendeeJSONBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AttendeeJSONBuilder address(String address) {
        this.address = address;
        return this;
    }

    public AttendeeJSONBuilder city(String city) {
        this.city = city;
        return this;
    }

    public AttendeeJSONBuilder state(String state) {
        this.state = state;
        return this;
    }

    public AttendeeJSONBuilder zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public AttendeeJSONBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public AttendeeJSONBuilder emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public String build(
    ) {
        return "{" +
                "\"firstName\": \"" + firstName + "\", " +
                "\"lastName\": \"" + lastName + "\", " +
                "\"address\": \"" + address + "\", " +
                "\"city\": \"" + city + "\", " +
                "\"state\": \"" + state + "\", " +
                "\"zipCode\": \"" + zipCode + "\", " +
                "\"phoneNumber\": \"" + phoneNumber + "\", " +
                "\"emailAddress\": \"" + emailAddress + "\" " +
                "}";
    }
}
