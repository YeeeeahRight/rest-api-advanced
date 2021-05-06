package com.epam.esm.persistence.audit;

import javax.persistence.*;

import com.epam.esm.persistence.model.entity.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityAuditListener {
    private final Logger LOGGER = LoggerFactory.getLogger(EntityAuditListener.class);

    @PrePersist
    private void prePersistAudit(AbstractEntity entity) {
        LOGGER.info("Persisting an entity: " + entity);
    }

    @PostPersist
    private void postPersistAudit(AbstractEntity entity) {
        LOGGER.info("The entity persisted: " + entity + " with id=" + entity.getId());
    }

    @PreUpdate
    private void preUpdateAudit(AbstractEntity entity) {
        LOGGER.info("Updating an entity: " + entity);
    }

    @PostUpdate
    private void postUpdateAudit(AbstractEntity entity) {
        LOGGER.info("The entity updated: " + entity);
    }

    @PreRemove
    private void preRemoveAudit(AbstractEntity entity) {
        LOGGER.info("Removing an entity: " + entity);
    }

    @PostRemove
    private void postRemoveAudit(AbstractEntity entity) {
        LOGGER.info("The entity removed: " + entity);
    }
}
