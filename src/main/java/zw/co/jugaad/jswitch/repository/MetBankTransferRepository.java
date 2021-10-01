package zw.co.jugaad.jswitch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zw.co.jugaad.jswitch.model.MetBankTransfer;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MetBankTransferRepository extends JpaRepository<MetBankTransfer, Long> {

    @Query(value = "update MetBankTransfer set responseCode =?2 and status ='COMPLETE' where rrn =?1", nativeQuery = true)
    void updateTransaction(String rrn, String responseCode);

    @Query(value = "select * from  MetBankTransfer where responseCode is null  and status ='TIMEOUT' ", nativeQuery = true)
    List<MetBankTransfer> findAllByMtiAndStatusAndResponseCode();

    Optional<MetBankTransfer> findByRrn(String rrn);

    @Query(value = "select rrn from MetBankTransfer\n" +
            "where id = (select max(id) from MetBankTransfer)", nativeQuery = true)
    Optional<String> getLastId();

    Page<MetBankTransfer> findAllByCreatedDateIsBetween(Instant startDate, Instant endDate, Pageable of);
}
