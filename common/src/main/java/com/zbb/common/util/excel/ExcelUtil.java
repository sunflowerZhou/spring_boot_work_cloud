package com.zbb.common.util.excel;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sunflower
 * @className
 * @description Excel工具类
 * @date 2020/6/25
 */
@Slf4j
public class ExcelUtil {

    /**
     * 解析Excel
     *
     * @param file      带解析的文件流
     * @param sheetNum  工作簿编号
     * @param hasTitle  是否包含标题
     * @param hasRowNum 是否包含行号列
     * @return list 解析后的excel
     */
    public static List<List<String>> readExcel(FileInputStream file, int sheetNum, boolean hasTitle, boolean hasRowNum) {
        int startRow = hasTitle ? 1 : 0;
        int startCol = hasRowNum ? 1 : 0;
        List<List<String>> listExcel = Lists.newArrayList();
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(file);
            //sheet 从0开始
            Sheet sheet = wb.getSheetAt(sheetNum);
            //取得最后一行的行号
            int rowNum = sheet.getLastRowNum() + 1;
            //行循环开始
            for (int i = startRow; i < rowNum; i++) {
                //行
                Row row = sheet.getRow(i);
                //每行的最后一个单元格位置
                int cellNum = row.getLastCellNum();
                List<String> listExcelCol = Lists.newArrayList();
                for (int j = startCol; j < cellNum; j++) {
                    //列循环开始
                    Cell cell = row.getCell(Short.parseShort(j + ""));
                    String cellValue = null;
                    if (null != cell) {
                        // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                        switch (cell.getCellType()) {
                            case _NONE:
                                cellValue = String.valueOf((int) cell.getNumericCellValue());
                                break;
                            case NUMERIC:
                                cellValue = cell.getNumericCellValue() + "";
                                break;
                            case STRING:
                                cellValue = cell.getStringCellValue();
                                break;
                            case FORMULA:
                                cellValue = cell.getNumericCellValue() + "";
                                break;
                            case BLANK:
                                cellValue = "";
                                break;
                            case BOOLEAN:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case ERROR:
                                cellValue = String.valueOf(cell.getErrorCellValue());
                                break;
                            default:
                                cellValue = "";
                        }
                    } else {
                        cellValue = "";
                    }
                    listExcelCol.add(cellValue);
                }
                listExcel.add(listExcelCol);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }

        return listExcel;
    }

    /**
     * 解析Excel
     *
     * @param file 待解析的文件流
     * @return list 解析后的excel
     */
    public static List<List<String>> readExcel(FileInputStream file) {
        return readExcel(file, 0, true, false);
    }
}
