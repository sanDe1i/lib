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

    @Column(name = "marc_count")
    private int marcCount;
    @Column(name = "pdf_count")
    private int pdfCount;

    @Column(name = "epub_count")
    private int epubCount;

    @Column(name = "view")
    private String view;

    @Column(name = "download")
    private String download;

    @Column(name = "borrow")
    private int borrow;

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

    public String getDisplay() {
        return display;
    }

    public int getMarcCount() {
        return marcCount;
    }

    public void setMarcCount(int marcCount) {
        this.marcCount = marcCount;
    }

    public int getPdfCount() {
        return pdfCount;
    }

    public void setPdfCount(int pdfCount) {
        this.pdfCount = pdfCount;
    }

    public int getEpubCount() {
        return epubCount;
    }

    public void setEpubCount(int epubCount) {
        this.epubCount = epubCount;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public int getBorrow() {
        return borrow;
    }

    public void setBorrow(int borrow) {
        this.borrow = borrow;
    }
}

