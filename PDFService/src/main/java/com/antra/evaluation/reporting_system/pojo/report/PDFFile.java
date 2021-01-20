package com.antra.evaluation.reporting_system.pojo.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class PDFFile {
    @Id
    @Field(value = "id")
    private String id;

    @Field(value = "file_name")
    private String fileName;

    @Field(value = "file_location")
    private String fileLocation;

    @Field(value = "submitter")
    private String submitter;

    @Field(value = "file_size")
    private Long fileSize;

    @Field(value = "description")
    private String description;

    @Field(value = "generated_time")
    private LocalDateTime generatedTime;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
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
//    public Long getFileSize() {
//        return fileSize;
//    }
//
//    public void setFileSize(Long fileSize) {
//        this.fileSize = fileSize;
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
//        return "PDFFile{" +
//                "id='" + id + '\'' +
//                ", fileName='" + fileName + '\'' +
//                ", fileLocation='" + fileLocation + '\'' +
//                ", submitter='" + submitter + '\'' +
//                ", fileSize=" + fileSize +
//                ", description='" + description + '\'' +
//                ", generatedTime=" + generatedTime +
//                '}';
//    }
}
