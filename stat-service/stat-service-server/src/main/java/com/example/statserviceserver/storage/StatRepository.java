package com.example.statserviceserver.storage;

import com.example.statservicedto.StatDtoGet;
import com.example.statserviceserver.model.StatModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<StatModel, Long> {
    @Query("select new com.example.statservicedto.StatDtoGet(sm.app, sm.uri, count (sm.id)) " +
            "from StatModel as sm  " +
            "where sm.timestamp BETWEEN ?1 AND ?2 " +
            "group by sm.app, sm.uri " +
            "order by count(sm.id) desc")
    List<StatDtoGet> findByDate(LocalDateTime start, LocalDateTime end);

    @Query("select new com.example.statservicedto.StatDtoGet(sm.app, sm.uri, count (sm.id)) " +
            "from StatModel as sm  " +
            "where sm.uri in ?3 AND sm.timestamp BETWEEN ?1 AND ?2 " +
            "group by sm.app, sm.uri " +
            "order by count(sm.id) desc")
    List<StatDtoGet> findByDateAndUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new com.example.statservicedto.StatDtoGet(sm.app, sm.uri, count(distinct sm.ip))" +
            "from StatModel as sm  where sm.id in  (select s.id from StatModel as s group by s.id, s.ip) " +
            "And sm.uri  in ?3 AND sm.timestamp BETWEEN ?1 AND ?2 " +
            "group by sm.app, sm.uri " +
            "order by count(sm.id) desc")
    List<StatDtoGet> findByUniqAndDateAndUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new com.example.statservicedto.StatDtoGet(sm.app, sm.uri, count(distinct sm.ip))" +
            "from StatModel as sm  where sm.id in " +
            "(select s.id from StatModel as s group by s.id, s.ip) " +
            "and sm.timestamp BETWEEN ?1 AND ?2 " +
            "group by sm.app, sm.uri " +
            "order by count(sm.id) desc")
    List<StatDtoGet> findByUniqAndDate(LocalDateTime start, LocalDateTime end);


}
