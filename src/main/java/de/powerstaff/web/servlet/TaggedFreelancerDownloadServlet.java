package de.powerstaff.web.servlet;

import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerToTag;
import de.powerstaff.business.service.FreelancerService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TaggedFreelancerDownloadServlet implements HttpRequestHandler {

    private FreelancerService freelancerService;

    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
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

    private Object safeDateFormat(String aValue) {
        if (aValue.length() == 10) {
            SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");
            try {
                return theFormat.parse(aValue);
            } catch (ParseException e) {
                return aValue;
            }
        }
        if (aValue.length() == 8) {
            SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yy");
            try {
                return theFormat.parse(aValue);
            } catch (ParseException e) {
                return aValue;
            }
        }
        return aValue;
    }

    @Override
    public void handleRequest(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {

        String theTagID = (String) aRequest.getAttribute("tagid");

        HSSFWorkbook theWorkbook = new HSSFWorkbook();
        HSSFSheet theWorkSheet = theWorkbook.createSheet("GetaggteFreiberufler");

        HSSFCellStyle theDateStyle = theWorkbook.createCellStyle();
        theDateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("d/m/jj"));

        int aRow = 0;
        // Header
        HSSFRow theRow = theWorkSheet.createRow(aRow++);
        addCellToRow(theRow, 0, "Anrede");
        addCellToRow(theRow, 1, "Name1");
        addCellToRow(theRow, 2, "Name2");
        addCellToRow(theRow, 3, "eMail");
        addCellToRow(theRow, 4, "Code");
        addCellToRow(theRow, 5, "Verfügbarkeit");
        addCellToRow(theRow, 6, "Satz");
        addCellToRow(theRow, 7, "Plz");
        addCellToRow(theRow, 8, "Letzter Kontakt");
        addCellToRow(theRow, 9, "Skills");
        addCellToRow(theRow, 10, "Tags");

        Set<Long> theTagsIDs = new HashSet();
        try {
            for (String theID : StringUtils.split(URLDecoder.decode(theTagID, aRequest.getCharacterEncoding()), ',')) {
                theTagsIDs.add(Long.valueOf(theID));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        for (Freelancer theFreelancer : freelancerService.findFreelancerByTagIDs(theTagsIDs)) {

            String theSkills  = saveObject(theFreelancer.getSkills().replace("\f", "").replace("\n", "").replace("\t", ""));

            StringBuilder theTagList = new StringBuilder();
            for (FreelancerToTag theTagAssignment : theFreelancer.getTags()) {
                if (theTagList.length() > 0) {
                    theTagList.append(" ");
                }
                theTagList.append(theTagAssignment.getTag().getName());
            }

            HSSFRow theFreelancerRow = theWorkSheet.createRow(aRow++);
            addCellToRow(theFreelancerRow, 0, saveObject(theFreelancer.getTitel()));
            addCellToRow(theFreelancerRow, 1, saveObject(theFreelancer.getName1()));
            addCellToRow(theFreelancerRow, 2, saveObject(theFreelancer.getName2()));
            addCellToRow(theFreelancerRow, 3, saveObject(theFreelancer.getFirstContactEMail())); // eMail
            addCellToRow(theFreelancerRow, 4, saveObject(theFreelancer.getCode()));
            addCellToRow(theFreelancerRow, 5, saveObject(theFreelancer.getAvailabilityAsDate()), theDateStyle);
            addCellToRow(theFreelancerRow, 6, saveObject(theFreelancer.getSallaryLong()));
            addCellToRow(theFreelancerRow, 7, saveObject(theFreelancer.getPlz()));
            addCellToRow(theFreelancerRow, 8, saveObject(safeDateFormat(theFreelancer.getLastContact())));
            addCellToRow(theFreelancerRow, 9, saveObject(theSkills));
            addCellToRow(theFreelancerRow, 10, theTagList.toString());
        }


        aResponse.setHeader("Content-disposition", "attachment; filename=GetaggteFreiberufler.xls");

        OutputStream theOutput = aResponse.getOutputStream();
        theWorkbook.write(theOutput);
        theOutput.flush();
    }

    private HSSFCell addCellToRow(HSSFRow aRow, int aColumn, String aValue) {
        HSSFCell theCell1 = aRow.createCell(aColumn);
        theCell1.setCellValue(aValue);
        return theCell1;
    }

    private void addCellToRow(HSSFRow aRow, int aColumn, String aValue, HSSFCellStyle aFormat) {
        HSSFCell theCell1 = addCellToRow(aRow, aColumn, aValue);
        theCell1.setCellStyle(aFormat);
        theCell1.setCellValue(aValue);
    }
}
