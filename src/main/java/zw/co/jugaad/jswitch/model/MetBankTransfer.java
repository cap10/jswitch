package zw.co.jugaad.jswitch.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetBankTransfer extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mti;

    private String processingCode;

    private String amount;

    private String transactionDate;

    private String stan;

    private String timeLocalTransaction;

    private String dateLocalTransaction;

    private String posEntryMode;

    private String posConditionCode;

    @NaturalId
    private String rrn;

    private String cardAcceptorTid;

    private String cardAcceptorIdCode;

    private String cardAcceptorLocation;

    private String currencyCode;

    private String recievingInstitution;

    private String accountDebit;

    private String accountCredit;

    private String responseCode;

    private String status;

    private String mobile;

    private String receivingAccountNameRtgs;

    private String receivingAccountNumberRtgs;

    private String receivingBankSwiftCodeRtgs;

    private Boolean reversed = Boolean.FALSE;

    private BigDecimal absoluteAmount = BigDecimal.ZERO;
}
