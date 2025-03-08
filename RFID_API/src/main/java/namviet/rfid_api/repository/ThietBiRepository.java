package namviet.rfid_api.repository;

import namviet.rfid_api.entity.ThietBi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThietBiRepository extends JpaRepository<ThietBi,Integer> {

    @Override
    Page<ThietBi> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM thiet_bi WHERE CONTAINS(ten_may, :searchText) ORDER BY thiet_bi_id DESC ", nativeQuery = true)
    Page<ThietBi> searchWithFTSService(@Param("searchText") String searchText, Pageable pageable);

}
