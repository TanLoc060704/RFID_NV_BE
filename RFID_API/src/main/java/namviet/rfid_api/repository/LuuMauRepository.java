package namviet.rfid_api.repository;

import namviet.rfid_api.entity.LuuMau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LuuMauRepository extends JpaRepository<LuuMau, Integer> {
    @Override
    Page<LuuMau> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM luu_mau WHERE CONTAINS(code, :searchText) ORDER BY luu_mau_id DESC ", nativeQuery = true)
    Page<LuuMau> searchWithFTS(@Param("searchText") String searchText, Pageable pageable);
}
