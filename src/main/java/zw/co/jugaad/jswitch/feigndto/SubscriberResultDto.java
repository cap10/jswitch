package zw.co.jugaad.jswitch.feigndto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class SubscriberResultDto implements Serializable {

    private Long id;

    private String mobile;

    private String email;

    private String firstname;

    private String lastname;

    private String idNumber;

    private LocalDate registrationDate;

    private String subscriberProfile;

    private String agentName;

    private LocalDate dateOfBirth;
    private String gender;

    private String subscriberPhoto;
    private String idPhoto;
    private String status;

    private String reason;

    private String street;

    private String suburb;

    private String city;
}
