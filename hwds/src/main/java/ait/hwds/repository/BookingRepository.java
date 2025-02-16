package ait.hwds.repository;


import ait.hwds.model.entity.Booking;
import ait.hwds.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUser(User authUser);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.article.id = :articleId " +
            "AND (b.entryDate <= :departureDate AND b.departureDate >= :entryDate)")
    boolean isBedBooked(@Param("articleId") Long articleId,
                        @Param("entryDate") LocalDate entryDate,
                        @Param("departureDate") LocalDate departureDate);

    boolean existsByIdAndDepartureDateAfter(Long articleId, LocalDate date);

    List<Booking> findBedByIdAndDepartureDateBefore(Long articleId, LocalDate date);

    List<Booking> findArticleByIdAndDepartureDateBefore(Long articleId, LocalDate now);
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM Booking b " +
            "WHERE b.article.id = :articleId " +
            "AND (b.entryDate <= :departureDate AND b.departureDate >= :entryDate)")
    boolean isArticleBooked(@Param("articleId") Long articleId,
                            @Param("entryDate") LocalDate entryDate,
                            @Param("departureDate") LocalDate departureDate);
}
