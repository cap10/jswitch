package zw.co.jugaad.jswitch.feigndto;


import lombok.Data;


@Data
public class TransactionResponse {
    private Long transactionId;
    private String originalRef;
    private TransactionStatus status;
    private TransactionTypes transactionTypes;
    private String source;
    private String destination;
    private String additionalData;

    public TransactionResponse(Long transactionId, String originalRef, TransactionStatus status, TransactionTypes transactionTypes, String source, String destination) {
        this.transactionId = transactionId;
        this.originalRef = originalRef;
        this.status = status;
        this.transactionTypes = transactionTypes;
        this.source = source;
        this.destination = destination;
    }

    public TransactionResponse(String originalRef, TransactionStatus status, TransactionTypes transactionTypes) {
        this.originalRef = originalRef;
        this.status = status;
        this.transactionTypes = transactionTypes;
    }

    public TransactionResponse(String originalRef, TransactionStatus status, TransactionTypes transactionTypes, String additionalData) {
        this.originalRef = originalRef;
        this.status = status;
        this.transactionTypes = transactionTypes;
        this.additionalData = additionalData;
    }
}
