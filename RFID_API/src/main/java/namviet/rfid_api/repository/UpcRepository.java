package namviet.rfid_api.repository;

import namviet.rfid_api.entity.Upc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UpcRepository extends JpaRepository<Upc,Integer> {
    Optional<Upc> findByUpc(String upc);

    @Override
    Page<Upc> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM upc WHERE CONTAINS(upc, :searchText) ORDER BY upc_id DESC ", nativeQuery = true)
    Page<Upc> searchWithFTS(@Param("searchText") String searchText, Pageable pageable);
}
