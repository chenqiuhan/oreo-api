package com.kawnnor.oreo.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExportUtil{
    /**  
     * 导出Excel(xls)工具类   
     * @param sheetName  
     * @param title  
     * @param values  
     * @param workbook  
     * @return  
     * @throws ParseException   
     */  
    public static HSSFWorkbook getHssFWorkbook(String sheetName,String[] title,String[][] values) throws ParseException{  
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        HSSFWorkbook workbook = new HSSFWorkbook();  
          
        // 创建一个sheet  
        HSSFSheet sheet = workbook.createSheet(sheetName);  
        // 添加表头  
        HSSFRow row = sheet.createRow(0);  
          
        // 单元格样式  
        HSSFCellStyle style = workbook.createCellStyle();  
        HSSFDataFormat format2 = workbook.createDataFormat();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        
        style.setFont(font);
        HSSFCell cell=null;  
        // 创建标题  
        for(int i=0;i<title.length;i++){  
            cell = row.createCell(i);  
            cell.setCellValue(title[i]);  
            cell.setCellStyle(style);
            if(i==10&&"交易开户行".equals(title[i])){
                style.setDataFormat(format2.getBuiltinFormat("0"));
            }
            if(i==title.length-1){  
                sheet.setDefaultColumnWidth(20);  
            }  
        }  
          
          
        // 创建内容  
//         if(values !=null && values.length>0){  
//             for(int j=0;j<values.length;j++){  
//                 row = sheet.createRow(j+1);  
//                 for(int k=0;k<values[j].length;k++){  
//                     cell = row.createCell(k);  
// //                  if(k == values[j].length-1){  
// //                      cell.setCellValue(format.format(format.parse(values[j][k])));  
// //                  }else{  
//                         if(k == 0){  
//                             cell.setCellValue(Long.parseLong(values[j][k]));  
//                         }else{  
//                             if(numType(values[j][k])){  
//                                 cell.setCellValue(Double.parseDouble(values[j][k]));  
//                             }else{  
//                                 cell.setCellValue(values[j][k]);  
//                             }  
//                         }  
// //                  }  
//                     cell.setCellStyle(style);  
//                 }  
//             }  
//         }  
        return workbook;  
    } 

     /**  
     * 导出xlsx格式数据(支持导出百万级数据)  
     * @param sheetName  
     * @param title  
     * @param values  
     * @return  
     */  
    public static SXSSFWorkbook getSXSSFWorkbook(String sheetName,String[] title,String[][] values){                 
        // 内存中只创建100个临时对象,超过100个将释放不用对象  
        SXSSFWorkbook wb =new SXSSFWorkbook(100);                 
    // 工作表对象  
        Sheet sheet =null;            
    // 行对象  
        Row row = null;           
    // 列对象  
        Cell cell = null;             
        int rowNo = 0;            
        int pageRowNo = 0;            
        // 表单样式风格             
        CellStyle style = wb.createCellStyle();           
        style.setAlignment(CellStyle.ALIGN_CENTER);               
        row = sheet.createRow(0);                        
        for(int i=0;i<title.length;i++){                      
            cell = row.createCell(i);                         
            cell.setCellValue(title[i]);                      
            cell.setCellStyle(style);                         
            if(i==title.length-1){                           
            sheet.setDefaultColumnWidth(20);                      
            }                         
        }                     
        if(values !=null && values.length>0){              
        for(int j=0;j<values.length;j++){                  
            // 超过一百万行后换一个工作簿,单个sheet最多<span><span class="comment">1048576</span></span>行  
            if(rowNo%1000000 == 0){                                           
                sheet = wb.createSheet("这是我的第"+(rowNo/1000000 +1)+"个工作簿");                      
                sheet = wb.getSheetAt(rowNo/1000000);                       
                pageRowNo = 0;                  
            }                     
            rowNo++;                  
            // 创建标题                   
            if(pageRowNo == 0){                       
            }
            // else{                                
            //     // 创建行  
            //     row = sheet.createRow(pageRowNo);                         
            //     for(int k=0;k<values[j].length;k++){                           
            //         // 创建列  
            //         cell = row.createCell(k);                         
            //         if(k == 0){  
            //             // 对列进行赋值  
            //             cell.setCellValue(Long.parseLong(values[j][k]));                      
            //             }else{                            
            //         if(numType(values[j][k])){                              
            //             cell.setCellValue(Double.parseDouble(values[j][k]));                          
            //         }else{                              
            //             cell.setCellValue(String.valueOf(values[j][k]));                              
            //             }                             
            //         }                         
            //             cell.setCellStyle(style);                         
            //     }                     
            // }                 
            pageRowNo++;              
            }           
        }       
        return wb;      
    }
}