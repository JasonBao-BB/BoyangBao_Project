package com.antra.evaluation.reporting_system.service;

import com.amazonaws.services.s3.AmazonS3;
import com.antra.evaluation.reporting_system.pojo.api.PDFRequest;
import com.antra.evaluation.reporting_system.pojo.api.PDFResponse;
import com.antra.evaluation.reporting_system.pojo.report.PDFFile;
import com.antra.evaluation.reporting_system.repo.PDFRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PDFServiceImpl implements PDFService {

    private static final Logger log = LoggerFactory.getLogger(PDFServiceImpl.class);

    private final PDFRepository pdfRepository;

    private final PDFGenerator generator;

    private final AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String s3Bucket;

    public PDFServiceImpl(PDFRepository repository, PDFGenerator generator, AmazonS3 s3Client) {
        this.pdfRepository = repository;
        this.generator = generator;
        this.s3Client = s3Client;
    }
    /*
    * Store PDFFile object into databases
    * Store
    * */
    @Override
    public PDFFile createPDF(final PDFRequest request) {
        /*
        * Create a PDF file and initialize file ID + file AUTHOR + file Description + file TIME
        * */
        PDFFile file = new PDFFile();
        file.setId("PDF-" + UUID.randomUUID().toString());
        file.setSubmitter(request.getSubmitter());
        file.setDescription(request.getDescription());
        file.setGeneratedTime(LocalDateTime.now());
        /*
        * Processing Request JSON data into a real PDF file in disk
        * Open it as an object and set meta-data
        * */
        PDFFile generatedFile= generator.generate(request);

        File temp = new File(generatedFile.getFileLocation());
        log.debug("Upload temp file to s3 {}", generatedFile.getFileLocation());
        s3Client.putObject(s3Bucket,file.getId(),temp);
        log.debug("Uploaded");

        file.setFileLocation(String.join("/",s3Bucket,file.getId()));
        System.out.println("PDF_经过更改后的FileLocation:"+file.getFileLocation());

        file.setFileSize(generatedFile.getFileSize());
        file.setFileName(generatedFile.getFileName());
        pdfRepository.save(file);

        log.debug("clear tem file {}", file.getFileLocation());
        if(temp.delete()){
            log.debug("cleared");
        }

        return file;
    }

    @Override
    public PDFResponse findById(String id) {
        PDFFile pdfFile = pdfRepository.findById(id).orElse(null);
        System.out.println(pdfFile.toString());
        PDFResponse pdfResponse = new PDFResponse();
        BeanUtils.copyProperties(pdfFile, pdfResponse);
        return pdfResponse;
    }
    /*
    * Used for Download
    * */
    @Override
    public InputStream findPDFById(String id) throws FileNotFoundException {

        Optional<PDFFile> fileInfo = pdfRepository.findById(id);
        return new FileInputStream(fileInfo.orElseThrow(FileNotFoundException::new).getFileLocation());
    }

    @Override
    public List<PDFResponse> findAllPDFs() {
        List<PDFFile> pdfFiles = pdfRepository.findAll();
        List<PDFResponse> responses = new ArrayList<>();

        pdfFiles.forEach(pdfFile -> {
            System.out.println(pdfFile.getId());
            PDFResponse pdfResponse = new PDFResponse();
            BeanUtils.copyProperties(pdfFile, pdfResponse);
            responses.add(pdfResponse);
        });

        return responses;
    }

    @Override
    public PDFResponse updatePDF(String id, PDFRequest pdfRequest) {

        Optional<PDFFile> optional = pdfRepository.findById(id);

        if (optional.isEmpty()){
            return null;
        }
        //Last version of PDF file
        PDFFile pdfFile = optional.get();
        //New PDF Request
        PDFFile generatedFile= generator.generate(pdfRequest);
        File temp = new File(generatedFile.getFileLocation());
        s3Client.putObject(s3Bucket,pdfFile.getId(),temp);
        //Update last version of PDF
        pdfFile.setDescription(generatedFile.getDescription());
        pdfFile.setSubmitter(generatedFile.getSubmitter());
        pdfFile.setGeneratedTime(generatedFile.getGeneratedTime());
        pdfFile.setFileLocation(generatedFile.getFileLocation());
        pdfFile.setFileSize(generatedFile.getFileSize());
        pdfFile.setFileName(generatedFile.getFileName());

        pdfRepository.save(pdfFile);

        PDFResponse pdfResponse = new PDFResponse();
        BeanUtils.copyProperties(pdfFile, pdfResponse);

        return pdfResponse;
    }

    @Override
    public PDFResponse deletePDF(String id) throws FileNotFoundException {
        PDFFile pdfFile = pdfRepository.findById(id).orElse(null);
        if (pdfFile == null) {
            throw new FileNotFoundException();
        }
        File file = new File(pdfFile.getFileLocation());
        file.delete();

        PDFResponse pdfResponse = new PDFResponse();
        BeanUtils.copyProperties(pdfFile, pdfResponse);
        pdfRepository.deleteById(id);
        return pdfResponse;
    }

}
