package com.antra.evaluation.reporting_system.endpoint;

import com.antra.evaluation.reporting_system.exception.ErrorResponse;
import com.antra.evaluation.reporting_system.exception.FileGenerationException;
import com.antra.evaluation.reporting_system.pojo.api.PDFRequest;
import com.antra.evaluation.reporting_system.pojo.api.PDFResponse;
import com.antra.evaluation.reporting_system.pojo.report.PDFFile;
import com.antra.evaluation.reporting_system.service.PDFService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
@CrossOrigin("*")
@RestController
public class PDFGenerationController {

    private static final Logger log = LoggerFactory.getLogger(PDFGenerationController.class);

    private PDFService pdfService;

    @Autowired
    public PDFGenerationController(PDFService pdfService) {
        this.pdfService = pdfService;
    }


    @PostMapping("/pdf")
    @ApiOperation("Generate PDF")
    public ResponseEntity<PDFResponse> createPDF(@RequestBody @Validated PDFRequest request) {
        log.info("Got request to generate PDF: {}", request);

        PDFResponse response = new PDFResponse();
        PDFFile file = null;
        response.setReqId(request.getReqId());

        try {
            file = pdfService.createPDF(request);
            response.setId(file.getId());
            response.setFileLocation(file.getFileLocation());
            response.setFileSize(file.getFileSize());
            log.info("Generated: {}", file);
        } catch (Exception e) {
            response.setFailed(true);
            log.error("Error in generating pdf", e);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/pdf/{id}")
    @ApiOperation("Show PDF information by ID")
    public ResponseEntity<PDFResponse> findPDFById(@PathVariable("id") String id){
        PDFResponse pdfResponse = pdfService.findById(id);
        return new ResponseEntity<>(pdfResponse, HttpStatus.OK);
    }

    @GetMapping("/pdf")
    @ApiOperation("Show All PDF")
    public ResponseEntity<List<PDFResponse>> listPDFs() {
        List<PDFResponse> list = pdfService.findAllPDFs();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/pdf/{id}")
    @ApiOperation("Update PDF")
    public ResponseEntity<PDFResponse> updatePDF(@PathVariable String id,
                                                 @RequestBody PDFRequest pdfRequest)
            throws FileNotFoundException {

        PDFResponse pdfResponse = pdfService.updatePDF(id, pdfRequest);

        return new ResponseEntity<>(pdfResponse, HttpStatus.OK);
    }

    @DeleteMapping("/pdf/{id}")
    @ApiOperation("Delete PDF")
    public ResponseEntity<PDFResponse> deletePDF(@PathVariable String id) throws FileNotFoundException {
        PDFResponse pdfResponse = pdfService.deletePDF(id);
        return new ResponseEntity<>(pdfResponse,HttpStatus.OK);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFound(Exception e) {
        log.error("The file doesn't exist", e);
        return new ResponseEntity<>(new ErrorResponse("The file doesn't exist", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileGenerationException.class)
    public ResponseEntity<ErrorResponse> handleFileGenerationExceptions(Exception e) {
        log.error("Cannot Generate Excel File", e);
        return new ResponseEntity<>(new ErrorResponse("Cannot Generate PDF File", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownExceptions(Exception e) {
        log.error("Something is wrong", e);
        return new ResponseEntity<>(new ErrorResponse("Something is wrong", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
