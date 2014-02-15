package de.powerstaff.web.servlet;

import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerToTag;
import de.powerstaff.business.entity.Tag;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.TagService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaggedFreelancerDownloadServlet implements HttpRequestHandler {

    private FreelancerService freelancerService;

    private TagService tagService;

    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    private String saveObject(Object aValue) {
        if (aValue == null) {
            return "";
        }
        if (aValue instanceof Date) {
            SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");
            return theFormat.format((Date) aValue);
        }
        return aValue.toString();
    }

    @Override
    public void handleRequest(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {

        String theTagID = (String) aRequest.getAttribute("tagid");
        Tag theTag = tagService.getTagByID(Long.parseLong(theTagID));

        HSSFWorkbook theWorkbook = new HSSFWorkbook();
        HSSFSheet theWorkSheet = theWorkbook.createSheet("GetaggteFreiberufler_"+theTag.getName());

        int aRow = 0;
        // Header
        HSSFRow theRow = theWorkSheet.createRow(aRow++);
        addCellToRow(theRow, 0, "Name1");
        addCellToRow(theRow, 1, "Name2");
        addCellToRow(theRow, 2, "Code");
        addCellToRow(theRow, 3, "Verfügbarkeit");
        addCellToRow(theRow, 4, "Satz");
        addCellToRow(theRow, 5, "Plz");
        addCellToRow(theRow, 6, "Letzter Kontakt");
        addCellToRow(theRow, 7, "Skills / Tags");

        for (Freelancer theFreelancer : freelancerService.findFreelancerByTag(theTag)) {

            StringBuilder theResult = new StringBuilder(saveObject(theFreelancer.getSkills().replace("\f", "").replace("\n", "").replace("\t", "")));
            for (FreelancerToTag theTagAssignment : theFreelancer.getTags()) {
                if (theResult.length() > 0) {
                    theResult.append(" ");
                }
                theResult.append(theTagAssignment.getTag().getName());
            }

            HSSFRow theFreelancerRow = theWorkSheet.createRow(aRow++);
            addCellToRow(theFreelancerRow, 0, saveObject(theFreelancer.getName1()));
            addCellToRow(theFreelancerRow, 1, saveObject(theFreelancer.getName2()));
            addCellToRow(theFreelancerRow, 2, saveObject(theFreelancer.getCode()));
            addCellToRow(theFreelancerRow, 3, saveObject(theFreelancer.getAvailabilityAsDate()));
            addCellToRow(theFreelancerRow, 4, saveObject(theFreelancer.getSallaryLong()));
            addCellToRow(theFreelancerRow, 5, saveObject(theFreelancer.getPlz()));
            addCellToRow(theFreelancerRow, 6, saveObject(theFreelancer.getLastContact()));
            addCellToRow(theFreelancerRow, 7, theResult.toString());
        }


        aResponse.setHeader("Content-disposition", "attachment; filename=GetaggteFreiberufler_" + theTag.getName() + ".xls");

        OutputStream theOutput = aResponse.getOutputStream();
        theWorkbook.write(theOutput);
        theOutput.flush();
    }

    private void addCellToRow(HSSFRow theRow, int aColumn, String aValue) {
        HSSFCell theCell1 = theRow.createCell(aColumn);
        theCell1.setCellValue(aValue);
    }
}
