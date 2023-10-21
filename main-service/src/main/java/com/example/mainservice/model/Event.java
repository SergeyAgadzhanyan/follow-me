package com.example.mainservice.model;


import com.example.mainservice.enums.StatEnum;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "events")

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User initiator;
    @Embedded
    private Location location;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "state_enum")
    private StatEnum state;
    private String title;
    @Formula("(select count(c.id) from comment as c where c.event_id = id)")
    private Long comments;
}
