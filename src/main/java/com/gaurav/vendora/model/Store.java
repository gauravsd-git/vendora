package com.gaurav.vendora.model;

import com.gaurav.vendora.domain.StoreStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @ManyToOne
    @JoinColumn(name = "store_admin_id", nullable = false)
    private User storeAdmin;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String description;
    private String storeType;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private StoreContact contact;

    @PostPersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.status = StoreStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}