package namviet.rfid_api.repository;

import namviet.rfid_api.entity.ChipData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChipDataRepository extends JpaRepository<ChipData, Integer> {
    List<ChipData> findByTidHeaderContaining(String chipId);
}
