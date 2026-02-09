package ru.itmo.web.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "HIT_RESULTS")  // ВЕРХНИЙ РЕГИСТР
public class HitResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x", precision = 10, scale = 2, nullable = false)
    private BigDecimal x;

    @Column(name = "y", precision = 10, scale = 2, nullable = false)
    private BigDecimal y;

    @Column(name = "r", length = 10, nullable = false)
    private String r;

    @Column(name = "hit", nullable = false)
    private Boolean hit;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public HitResult() {
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    public HitResult(BigDecimal x, BigDecimal y, String r, Boolean hit) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getX() { return x; }
    public void setX(BigDecimal x) { this.x = x; }

    public BigDecimal getY() { return y; }
    public void setY(BigDecimal y) { this.y = y; }

    public String getR() { return r; }
    public void setR(String r) { this.r = r; }

    public Boolean isHit() { return hit; }
    public void setHit(Boolean hit) { this.hit = hit; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}