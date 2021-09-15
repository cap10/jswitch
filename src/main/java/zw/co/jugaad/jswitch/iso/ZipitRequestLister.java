package zw.co.jugaad.jswitch.iso;

import lombok.SneakyThrows;
import org.jpos.iso.*;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import zw.co.jugaad.jswitch.feignclient.ZipitFeignClient;
import zw.co.jugaad.jswitch.feigndto.SubscriberZipitReceiveDto;
import zw.co.jugaad.jswitch.feigndto.TransactionResponse;

import java.io.IOException;

public class ZipitRequestLister implements ISORequestListener {

    private final ZipitFeignClient zipitFeignClient;

    public ZipitRequestLister(ZipitFeignClient zipitFeignClient) {
        this.zipitFeignClient = zipitFeignClient;
    }

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

            switch (isoMsg.getMTI()) {

                case "0200": {
                    try {
                        MUX mux = QMUX.getMUX("server_1_mux");
                        SubscriberZipitReceiveDto subscriberZipitReceiveDto = SubscriberZipitReceiveDto.builder()
                                .subscriberMobile(isoMsg.get)
                        TransactionResponse transactionResponse = zipitFeignClient.zipitReceive();
                        ISOMsg responseMsg;
                        if (isoMsg.getString(4).equalsIgnoreCase("000000009999"))
                            responseMsg = createZipitSuccessfulResponse();
                        else {
                            responseMsg = createZipitFailureResponse();
                        }
                        if (responseMsg != null) {
                            isoSource.send(responseMsg);
                        }
                    } catch (NameRegistrar.NotFoundException | ISOException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                default:
                    throw new Exception("Invalid message");
            }
        }

        private ISOMsg createZipitSuccessfulResponse() throws ISOException {

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setMTI("0210");
            isoMsg.set(3, "510000");
            isoMsg.set(4, "000000009999");
            isoMsg.set(7, "0915114041");
            isoMsg.set(11, "028657");
            isoMsg.set(12, "114041");
            isoMsg.set(13, "0915");
            isoMsg.set(15, "0915");
            isoMsg.set(22, "000");
            isoMsg.set(25, "00");
            isoMsg.set(26, "08");
            isoMsg.set(28, "C00000000");
            isoMsg.set(28, "C00000000");
            isoMsg.set(32, "516261");
            isoMsg.set(37, "091585787752");
            isoMsg.set(39, "00");
            isoMsg.set(41, "ZimboCash");
            isoMsg.set(42, "ZimSwitch Trans");
            isoMsg.set(43, "EcoDollar 716");
            isoMsg.set(49, "932");
            isoMsg.set(54, "0001932 C0002767731900002932C000276773190");
            isoMsg.set(56, "1510");
            isoMsg.set(59, "0010877187");
            isoMsg.set(103, "1090000361");
            isoMsg.set(123, "000000000000121");
            return isoMsg;
        }

        private ISOMsg createZipitFailureResponse() throws ISOException {

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setMTI("0210");
            isoMsg.set(3, "510000");
            isoMsg.set(4, "000000009999");
            isoMsg.set(7, "0915114041");
            isoMsg.set(11, "028657");
            isoMsg.set(12, "114041");
            isoMsg.set(13, "0915");
            isoMsg.set(15, "0915");
            isoMsg.set(22, "000");
            isoMsg.set(25, "00");
            isoMsg.set(26, "08");
            isoMsg.set(28, "C00000000");
            isoMsg.set(28, "C00000000");
            isoMsg.set(32, "516261");
            isoMsg.set(37, "091585787752");
            isoMsg.set(39, "01");
            isoMsg.set(41, "ZimboCash");
            isoMsg.set(42, "ZimSwitch Trans");
            isoMsg.set(43, "EcoDollar 716");
            isoMsg.set(49, "932");
            isoMsg.set(54, "0001932 C0002767731900002932C000276773190");
            isoMsg.set(56, "1510");
            isoMsg.set(59, "0010877187");
            isoMsg.set(103, "1090000361");
            isoMsg.set(123, "000000000000121");
            return isoMsg;
        }
    }
}
