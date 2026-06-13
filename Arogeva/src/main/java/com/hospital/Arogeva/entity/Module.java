package com.hospital.Arogeva.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "modules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_id")
    private Integer moduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "module_name", length = 200)
    private String moduleName;

    @Column(name = "scope_description", columnDefinition = "TEXT")
    private String scopeDescription;

    @Column(name = "planned_man_days", precision = 10, scale = 2)
    private BigDecimal plannedManDays;

    @Column(name = "planned_cost", precision = 15, scale = 2)
    private BigDecimal plannedCost;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
