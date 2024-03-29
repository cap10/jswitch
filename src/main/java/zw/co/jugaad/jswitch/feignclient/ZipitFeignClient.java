package zw.co.jugaad.jswitch.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import zw.co.jugaad.jswitch.feigndto.SubscriberZipitReceiveDto;
import zw.co.jugaad.jswitch.feigndto.TransactionResponse;


@FeignClient(name = "akupay-api-gateway",url = "https://api-metbank.jugaad.co.zw")
public interface ZipitFeignClient {

    @PostMapping("akupay-transaction-service/api/v1/transactions/zipit-receive")
    ResponseEntity<TransactionResponse> zipitReceive(@RequestBody SubscriberZipitReceiveDto subscriberZipitReceiveDto) throws Exception;
}
