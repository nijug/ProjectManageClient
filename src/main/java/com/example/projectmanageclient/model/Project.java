package com.example.projectmanageclient.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Project {

    private Integer id;

    private String name;

    private String description;

    private Date dateStarted;

    private Date dateEnded;

    private String priority;

    public Project() {
    }

    public Project( String name, String description, Date dateStarted, Date dateEnded, String priority) {
        this.name = name;
        this.description = description;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;
        this.priority = priority;
    }


}


