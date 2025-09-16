package com.liule13.todoserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Todo {
    @Id
    private String id;
    private String text;
    private Boolean done;
    @PrePersist
    public void ensureId() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
    }
}
