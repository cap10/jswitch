package zw.co.jugaad.jswitch.feigndto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {
    private String statusCode;
    private String message;
    private String timestamp;
    private String status;
    private int errorCode;
}
