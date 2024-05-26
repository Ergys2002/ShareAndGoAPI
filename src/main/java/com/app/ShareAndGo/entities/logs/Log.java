package com.app.ShareAndGo.entities.logs;

import com.app.ShareAndGo.entities.BaseEntity;
import com.app.ShareAndGo.enums.LogType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public class Log extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type")
    private LogType logType;

    private String description;
}
