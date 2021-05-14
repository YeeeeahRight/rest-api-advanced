package com.epam.esm.persistence.model.entity;

import com.epam.esm.persistence.audit.EntityAuditListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@EntityListeners(EntityAuditListener.class)
@Table(name = "orders")
public class Order extends AbstractEntity {

    @Column(name = "order_date", nullable = false, updatable = false)
    private ZonedDateTime orderDate;

    @Column(nullable = false, updatable = false)
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name="certificate_id", nullable = false)
    private GiftCertificate certificate;

    public Order() {
    }

    public Order(long id, ZonedDateTime orderDate, BigDecimal cost, User user,
                 GiftCertificate certificate) {
        setId(id);
        this.orderDate = orderDate;
        this.cost = cost;
        this.user = user;
        this.certificate = certificate;
    }

    @PrePersist
    public void onCreate() {
        orderDate = ZonedDateTime.now();
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GiftCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(GiftCertificate certificate) {
        this.certificate = certificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!Objects.equals(orderDate, order.orderDate)) return false;
        if (!Objects.equals(cost, order.cost)) return false;
        if (!Objects.equals(user, order.user)) return false;
        return Objects.equals(certificate, order.certificate);
    }

    @Override
    public int hashCode() {
        int result = orderDate != null ? orderDate.hashCode() : 0;
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (certificate != null ? certificate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderDate=" + orderDate +
                ", cost=" + cost +
                ", user=" + user +
                ", certificate=" + certificate +
                '}';
    }
}
