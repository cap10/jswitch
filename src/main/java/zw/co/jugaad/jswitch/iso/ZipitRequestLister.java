package zw.co.jugaad.jswitch.iso;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import zw.co.jugaad.jswitch.feignclient.ZipitFeignClient;
import zw.co.jugaad.jswitch.feigndto.SubscriberZipitReceiveDto;
import zw.co.jugaad.jswitch.feigndto.TransactionResponse;

import java.math.BigDecimal;

@Slf4j
public class ZipitRequestLister implements ISORequestListener {

    @Autowired
    private ZipitFeignClient zipitFeignClient;


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
                    /*isoMsg.setResponseMTI();
                    isoMsg.set(39, "00");
                    isoSource.send(isoMsg);*/

                    TransactionResponse transactionResponse;
                    try {
                        transactionResponse = performZipitReceive(
                                isoMsg.getString(103),//cashmet mobile
                                isoMsg.getString(4),//amount
                                isoMsg.getString(103),//account
                                isoMsg.getString(100),//bin
                                isoMsg.getString(37)//rrn
                        );
                        log.info("################### Response: {}", transactionResponse.toString());
                        if (transactionResponse.getStatus().name().equalsIgnoreCase("COMPLETE")) {
                            isoMsg = createZipitSuccessfulResponse();
                        } else {
                            throw new Exception(transactionResponse.getStatus().name());
                        }
                    } catch (Exception exception) {
                        isoMsg = createZipitFailureResponse();
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
            return isoMsg;
        }

        private ISOMsg createZipitFailureResponse() throws ISOException {
            isoMsg.setResponseMTI();
            isoMsg.set(39, "42");
            return isoMsg;

        }

        private TransactionResponse performZipitReceive(String mobile, String amount, String sourceAccount, String bin, String rrn) {
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
                throw new RuntimeException("Transaction failed");
            }
        }

        private BigDecimal getAmountInDollars(String amount) {
            BigDecimal formattedAmount = new BigDecimal(amount);
            return formattedAmount.divide(new BigDecimal(100));
        }

    }
}




