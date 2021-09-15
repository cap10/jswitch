package zw.co.jugaad.jswitch.feigndto;


import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
public class SubscriberZipitReceiveDto {
    private String subscriberMobile;

    private String bank;

    private String bankCode;

    private String bankAccount;

    private BigDecimal amount;

    private String pin;

    private Channel channel;
}
