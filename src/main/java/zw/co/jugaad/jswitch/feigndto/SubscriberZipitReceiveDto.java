package zw.co.jugaad.jswitch.feigndto;


import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
public class SubscriberZipitReceiveDto {

    private String subscriberMobile;

    private String bin;

    private String bankAccount;

    private BigDecimal amount;

    private Channel channel;

    private String rrn;

}
