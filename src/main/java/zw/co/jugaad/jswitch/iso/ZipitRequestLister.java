package zw.co.jugaad.jswitch.iso;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import zw.co.jugaad.jswitch.feigndto.SubscriberZipitReceiveDto;
import zw.co.jugaad.jswitch.feigndto.TransactionResponse;

import java.math.BigDecimal;
import java.net.URI;
import java.util.concurrent.TimeoutException;

@Slf4j
public class ZipitRequestLister implements ISORequestListener {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${zipit-receive.url}")
    private String url;


    @SneakyThrows
    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        Thread t = new Thread(new Processor(isoSource, isoMsg));
        t.start();
        return true;
    }


    class Processor implements Runnable {

        private ISOSource isoSource;
        private ISOMsg isoMsg;

        Processor(ISOSource isoSource, ISOMsg isoMsg) {
            this.isoSource = isoSource;
            this.isoMsg = isoMsg;
        }

        @SneakyThrows
        @Override
        public void run() {
            GenericPackager pkg = new GenericPackager("cfg/packager/postpack.xml");
            Logger l = new Logger();
            l.addListener(new SimpleLogListener());
            pkg.setLogger(l, "");
            isoMsg.setPackager(pkg);

            switch (isoMsg.getMTI()) {
                case "0200": {
                    log.info("############## Attempting to call Feign####################");
                    TransactionResponse transactionResponse;
                    try {
                        /*transactionResponse = restTemplateZipitReceive(
                                isoMsg.getString(103),//cashmet mobile
                                isoMsg.getString(4),//amount
                                isoMsg.getString(103),//account
                                isoMsg.getString(32),//bin
                                isoMsg.getString(37)//rrn
                        );

                        log.info("################### Response: {}", transactionResponse.toString());
                        if (transactionResponse.getStatus().name().equalsIgnoreCase("COMPLETE")) {
                            isoMsg = createZipitSuccessfulResponse();
                        } else {
                            log.info("######################### Exception occurred: {}", transactionResponse.toString());
                            throw new Exception(transactionResponse.getStatus().name());
                        }*/
                        isoMsg = createZipitSuccessfulResponse();
                    } catch (Exception exception) {
                        if (exception instanceof TimeoutException) {
                            isoMsg = createZipitFailureResponse(42);
                        }
                        if (exception.getMessage().equalsIgnoreCase("Bank not found")) {
                            isoMsg = createZipitFailureResponse(96);
                        } else {
                            isoMsg = createZipitFailureResponse(42);
                        }
                    }
                    isoSource.send(isoMsg);

                    break;
                }

                case "0420": {
                    isoMsg.setResponseMTI();
                    isoMsg.set(39, "00");
                    isoSource.send(isoMsg);
                }

                case "0800": {
                    isoMsg.setResponseMTI();
                    isoMsg.set(39, "00");
                    isoSource.send(isoMsg);
                }
                default:
                    throw new Exception("Invalid message");
            }
        }

        private ISOMsg createZipitSuccessfulResponse() throws ISOException {
            isoMsg.setResponseMTI();
            isoMsg.set(39, "00");
            isoMsg.set(28, "C00000000");
            isoMsg.unset("127.22");
            isoMsg.unset("127.3");
            isoMsg.unset("127.20");


            String field122 = getLengthOfReference("SrcCustFName", "VENON").concat(
                    getLengthOfReference("SrcCustLName",
                            "MAPFUNDE").concat(
                            getLengthOfReference("SrcMobileNo", "263775091262").concat(
                                    getLengthOfReference("SrcCustIdNo", "592001300Y75")
                            ))
            );
            isoMsg.set("127.22", field122);
            return isoMsg;
        }

        private ISOMsg createZipitFailureResponse(int errorCode) throws ISOException {
            isoMsg.setResponseMTI();
            isoMsg.set(39, String.valueOf(errorCode));
            return isoMsg;

        }

        /*private TransactionResponse performZipitReceive(String mobile, String amount, String sourceAccount, String bin, String rrn) throws Exception {
            SubscriberZipitReceiveDto subscriberZipitReceiveDto = SubscriberZipitReceiveDto.builder()
                    .subscriberMobile(mobile)
                    .amount(getAmountInDollars(amount))
                    .bankAccount(sourceAccount)
                    .bin(bin)
                    .rrn(rrn)
                    .build();
            try {
                ResponseEntity<TransactionResponse> transactionResponse = zipitFeignClient.zipitReceive(subscriberZipitReceiveDto);
                return transactionResponse.getBody();
            } catch (Exception exception) {
                if (exception instanceof FeignException) {
                    FeignException feignException = (FeignException) exception;
                    var message = objectMapper.readValue(feignException.contentUTF8(), ResponseMessage.class);
                    log.info("######################{}", feignException.contentUTF8());
                    log.info("######################### Exception occurred: {}", message.getMessage());
                }
                exception.printStackTrace();
                throw new RuntimeException("Transaction failed");
            }
        }*/

        private BigDecimal getAmountInDollars(String amount) {
            log.info("##################### formating amount########################3");
            BigDecimal formattedAmount = new BigDecimal(amount);
            log.info("##################### Formatted amount########################3");
            return formattedAmount.divide(new BigDecimal(100));
        }


        private TransactionResponse restTemplateZipitReceive(String mobile, String amount, String sourceAccount, String bin, String rrn) throws Exception {
            SubscriberZipitReceiveDto subscriberZipitReceiveDto = SubscriberZipitReceiveDto.builder()
                    .subscriberMobile(mobile)
                    .amount(getAmountInDollars(amount))
                    .bankAccount(sourceAccount)
                    .bin(bin)
                    .rrn(rrn)
                    .build();
            RestTemplate restTemplate = new RestTemplate();
            final String baseUrl = url;
            URI uri = new URI(baseUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", "$apr1$0xpiuy83$80wyJVeTrN/UhcZuPA7pX.");
            HttpEntity<SubscriberZipitReceiveDto> request = new HttpEntity<>(subscriberZipitReceiveDto, headers);
            try {
                ResponseEntity<TransactionResponse> result = restTemplate.postForEntity(uri, request, TransactionResponse.class);
                log.info("####################################### Result {}", result.getBody().toString());
                //publish notification to RabbitMQ.
                return result.getBody();
            } catch (Exception exception) {
                log.info("######################### Exception occurred: {}", exception.getMessage());
                throw new Exception(exception.getMessage());
            }
        }

        private String getLengthOfReference(String tag, String value) {

            int length = tag.length();
            int lengthOfLength = String.valueOf(length).length();
            String tagged = String.valueOf(lengthOfLength).concat(String.valueOf(length)).concat(tag);
            int lengthOfValue = value.length();
            int lengthOfLengthValue = String.valueOf(lengthOfValue).length();
            String valued = String.valueOf(lengthOfLengthValue).concat(String.valueOf(lengthOfValue)).concat(value);
            return tagged.concat(valued);
        }

    }
}




