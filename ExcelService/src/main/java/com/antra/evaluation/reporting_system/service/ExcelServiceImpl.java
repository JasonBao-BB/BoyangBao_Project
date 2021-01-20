package com.antra.evaluation.reporting_system.service;

import com.amazonaws.services.s3.AmazonS3;
import com.antra.evaluation.reporting_system.exception.FileGenerationException;
import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelData;
import com.antra.evaluation.reporting_system.pojo.report.ExcelDataHeader;
import com.antra.evaluation.reporting_system.pojo.report.ExcelDataSheet;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import com.antra.evaluation.reporting_system.repo.ExcelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);

    private final  ExcelRepository excelRepository;

    @Value("${s3.bucket}")
    private String s3Bucket;

    private final ExcelGenerationService excelGenerationService;

    private final AmazonS3 s3Client;

    @Autowired
    public ExcelServiceImpl(ExcelRepository excelRepository, ExcelGenerationService excelGenerationService, AmazonS3 s3Client) {
        this.excelRepository = excelRepository;
        this.excelGenerationService = excelGenerationService;
        this.s3Client = s3Client;
    }

    /*
    * findExcelById(String id)
    * */
    @Override
    public InputStream getExcelBodyById(String id) throws FileNotFoundException {
        Optional<ExcelFile> fileInfo = excelRepository.findById(id);
        return new FileInputStream(fileInfo.orElseThrow(FileNotFoundException::new).getFileLocation());
    }

//    public ExcelFile createExcel(final ExcelRequest request) {
//        ExcelFile file = new ExcelFile();
//        file.setFileId("File-" + UUID.randomUUID().toString());
//        file.setSubmitter(request.getSubmitter());
//        file.setDescription(request.getDescription());
//        file.setGeneratedTime(LocalDateTime.now());
//
//        ExcelFile generatedFile =
//    }

    /*
    * save Excel file
    * */
    @Override
    public ExcelFile generateFile(ExcelRequest request, boolean multisheet) {
        ExcelFile excelFile = new ExcelFile();
        excelFile.setFileId("EXCEL-"+UUID.randomUUID().toString());
        ExcelData data = new ExcelData();
        data.setTitle(request.getDescription());
        data.setFileId(excelFile.getFileId());
        data.setSubmitter(excelFile.getSubmitter());

        if(multisheet){
            data.setSheets(generateMultiSheet(request));
        }else {
            data.setSheets(generateSheet(request));
        }
        try {
            File generatedFile = excelGenerationService.generateExcelReport(data);
            excelFile.setFileLocation(generatedFile.getAbsolutePath());
            excelFile.setFileName(generatedFile.getName());
            excelFile.setGeneratedTime(LocalDateTime.now());
            excelFile.setSubmitter(request.getSubmitter());
            excelFile.setFileSize(generatedFile.length());

            excelFile.setDescription(request.getDescription());
//          File temp = new File(generatedFil);
//          s3Client.putObject(s3Bucket,fileInfo.getFileId(),temp);
        } catch (IOException e) {
//            log.error("Error in generateFile()", e);
            throw new FileGenerationException(e);
        }
        //Convert ExcelFile object into File object
        File temp = new File(excelFile.getFileLocation());
//        System.out.println(temp.getName());
        System.out.println("excel");
        excelRepository.save(excelFile);

        s3Client.putObject(s3Bucket, excelFile.getFileId(), temp);
//        excelRepository.saveFile(fileInfo);
        log.debug("Excel File Generated : {}", excelFile);
        return excelFile;
    }

    /*
    * find all excels
    * */
    @Override
    public List<ExcelFile> getExcelList() {
        List<ExcelFile> files = excelRepository.findAll();
//        return excelRepository.getFiles();
        return files;
    }

    /*
    * Delete file
    * */
    @Override
    public ExcelFile deleteFile(String id) throws FileNotFoundException {
        ExcelFile excelFile = excelRepository.deleteExcelFileByFileId(id);
//        ExcelFile excelFile = excelRepository.deleteFile(id);
        if (excelFile == null) {
            throw new FileNotFoundException();
        }
        File file = new File(excelFile.getFileLocation());
        file.delete();
        return excelFile;
    }

    private List<ExcelDataSheet> generateSheet(ExcelRequest request) {
        List<ExcelDataSheet> sheets = new ArrayList<>();
        ExcelDataSheet sheet = new ExcelDataSheet();
        sheet.setHeaders(request.getHeaders().stream().map(ExcelDataHeader::new).collect(Collectors.toList()));
        sheet.setDataRows(request.getData().stream().map(listOfString -> (List<Object>) new ArrayList<Object>(listOfString)).collect(Collectors.toList()));
        sheet.setTitle("sheet-1");
        sheets.add(sheet);
        return sheets;
    }
    private List<ExcelDataSheet> generateMultiSheet(ExcelRequest request) {
        List<ExcelDataSheet> sheets = new ArrayList<>();
        int index = request.getHeaders().indexOf(((MultiSheetExcelRequest) request).getSplitBy());
        Map<String, List<List<String>>> splittedData = request.getData().stream().collect(Collectors.groupingBy(row -> (String)row.get(index)));
        List<ExcelDataHeader> headers = request.getHeaders().stream().map(ExcelDataHeader::new).collect(Collectors.toList());
        splittedData.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(
                entry ->{
                    ExcelDataSheet sheet = new ExcelDataSheet();
                    sheet.setHeaders(headers);
                    sheet.setDataRows(entry.getValue().stream().map(listOfString -> {
                        List<Object> listOfObject = new ArrayList<>();
                        listOfString.forEach(listOfObject::add);
                        return listOfObject;
                    }).collect(Collectors.toList()));
                    sheet.setTitle(entry.getKey());
                    sheets.add(sheet);
                }
        );
        return sheets;
    }
}
