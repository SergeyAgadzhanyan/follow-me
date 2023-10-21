package com.example.mainservice.storage;

import com.example.mainservice.enums.RequestStatus;
import com.example.mainservice.model.EventRequestShort;
import com.example.mainservice.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findByEventId(Long eventId);

    List<ParticipationRequest> findByRequesterId(Long userId);

    List<ParticipationRequest> findByEventIdAndEventInitiatorId(Long eventId, Long userId);

    List<ParticipationRequest> findByEventIdAndStatusIn(Long eventId, List<RequestStatus> statuses);

    @Query("select new com.example.mainservice.model.EventRequestShort(p.event.id,count(p.id)) " +
            "from ParticipationRequest as p where p.event.id in :eventIds and p.status = :status " +
            "group by p.event.id")
    List<EventRequestShort> findShortByIdsAndStatus(List<Long> eventIds, RequestStatus status);

    List<ParticipationRequest> findByEventIdInAndStatus(List<Long> eventIds, RequestStatus status);

    List<ParticipationRequest> findByEventIdAndRequesterId(Long eventId, Long userId);


}
