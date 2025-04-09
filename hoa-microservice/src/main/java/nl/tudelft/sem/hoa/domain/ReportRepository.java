package nl.tudelft.sem.hoa.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findReportByIdReporter(String idReporter);

    List<Report> findReportByIdAccused(String idAccused);

    List<Report> findReportByHoaId(String hoaId);
}
