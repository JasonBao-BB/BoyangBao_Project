package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.api.PDFRequest;
import com.antra.evaluation.reporting_system.pojo.api.PDFResponse;
import com.antra.evaluation.reporting_system.pojo.report.PDFFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface PDFService {
    PDFFile createPDF(PDFRequest request);

    PDFResponse findById(String id);

    InputStream findPDFById(String id) throws FileNotFoundException;

    List<PDFResponse> findAllPDFs();

    PDFResponse updatePDF(String id, PDFRequest request);

    PDFResponse deletePDF(String id) throws FileNotFoundException;
}
