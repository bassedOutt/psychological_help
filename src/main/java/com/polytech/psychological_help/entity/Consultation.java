package com.polytech.psychological_help.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "consultation")
@Entity
@Data
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private User consultant;

    @Column(name = "pib")
    private String pib;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;
}
