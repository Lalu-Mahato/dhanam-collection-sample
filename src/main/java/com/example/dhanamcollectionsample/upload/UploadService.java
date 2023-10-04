package com.example.dhanamcollectionsample.upload;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dhanamcollectionsample.group.GroupService;
import com.example.dhanamcollectionsample.group.entity.Group;
import com.example.dhanamcollectionsample.prospect.ProspectService;
import com.example.dhanamcollectionsample.prospect.entity.Prospect;

@Service
public class UploadService {
    @Autowired
    private GroupService groupService;
    @Autowired
    private ProspectService prospectService;

    public ResponseEntity<Object> uploadCollections(MultipartFile multipartFile) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheet("Sheet1");
            XSSFRow row = worksheet.getRow(1);

            // creating new group
            Group group = new Group();
            group.setGroupId((String) row.getCell(2).getStringCellValue());
            group.setGroupName((String) row.getCell(3).getStringCellValue());
            group.setCentreId((String) row.getCell(9).getStringCellValue());
            group.setLocaleName((String) row.getCell(10).getStringCellValue());
            group.setLocalId((String) row.getCell(11).getStringCellValue());
            group.setCentreName((String) row.getCell(23).getStringCellValue());
            group.setEmiStatus((String) (row.getCell(26).getStringCellValue()).toLowerCase());
            Group newGroup = groupService.create(group);

            // creating new prospect
            Prospect prospect = new Prospect();
            String groupHead = row.getCell(12).getStringCellValue();
            prospect.setGrpHead((boolean) Boolean.parseBoolean(groupHead));

            prospect.setProspectId((String) row.getCell(0).getStringCellValue());
            prospect.setProspectName((String) row.getCell(1).getStringCellValue());
            prospect.setProspectMobile((Long) Math.round(row.getCell(5).getNumericCellValue()));
            prospect.setCifId((Long) Math.round(row.getCell(6).getNumericCellValue()));

            Prospect newProspect = prospectService.create(prospect);
            workbook.close();

            // mapping prospect to group
            prospectService.mappingToGroup(newGroup, newProspect);

            return ResponseEntity.status(HttpStatus.OK).body("Collection data uploaded successfully.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e.getMessage());
        }
    }
}
