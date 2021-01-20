package com.antra.evaluation.reporting_system.pojo.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "excel")
public class ExcelFile {
    @Id
    @Column(name = "id")
    private String fileId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_location")
    private String fileLocation;

    @Column(name = "submitter")
    private String submitter;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "description")
    private String description;

    @Column(name = "generated_time")
    private LocalDateTime generatedTime;

//    public Long getFileSize() {
//        return fileSize;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setFileSize(Long fileSize) {
//        this.fileSize = fileSize;
//    }
//
//    public String getFileId() {
//        return fileId;
//    }
//
//    public void setFileId(String fileId) {
//        this.fileId = fileId;
//    }
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public String getFileLocation() {
//        return fileLocation;
//    }
//
//    public void setFileLocation(String fileLocation) {
//        this.fileLocation = fileLocation;
//    }
//
//    public String getSubmitter() {
//        return submitter;
//    }
//
//    public void setSubmitter(String submitter) {
//        this.submitter = submitter;
//    }
//
//    public LocalDateTime getGeneratedTime() {
//        return generatedTime;
//    }
//
//    public void setGeneratedTime(LocalDateTime generatedTime) {
//        this.generatedTime = generatedTime;
//    }
//
//    @Override
//    public String toString() {
//        return "ExcelFile{" +
//                "fileId='" + fileId + '\'' +
//                ", fileName='" + fileName + '\'' +
//                ", fileLocation='" + fileLocation + '\'' +
//                ", submitter='" + submitter + '\'' +
//                ", fileSize=" + fileSize +
//                ", generatedTime=" + generatedTime +
//                '}';
//    }
}
