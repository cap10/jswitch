package zw.co.jugaad.jswitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import zw.co.jugaad.jswitch.field127.Field127;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.TimeZone;

@SpringBootApplication
@Slf4j
public class JswitchApplication implements CommandLineRunner {

    private ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(JswitchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        /*String hex = "30323030B23A04D588E0812000000000" + "12000022353130303030303030303030"
                + "30303939393930393136323134333236" + "31313732323930393338333330393037"
                + "30393136303030303030384330303030" + "30303030433030303030303030303635"
                + "36313438313036313233343536303030" + "30303030333236313933303035303030"
                + "315A696D73776974636820205472616E" + "4642432045636F6E6574204D6F62696C"
                + "65204D6F6F6C61486172617265202020" + "20202020203731363933323030343135"
                + "31303031303030303030333236313930" + "36353631343831313232363337373236"
                + "39393535353031353130303435303230" + "30313330313231303030353830600014"
                + "00000000003130303030303033323631" + "395A73775372632020202020204D6574"
                + "43617368536E6B202031313732323931" + "31373232392020202020202020202020"
                + "20323032313039313630303439393231" + "38506F7374696C696F6E3A4D65746144"
                + "6174613232303231344164646974696F" + "6E616C44617461313131323134416464"
                + "6974696F6E616C44617461333433343C" + "3F786D6C2076657273696F6E3D22312E"
                + "302220656E636F64696E673D22555446" + "2D3822207374616E64616C6F6E653D22"
                + "796573223F3E3C6669656C643132373E" + "202020203C457874656E646564547261"
                + "6E73616374696F6E547970653E393030" + "303C2F457874656E6465645472616E73"
                + "616374696F6E547970653E202020203C" + "53656E64657244657461696C733E2020"
                + "2020202020203C42616E6B3E46424342" + "616E6B3C2F42616E6B3E202020202020"
                + "20203C4E6174696F6E616C49643E3633" + "323135313230374232383C2F4E617469"
                + "6F6E616C49643E20202020202020203C" + "4D6F62696C654E756D6265723E323633"
                + "3737323936323130303C2F4D6F62696C" + "654E756D6265723E2020202020202020"
                + "3C5265666572656E63653E546573743C" + "2F5265666572656E63653E2020202020"
                + "2020203C456D61696C41646472657373" + "3E6C6F76656A6F796D6170686F736140"
                + "7961686F6F2E636F6D3C2F456D61696C" + "416464726573733E202020203C2F5365"
                + "6E64657244657461696C733E20202020" + "3C5472616E507572706F7365436F6465"
                + "3E4F54523C2F5472616E507572706F73" + "65436F64653E3C2F6669656C64313237" + "3E";
        ISOMsg m = new ISOMsg();
        try {
            GenericPackager pkg = new GenericPackager("cfg/packager/postpack.xml");
            Logger l = new Logger();
            l.addListener(new SimpleLogListener());

            pkg.setLogger(l, "");
            m.setPackager(pkg);
            //convert the hex to bytes and feed it to unpack
            byte[] arr = ISOUtil.hex2byte(hex);
            m.unpack(arr);
            m.dump(System.out, "");

            String field12722 = m.getString("127.22");

            log.info("################### {}", field12722);

            String array[] = field12722.split("<field127>");

            JAXBContext jaxbContext;
            try
            {
                jaxbContext = JAXBContext.newInstance(Field127.class);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                Field127 field127 = (Field127) jaxbUnmarshaller.unmarshal(new StringReader("<field127>".concat(array[1])));

                System.out.println(field127.getSenderDetails().getMobileNumber());
            }
            catch (JAXBException e)
            {
                e.printStackTrace();
            }

        } catch (Exception e) {
            m.dump(System.out, "x");
            e.printStackTrace();

        }*/

    }

    @Bean
    AuthInterceptor authFeign() {
        return new AuthInterceptor();
    }



    class AuthInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {
            template.header("x-api-key", "$apr1$0xpiuy83$80wyJVeTrN/UhcZuPA7pX.");
        }

    }

    @PostConstruct
    public void init(){
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("CAT"));
    }
}

