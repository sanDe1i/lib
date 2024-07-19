package com.example.lm.Model;


import jakarta.persistence.*;

@Entity
@Table(name = "resources_lib")
public class ResourcesLib {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="alternate_name")
    private String alternateName;

    @Column(name="type")
    private String type;

    @Column(name="description")
    private String description;

    @Column(name="display")
    private String display;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay(String display) {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}

