package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.status.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByIdAndInitiatorId(long eventId, long userId);

    List<Event> findAllByInitiatorId(long userId, Pageable pageable);

    boolean existsByIdAndInitiatorId(long eventId, long userId);

    boolean existsByCategoryId(long catId);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.initiator.id IN ?1 OR ?1 IS null) " +
            "AND (e.state IN ?2 OR ?2 IS null) " +
            "AND (e.category.id IN ?3 OR ?3 IS null) " +
            "AND (cast(?4 as timestamp) is null OR e.eventDate > ?4) AND (cast(?5 as timestamp) is null OR e.eventDate < ?5) ")
    List<Event> findAllByParam(List<Long> users, List<State> states, List<Long> categories,
                               LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((?1 IS null) OR ((lower(e.annotation) LIKE concat('%', lower(?1), '%')) OR (lower(e.description) LIKE concat('%', lower(?1), '%')))) " +
            "AND (e.category.id IN ?2 OR ?2 IS null) " +
            "AND (e.paid = ?3 OR ?3 IS null) " +
            "AND (cast(?4 as timestamp) is null OR e.eventDate > ?4) AND (cast(?5 as timestamp) is null OR e.eventDate < ?5) " +
            "AND (?6 = false OR ((?6 = true AND e.participantLimit > (SELECT count(*) FROM Request AS r WHERE e.id = r.event.id))) " +
            "OR (e.participantLimit > 0 )) ")
    List<Event> findAllEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Pageable pageable);
}
