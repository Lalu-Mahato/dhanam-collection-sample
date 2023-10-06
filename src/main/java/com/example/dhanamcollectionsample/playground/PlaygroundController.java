package com.example.dhanamcollectionsample.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dhanamcollectionsample.group.GroupRepository;
import com.example.dhanamcollectionsample.group.GroupService;
import com.example.dhanamcollectionsample.group.entity.Group;

@RestController
@RequestMapping("/api/v1/playground")
public class PlaygroundController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    GroupService groupService;
    private static final int THREAD_POOL_SIZE = 5;

    @PostMapping
    public String test(@RequestParam("file") MultipartFile multipartFile) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheet("Sheet1");

            int totalRows = worksheet.getPhysicalNumberOfRows();
            int chunkSize = totalRows / THREAD_POOL_SIZE;

            ExecutorService executor = Executors.newFixedThreadPool(chunkSize);
            System.out.println(totalRows + " " + THREAD_POOL_SIZE + " " + chunkSize);

            for (int i = 0; i < THREAD_POOL_SIZE; i++) {
                int start = i * chunkSize;
                int end = (i == THREAD_POOL_SIZE - 1) ? totalRows : (i + 1) * chunkSize;

                executor.submit(new ExcelReaderTask(worksheet, start, end));
            }

            executor.shutdown();
            workbook.close();

            return "Excel reading process initiated.";
        } catch (Exception e) {
            return "true";
        }
    }

    @PostMapping("/sample")
    public String sample(@RequestParam("file") MultipartFile multipartFile) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheet("Sheet1");

            int totalRows = worksheet.getPhysicalNumberOfRows();
            int chunkSize = totalRows / THREAD_POOL_SIZE;

            ExecutorService executor = Executors.newFixedThreadPool(chunkSize);
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (int i = 0; i < THREAD_POOL_SIZE; i++) {
                int start = i * chunkSize;
                int end = (i == THREAD_POOL_SIZE - 1) ? totalRows : (i + 1) * chunkSize;

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> processRows(worksheet, start, end),
                        executor);

                futures.add(future);
            }

            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

            allOf.get();
            workbook.close();
            return "Excel reading process initiated.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred during Excel reading: " + e.getMessage();
        }
    }

    private void processRows(XSSFSheet sheet, int startRow, int endRow) {
        for (int i = startRow; i < endRow; i++) {
            Row row = sheet.getRow(i);
            // Process the row as needed
            // System.out.println("Processing row: " + row.getCell(0).getStringCellValue());

            String groupId = (String) row.getCell(2).getStringCellValue();
            Optional<Group> optionalGroup = groupRepository.findByGroupId(groupId);

            Group group;
            if (optionalGroup.isPresent()) {
                group = optionalGroup.get();
            } else {
                // creating new group
                Group newGroup = new Group();
                newGroup.setGroupId((String) row.getCell(2).getStringCellValue());
                newGroup.setGroupName((String) row.getCell(3).getStringCellValue());
                newGroup.setCentreId((String) row.getCell(9).getStringCellValue());
                newGroup.setLocaleName((String) row.getCell(10).getStringCellValue());
                newGroup.setLocalId((String) row.getCell(11).getStringCellValue());
                newGroup.setCentreName((String) row.getCell(23).getStringCellValue());
                newGroup.setEmiStatus((String) (row.getCell(26).getStringCellValue()).toLowerCase());
                group = groupService.create(newGroup);
            }
        }
    }

}
