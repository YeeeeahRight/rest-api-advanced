package com.epam.esm.persistence.model;

import java.math.BigDecimal;

public class BestUserTag {
    private long id;
    private String tagName;
    private BigDecimal highestCost;

    public BestUserTag() {
    }

    public BestUserTag(long id, String tagName, BigDecimal highestCost) {
        this.id = id;
        this.tagName = tagName;
        this.highestCost = highestCost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public BigDecimal getHighestCost() {
        return highestCost;
    }

    public void setHighestCost(BigDecimal highestCost) {
        this.highestCost = highestCost;
    }
}
