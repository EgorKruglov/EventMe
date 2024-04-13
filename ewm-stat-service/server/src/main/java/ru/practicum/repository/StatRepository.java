package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.HitResponseDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.HitResponseDto(hit.ip, hit.uri, count(distinct hit.ip)) " +
            "from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "group by hit.ip, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<HitResponseDto> findAllByTimestampBetweenStartAndEndWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.HitResponseDto(hit.ip, hit.uri, count(hit.ip)) " +
            "from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "group by hit.ip, hit.uri " +
            "order by count(hit.ip) desc ")
    List<HitResponseDto> findAllByTimestampBetweenStartAndEndWhereIpNotUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.HitResponseDto(hit.ip, hit.uri, count(distinct hit.ip)) " +
            "from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 and hit.uri in ?3 " +
            "group by hit.ip, hit.uri " +
            "order by count(distinct hit.ip) desc ")
    List<HitResponseDto> findAllByTimestampBetweenStartAndEndWithUrisUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.HitResponseDto(hit.ip, hit.uri, count(hit.ip)) " +
            "from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 and hit.uri in ?3 " +
            "group by hit.ip, hit.uri " +
            "order by count(hit.ip) desc ")
    List<HitResponseDto> findAllByTimestampBetweenStartAndEndWithUrisIpNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
