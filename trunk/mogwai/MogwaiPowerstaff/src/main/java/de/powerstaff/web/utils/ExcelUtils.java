package de.powerstaff.web.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelUtils {

    public static HSSFCell addCellToRow(HSSFRow aRow, int aColumn, String aValue) {
        HSSFCell theCell1 = aRow.createCell(aColumn);
        theCell1.setCellValue(aValue);
        return theCell1;
    }

    public static void addCellToRow(HSSFRow aRow, int aColumn, String aValue, HSSFCellStyle aFormat) {
        HSSFCell theCell1 = addCellToRow(aRow, aColumn, aValue);
        theCell1.setCellStyle(aFormat);
        theCell1.setCellValue(aValue);
    }

    public static String saveObject(Object aValue) {
        if (aValue == null) {
            return "";
        }
        if (aValue instanceof Date) {
            SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");
            return theFormat.format((Date) aValue);
        }
        return aValue.toString();
    }
}