package in.devbuk.springbootwebapp.repository;

import in.devbuk.springbootwebapp.entity.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
}
