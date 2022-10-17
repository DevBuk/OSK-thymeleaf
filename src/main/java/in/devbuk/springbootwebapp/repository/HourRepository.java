package in.devbuk.springbootwebapp.repository;

import in.devbuk.springbootwebapp.entity.Hour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HourRepository extends JpaRepository<Hour, Long> {
}
