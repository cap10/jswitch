package zw.co.jugaad.jswitch.feigndto;

public enum TransactionStatus {
    COMPLETE,
    PENDING,
    REVERSED,
    PENDING_REVERSAL;

    private TransactionStatus() {
    }
}
