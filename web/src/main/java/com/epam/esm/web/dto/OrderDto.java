package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OrderDto extends RepresentationModel<OrderDto> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @JsonProperty(value="user", access = JsonProperty.Access.READ_ONLY)
    private UserDto userDto;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private GiftCertificateDto certificate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal cost;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime createDate;

    @JsonCreator
    public OrderDto() {
    }

    public OrderDto(long id, UserDto userDto, GiftCertificateDto certificate,
                    BigDecimal cost, ZonedDateTime createDate) {
        this.id = id;
        this.userDto = userDto;
        this.certificate = certificate;
        this.cost = cost;
        this.createDate = createDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public GiftCertificateDto getCertificate() {
        return certificate;
    }

    public void setCertificate(GiftCertificateDto certificate) {
        this.certificate = certificate;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDto orderDto = (OrderDto) o;

        if (!Objects.equals(userDto, orderDto.userDto)) return false;
        if (!Objects.equals(certificate, orderDto.certificate))
            return false;
        if (!Objects.equals(cost, orderDto.cost)) return false;
        return Objects.equals(createDate, orderDto.createDate);
    }

    @Override
    public int hashCode() {
        int result = userDto != null ? userDto.hashCode() : 0;
        result = 31 * result + (certificate != null ? certificate.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        return result;
    }
}
