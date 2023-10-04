package com.example.dhanamcollectionsample.upload;

import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dhanamcollectionsample.group.GroupRepository;
import com.example.dhanamcollectionsample.group.GroupService;
import com.example.dhanamcollectionsample.group.entity.Group;
import com.example.dhanamcollectionsample.loan.LoanService;
import com.example.dhanamcollectionsample.loan.entity.Loan;
import com.example.dhanamcollectionsample.prospect.ProspectService;
import com.example.dhanamcollectionsample.prospect.entity.Prospect;

@Service
public class UploadService {
    @Autowired
    private GroupService groupService;
    @Autowired
    private ProspectService prospectService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private GroupRepository groupRepository;

    public ResponseEntity<Object> uploadCollections(MultipartFile multipartFile) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheet("Sheet1");

            for (var i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = worksheet.getRow(i);

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

                // creating new prospect
                Prospect newProspect = new Prospect();
                String groupHead = row.getCell(12).getStringCellValue();
                newProspect.setGrpHead((boolean) Boolean.parseBoolean(groupHead));

                newProspect.setProspectId((String) row.getCell(0).getStringCellValue());
                newProspect.setProspectName((String) row.getCell(1).getStringCellValue());
                newProspect.setProspectMobile((Long) Math.round(row.getCell(5).getNumericCellValue()));
                newProspect.setCifId((Long) Math.round(row.getCell(6).getNumericCellValue()));
                Prospect prospect = prospectService.create(newProspect);

                // creating new loan
                Loan newLoan = new Loan();
                newLoan.setProductType((String) row.getCell(4).getStringCellValue());
                newLoan.setLoanId((String) row.getCell(8).getStringCellValue());
                newLoan.setLoanAccNumber((Long) Math.round(row.getCell(7).getNumericCellValue()));
                newLoan.setLoanAmount((double) Math.round(row.getCell(13).getNumericCellValue()));
                newLoan.setDpd((double) Math.round(row.getCell(15).getNumericCellValue()));
                newLoan.setDpdAmount((double) Math.round(row.getCell(16).getNumericCellValue()));
                newLoan.setOsBalance((double) Math.round(row.getCell(17).getNumericCellValue()));
                Loan loan = loanService.create(newLoan);

                // mapping prospect to group
                prospectService.mappingToGroup(group, prospect);

                // mapping loan to prospect
                loanService.mappingToProspect(loan, prospect);
            }
            workbook.close();
            return ResponseEntity.status(HttpStatus.OK).body("Collection data uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e.getMessage());
        }
    }
}
