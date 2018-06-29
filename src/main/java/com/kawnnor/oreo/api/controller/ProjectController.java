package com.kawnnor.oreo.api.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.record.WSBoolRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.jni.File;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kawnnor.oreo.api.constant.Constant;
import com.kawnnor.oreo.api.model.FlowDataCount;
import com.kawnnor.oreo.api.model.Project;
import com.kawnnor.oreo.api.model.ProjectBankFlow;
import com.kawnnor.oreo.api.model.ProjectThreeFlow;
import com.kawnnor.oreo.api.model.Result;
import com.kawnnor.oreo.api.service.ProjectFlowService;
import com.kawnnor.oreo.api.service.ProjectService;
import com.kawnnor.oreo.api.util.ExportExcel;
import com.kawnnor.oreo.api.util.ExportUtil;
import com.kawnnor.oreo.api.util.StringUtil;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


@RestController
@RequestMapping("/project")
public class ProjectController{

    @Resource
    private ProjectService projectService;

    @Resource
    private ProjectFlowService projectFlowService;


    @RequestMapping(value="/{id}",method=RequestMethod.GET,produces = "application/json")
    public Project selectById(@PathVariable("id") String id){
        Project project = projectService.select(Integer.parseInt(id));
        System.out.println("进入select....");
        return project;
    }

    @GetMapping(produces = "application/json")
    public List<Project> selectAll(){
        List<Project> list = projectService.selectAll();
        return list;
    }

    @PostMapping(value="/selectProject",produces = "application/json")
    public List<Project> selectProject(@RequestBody Project project){
        System.out.println("-----------selectProject--------:"+project.getName());
        List<Project> list = projectService.select(project);
        return list;
    }



    @PostMapping
    public String insert(@RequestBody Project project){
        System.out.println("进入insert。。。");
        System.out.println(project.getName());
        project.setCreateDate(new Date());
        project.setModifyDate(new Date());
        projectService.insert(project);
        return "success";
    }

    @PutMapping
    public String update(@RequestBody Project project){
        System.out.println("进入update。。。");
        System.out.println("name:"+project.getName());
        project.setModifyDate(new Date());
        projectService.update(project);
        return "success";
    }

    @DeleteMapping(value="/{id}")
    public String delete(@PathVariable("id") String id){
        System.out.println("进入delete。。。");
        projectService.delete(id);
        return "success";
    }

    @PostMapping("/signlefile/{id}")
    public Result importProject(@PathVariable("id") String id,MultipartFile file){
        Result result = new Result();
        Workbook workbook = null;
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getName());
        System.out.println(file.getContentType());
        System.out.println(id);
        try {
            InputStreamReader read = new InputStreamReader(file.getInputStream(),"utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);  
            String line = null;
            int tablenum = 0;
            int rownum = 0;
            StringBuffer xml = new StringBuffer(); //将html内容截取为xml形式的字符串
            Boolean write = false;
            while ((line = bufferedReader.readLine()) != null) { 
                if(line.startsWith("#")){  
                    continue;  
                } 
                //指定字符串判断处  
                if (line.contains("<html")) {
                    System.out.println("找到了");
                    result.setCode(0);
                    result.setMsg("文件与模板样式不符，无法上传！建议您将数据导入模板后上传模板文件。");
                    return result;
                }
            }
            result = readAndSaveExcel(file, id);
            return result;

        } catch (IOException e) {
            result.setCode(0);
            result.setMsg("文件解析出现异常");
            e.printStackTrace();
            return result;
        }
    }
    
    /**
     * 读取excel中的数据保存到数据库中
     */
    public Result readAndSaveExcel(MultipartFile file,String id){
        Result result = new Result();
        Workbook workbook = null;
        try {
            if(file.getOriginalFilename().matches("^.+\\.(?i)(xlsx)$")){
                workbook = new XSSFWorkbook(file.getInputStream());
            }else{
                workbook = new HSSFWorkbook(file.getInputStream());
            }
            int numberOfSheets = workbook.getNumberOfSheets();
            List<ProjectBankFlow> list = new ArrayList();
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                int coloumNum = sheet.getRow(0).getLastCellNum();
                System.out.println("列数："+coloumNum);
                if(sheet.getRow(0)!=null&&sheet.getRow(0).getLastCellNum()==19&&"序号".equals(sheet.getRow(0).getCell(0).getStringCellValue())){

                }else{
                    result.setCode(0);
                    result.setMsg("文件与模板样式不符，无法上传！");
                    return result;
                }
                for(int j=1;j<physicalNumberOfRows;j++){
                    //j=0为标题行
                    ProjectBankFlow projectBankFlow = new ProjectBankFlow();
                    projectBankFlow.setAccount(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(1)), "："));
                    projectBankFlow.setToName(String.valueOf(sheet.getRow(j).getCell(2)));
                    projectBankFlow.setToAccount(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(3)), "："));
                    projectBankFlow.setMoney("-".equals(String.valueOf(sheet.getRow(j).getCell(4)))?null:Double.valueOf(String.valueOf(sheet.getRow(j).getCell(4))));
                    projectBankFlow.setBalance("-".equals(String.valueOf(sheet.getRow(j).getCell(5)))?null:Double.valueOf(String.valueOf(sheet.getRow(j).getCell(5))));
                    projectBankFlow.setBorrowFlag(String.valueOf(sheet.getRow(j).getCell(6)));
                    projectBankFlow.setTransactionType(String.valueOf(sheet.getRow(j).getCell(7)));
                    projectBankFlow.setTransactionResult(String.valueOf(sheet.getRow(j).getCell(8)));
                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date date = null;
                        if(!"".equals(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(9)), "："))
                        &&!"-".equals(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(9)), "："))){
                            date = sdf.parse(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(9)), "："));
                            System.out.println("日期是："+StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(9)), "："));
                        }
                        projectBankFlow.setTransactionDate(date);
                    } catch (ParseException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    DecimalFormat df = new DecimalFormat("0");
                    System.out.println("开户银行原始： "+sheet.getRow(j).getCell(10));
                    if(HSSFCell.CELL_TYPE_STRING==sheet.getRow(j).getCell(10).getCellType()){//字符串类型
                        System.out.println("开户银行转义： "+sheet.getRow(j).getCell(10).getStringCellValue());
                        projectBankFlow.setTransactionBank(sheet.getRow(j).getCell(10).getStringCellValue());
                    }else if(HSSFCell.CELL_TYPE_NUMERIC==sheet.getRow(j).getCell(10).getCellType()){//数字类型
                        System.out.println("开户银行转义： "+df.format(sheet.getRow(j).getCell(10).getNumericCellValue()));
                        projectBankFlow.setTransactionBank(df.format(sheet.getRow(j).getCell(10).getNumericCellValue()));
                    }
                    projectBankFlow.setTransactionOutlets(String.valueOf(sheet.getRow(j).getCell(11)));
                    projectBankFlow.setTransactionNum(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(12)), ";"));
                    System.out.println(String.valueOf(sheet.getRow(j).getCell(13)));
                    projectBankFlow.setVoucherNum(String.valueOf(sheet.getRow(j).getCell(13)));
                    projectBankFlow.setEnding(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(14)),":"));
                    projectBankFlow.setTransactionPlace(String.valueOf(sheet.getRow(j).getCell(15)));
                    projectBankFlow.setBusiness(String.valueOf(sheet.getRow(j).getCell(16)));
                    projectBankFlow.setIp(String.valueOf(sheet.getRow(j).getCell(17)));
                    projectBankFlow.setMac(String.valueOf(sheet.getRow(j).getCell(18)));
                    projectBankFlow.setStatus(Constant.PROJECT_FLOW_PASS);
                    System.out.println("去保存啊");
                    list.add(projectBankFlow);
                    // projectFlowService.saveProjectBankFlow(projectBankFlow, id);
                }
            }
            projectFlowService.saveProjectBankFlow(list, id);
            
        } catch (IOException e) {
            result.setCode(0);
            result.setMsg("导入出现异常，未知错误！");
            e.printStackTrace();
            return result;
        }
        result.setCode(200);
        result.setMsg("success");
        return result;

    }

    public List<List<String>> readHtml(String xml){
        List<List<String>> trList = new ArrayList<List<String>>();
		try {
            //根据传入xml，生成document
			Document document = DocumentHelper.parseText(xml);
			// 获取xml的根元素table
			Element rootElement = document.getRootElement();
			// 迭代器循环，遍历根元素<table>，获取第二级节点《tr》
			for(Iterator<?> i = rootElement.elementIterator(); i.hasNext();){
				// 定义每一行的list，按序保存td的行元素
				List<String> tdList = new ArrayList<String>();
				// 获取第二级节点tr
				Element secondElement = (Element) i.next();
				// 迭代器循环，遍历tr，获取td
				for(Iterator<?> j = secondElement.elementIterator(); j.hasNext();){
					// 获取第三级节点《td》
					Element thirdElement = (Element) j.next();
                    // 将td的内容逐个放到list中国
					tdList.add(thirdElement.getText());
				}
				// 把每行td放到trlist中
				trList.add(tdList);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
        }
        return trList;
    }

    public void saveBankHtml(List<List<String>> list,String id){
        System.out.println("进保存方法了啊");
        System.out.println("数据共"+list.size());
        try {
            List<ProjectBankFlow> lists = new ArrayList();
            for(List<String> flow : list){
                ProjectBankFlow projectBankFlow = new ProjectBankFlow();
                projectBankFlow.setAccount(StringUtil.subStr(flow.get(1), "："));
                projectBankFlow.setToName(flow.get(2));
                projectBankFlow.setToAccount(StringUtil.subStr(flow.get(3), "："));
                projectBankFlow.setMoney(Double.valueOf(flow.get(4)));
                projectBankFlow.setBalance(Double.valueOf(flow.get(5)));
                projectBankFlow.setBorrowFlag(flow.get(6));
                projectBankFlow.setTransactionType(flow.get(7));
                projectBankFlow.setTransactionResult(flow.get(8));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = null;
                if(!"".equals(StringUtil.subStr(flow.get(9), "："))
                &&!"-".equals(StringUtil.subStr(flow.get(9), "："))
                &&!"-".equals(StringUtil.subStr(flow.get(9), "："))){
                    date = sdf.parse(StringUtil.subStr(flow.get(9), "："));
                    System.out.println("日期是："+StringUtil.subStr(flow.get(9), "："));
                }
                projectBankFlow.setTransactionDate(date);
                projectBankFlow.setTransactionBank(flow.get(10));
                projectBankFlow.setTransactionOutlets(flow.get(11));
                projectBankFlow.setTransactionNum(StringUtil.subStr(flow.get(12), ";"));
                projectBankFlow.setVoucherNum(flow.get(13));
                projectBankFlow.setEnding(StringUtil.subStr(flow.get(14),":"));
                projectBankFlow.setTransactionPlace(flow.get(15));
                projectBankFlow.setBusiness(flow.get(16));
                projectBankFlow.setIp(flow.get(17));
                projectBankFlow.setMac(flow.get(18));
                projectBankFlow.setStatus(Constant.PROJECT_FLOW_PASS);
                System.out.println("去保存啊");
                lists.add(projectBankFlow);
            }
            projectFlowService.saveProjectBankFlow(lists, id);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @PostMapping(value="/selectAllBankFlowByProject",produces = "application/json")
    public List<ProjectBankFlow> selectAllBankFlowByProject(@RequestBody ProjectBankFlow projectBankFlow){
        System.out.println("-----------进入selectAllBankFlowByProject-------");
        System.out.println("项目id："+projectBankFlow.getProjectId());
        System.out.println("数据状态："+projectBankFlow.getStatus());
        List<ProjectBankFlow> list = projectFlowService.selectBankFlowByProject(projectBankFlow);
        System.out.println("count:"+list.size());
        return list;
    }

    @PostMapping(value="/selectAllThreeFlowByProject",produces = "application/json")
    public List<ProjectThreeFlow> selectAllThreeFlowByProject(@RequestBody ProjectThreeFlow projectThreeFlow){
        System.out.println("-----------selectAllThreeFlowByProject-------");
        System.out.println("项目id："+projectThreeFlow.getProjectId());
        System.out.println("数据状态："+projectThreeFlow.getStatus());
        List<ProjectThreeFlow> list = projectFlowService.selectThreeFlowByProject(projectThreeFlow);
        System.out.println("count:"+list.size());
        return list;
    }

    /**
     * 流水数据统计
     */
    @GetMapping(value="/getDataCount/{pid}",produces="application/json")
    public Map getDataCount(@PathVariable("pid") String pid){
        Map map = new HashMap();
        ProjectBankFlow projectBankFlow = new ProjectBankFlow();
        projectBankFlow.setProjectId(Integer.valueOf(pid));
        List<FlowDataCount> list = projectFlowService.flowDataCount(projectBankFlow);

        ProjectThreeFlow projectThreeFlow = new ProjectThreeFlow();
        projectThreeFlow.setProjectId(Integer.valueOf(pid));
        List<FlowDataCount> list3 = projectFlowService.threeFlowDataCount(projectThreeFlow);

        System.out.println(pid);
        //银行数据
        int pass = 0;
        int error = 0;
        int unclean = 0;
        //第三方数据
        int pass3 = 0;
        int error3 = 0;
        int unclean3 = 0;
        //统计银行数据
        for(FlowDataCount fdc : list){
            if(Constant.PROJECT_FLOW_PASS.toString().equals(fdc.getStatus())){
                pass = fdc.getCount();
            }else if(Constant.PROJECT_FLOW_ERROR.toString().equals(fdc.getStatus())){
                error = fdc.getCount();
            }else if(Constant.PROJECT_FLOW_UNCLEAN.toString().equals(fdc.getStatus())){
                unclean = fdc.getCount();
            }
            
        }
        //统计第三方数据
        for(FlowDataCount fdc : list3){
            if(Constant.PROJECT_FLOW_PASS.toString().equals(fdc.getStatus())){
                pass3 = fdc.getCount();
            }else if(Constant.PROJECT_FLOW_ERROR.toString().equals(fdc.getStatus())){
                error3 = fdc.getCount();
            }else if(Constant.PROJECT_FLOW_UNCLEAN.toString().equals(fdc.getStatus())){
                unclean3 = fdc.getCount();
            }
            
        }
        int count =  pass+error+unclean;
        int count3 =  pass3+error3+unclean3;

        map.put("successCount", count);
        map.put("errorCount", error);
        map.put("threeSuccessCount", count3);
        map.put("threeErrorCount", error3);
        return map;
    }

    /**
     * 删除银行流水数据
     */
    @DeleteMapping("/deleteBankFlowById/{id}")
    public String deleteBankFlowById(@PathVariable("id") String id){
        System.out.println("------------进入deleteBankFlowById------------");
        ProjectBankFlow projectBankFlow = new ProjectBankFlow();
        projectBankFlow.setId(Integer.parseInt(id));
        projectFlowService.deleteBankFlowById(projectBankFlow);
        return "success";
    }


    /**
     * 删除第三方流水数据
     */
    @DeleteMapping("/deleteThreeFlowById/{id}")
    public String deleteThreeFlowById(@PathVariable("id") String id){
        System.out.println("------------进入deleteBankFlowById------------");
        ProjectThreeFlow projectThreeFlow = new ProjectThreeFlow();
        projectThreeFlow.setId(Integer.parseInt(id));
        projectFlowService.deleteThreeFlowById(projectThreeFlow);
        return "success";
    }

    /**
     * 修改银行流水数据，重新上传
     */
    @PostMapping("/updateBankFlow")
    public String updateBankFlow(@RequestBody ProjectBankFlow projectBankFlow){
        System.out.println("---------进入updateBankFlow---------");
        System.out.println("项目id："+projectBankFlow.getProjectId());
        
        projectBankFlow.setStatus(Constant.PROJECT_FLOW_PASS);

        projectFlowService.updateBankFlow(projectBankFlow);
        return "success";
    }

    /**
     * 修改第三方流水数据，重新上传
     */
    @PostMapping("/updateThreeFlow")
    public String updateThreeFlow(@RequestBody ProjectThreeFlow projectThreeFlow){
        System.out.println("---------进入updateThreeFlow---------");
        System.out.println("项目id："+projectThreeFlow.getProjectId());
        
        projectThreeFlow.setStatus(Constant.PROJECT_FLOW_PASS);

        projectFlowService.updateThreeFlow(projectThreeFlow);
        return "success";
    }

    @PostMapping("/importThreeFlow/{id}")
    public Result importThreeFlow(@PathVariable("id") String id,MultipartFile file){
        Workbook workbook = null;
        Result result = new Result();
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getName());
        System.out.println(file.getContentType());
        System.out.println(id);
        try {
            InputStreamReader read = new InputStreamReader(file.getInputStream(),"utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);  
            String line = null;
            while ((line = bufferedReader.readLine()) != null) { 
                if(line.startsWith("#")){  
                    continue;  
                } 
                //指定字符串判断处  
                if (line.contains("<html")) {
                    System.out.println("找到了");
                    result.setCode(0);
                    result.setMsg("文件与模板样式不符，无法上传！");
                    return result;
                }
            }
            result = readAndSaveThreeExcel(file, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public Result readAndSaveThreeExcel(MultipartFile file,String id){
        Result result = new Result();
        Workbook workbook = null;
        try {
            if(file.getOriginalFilename().matches("^.+\\.(?i)(xlsx)$")){
                workbook = new XSSFWorkbook(file.getInputStream());
            }else{
                workbook = new HSSFWorkbook(file.getInputStream());
            }
            int numberOfSheets = workbook.getNumberOfSheets();
            List<ProjectThreeFlow> list = new ArrayList<>();
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                int coloumNum = sheet.getRow(0).getLastCellNum();
                System.out.println("列数："+coloumNum);
                if(sheet.getRow(0)!=null&&sheet.getRow(0).getLastCellNum()==25&&"序号".equals(sheet.getRow(0).getCell(0).getStringCellValue())){

                }else{
                    result.setCode(0);
                    result.setMsg("文件与模板样式不符，无法上传！");
                    return result;
                }
                for(int j=1;j<physicalNumberOfRows;j++){
                    //j=1为标题行
                    ProjectThreeFlow projectThreeFlow = new ProjectThreeFlow();
                    projectThreeFlow.setTransactionNum(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(1)), "："));
                    projectThreeFlow.setPayAccount(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(2)), "："));
                    projectThreeFlow.setPayBank(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(3)), "："));
                    projectThreeFlow.setPayBankNum(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(4)), "："));
                    projectThreeFlow.setMoney(Double.valueOf(String.valueOf(sheet.getRow(j).getCell(5))));
                    projectThreeFlow.setBalance("-".equals(String.valueOf(sheet.getRow(j).getCell(6)))?null:Double.valueOf(String.valueOf(sheet.getRow(j).getCell(6))));
                    projectThreeFlow.setReceiveAccount(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(7)), "："));
                    projectThreeFlow.setReceiveBank(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(8)), "："));
                    projectThreeFlow.setReceiveBankNum(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(9)), "："));
                    projectThreeFlow.setTransactionType(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(10)), "："));


                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date date = null;
                        if(!"".equals(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(11)), "："))
                        &&!"-".equals(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(11)), "："))){
                            date = sdf.parse(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(11)), "："));
                            System.out.println("日期是："+StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(11)), "："));
                        }
                        projectThreeFlow.setTransactionDate(date);
                    } catch (ParseException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    projectThreeFlow.setPayType(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(12)), "："));
                    projectThreeFlow.setOutIn(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(13)), "："));
                    projectThreeFlow.setMoneyType(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(14)), "："));
                    projectThreeFlow.setPosNum(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(15)), "："));
                    projectThreeFlow.setBusiness(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(16)), "："));
                    projectThreeFlow.setEquipment(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(17)), "："));
                    projectThreeFlow.setIp(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(18)), "："));
                    projectThreeFlow.setMac(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(19)), "："));
                    projectThreeFlow.setLongitude(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(20)), "："));
                    projectThreeFlow.setLatitude(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(21)), "："));
                    projectThreeFlow.setEquipmentNum(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(22)), "："));
                    projectThreeFlow.setBankTransactionNum(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(23)), "："));
                    projectThreeFlow.setNote(StringUtil.subStr(String.valueOf(sheet.getRow(j).getCell(24)), "："));


                    projectThreeFlow.setStatus(Constant.PROJECT_FLOW_PASS);
                    System.out.println("去保存啊");
                    list.add(projectThreeFlow);
                    // projectFlowService.saveProjectBankFlow(projectBankFlow, id);

                }
            }
            projectFlowService.saveProjectThreeFlow(list, id);
            
        } catch (IOException e) {
            result.setCode(0);
            result.setMsg("导入出现异常，未知错误！");
            e.printStackTrace();
            return result;
        }
        result.setCode(200);
        result.setMsg("success");
        return result;

    }

    public void saveThreeHtml(List<List<String>> list,String id){
        System.out.println("进第三方保存方法了啊");
        System.out.println("数据共"+list.size());
        try {
            List<ProjectThreeFlow> lists = new ArrayList<>();
            for(List<String> flow : list){
                ProjectThreeFlow projectThreeFlow = new ProjectThreeFlow();
                projectThreeFlow.setTransactionNum(StringUtil.subStr(flow.get(1), "："));
                projectThreeFlow.setPayAccount(StringUtil.subStr(flow.get(2), "："));
                projectThreeFlow.setPayBank(StringUtil.subStr(flow.get(3), "："));
                projectThreeFlow.setPayBankNum(StringUtil.subStr(flow.get(4), "："));
                projectThreeFlow.setMoney("-".equals(flow.get(5))?null:Double.valueOf(flow.get(5)));
                projectThreeFlow.setBalance("-".equals(flow.get(6))?null:Double.valueOf(flow.get(6)));
                projectThreeFlow.setReceiveAccount(StringUtil.subStr(flow.get(7), "："));
                projectThreeFlow.setReceiveBank(flow.get(8));
                projectThreeFlow.setReceiveBankNum(StringUtil.subStr(flow.get(9), "："));
                projectThreeFlow.setTransactionType(flow.get(10));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = null;
                if(!"".equals(StringUtil.subStr(flow.get(11), "："))
                &&!"-".equals(StringUtil.subStr(flow.get(11), "："))
                &&!"-".equals(StringUtil.subStr(flow.get(11), "："))){
                    date = sdf.parse(StringUtil.subStr(flow.get(11), "："));
                    System.out.println("日期是："+StringUtil.subStr(flow.get(11), "："));
                }
                projectThreeFlow.setTransactionDate(date);
                projectThreeFlow.setPayType(flow.get(12));
                projectThreeFlow.setOutIn(flow.get(13));
                projectThreeFlow.setMoneyType(flow.get(14));
                projectThreeFlow.setPosNum(StringUtil.subStr(flow.get(15), "："));
                projectThreeFlow.setBusiness(StringUtil.subStr(flow.get(16), "："));
                projectThreeFlow.setEquipment(flow.get(17));
                projectThreeFlow.setIp(StringUtil.subStr(flow.get(18), "："));
                projectThreeFlow.setMac(StringUtil.subStr(flow.get(19), "："));
                projectThreeFlow.setLongitude(StringUtil.subStr(flow.get(20), "："));
                projectThreeFlow.setLatitude(StringUtil.subStr(flow.get(21), "："));
                projectThreeFlow.setEquipmentNum(StringUtil.subStr(flow.get(22), "："));
                projectThreeFlow.setBankTransactionNum(StringUtil.subStr(flow.get(23), "："));
                projectThreeFlow.setNote(flow.get(24));
                projectThreeFlow.setStatus(Constant.PROJECT_FLOW_PASS);
                System.out.println("去保存啊");
                lists.add(projectThreeFlow);
            }
            projectFlowService.saveProjectThreeFlow(lists, id);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }



        /**  
     * 测试导出Excel文件  
     * @throws Exception  
     */  
    @GetMapping("/downLoadBank")
    public OutputStream downLoadBank(HttpServletResponse response) throws Exception{  
        System.out.println("-------------开始下载银行模板--------------");
        String sheetName = "导出Excel";  
        String fileName = "银行流水模板.xls";  
        String[] title = new String[]{
            "序号","查询账号","对方账号姓名","对方账号卡号","金额",
            "余额","借贷标志","交易类型","交易结果","时间",
            "交易开户行","交易网点名称","交易流水号","凭证号","终端号",
            "交易发生地","商户名称","IP地址","MAC地址"}; // 显示列  
        String[][] values=null;  
          
        // 导出xls格式  
        HSSFWorkbook workbook =ExportUtil.getHssFWorkbook(sheetName, title, values);  
               // 导出xlsx格式  
               // SXSSFWorkbook workbook =ExportUtil.getSXSSFWorkbook(sheetName, title, values);  
                // 网页端弹出下载框，将文件保存到指定位置  
        OutputStream os=response.getOutputStream();  
        try {  
            this.setResponseHeader(response,fileName);  
            workbook.write(os);  
            os.flush();  
            os.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return os;
    } 
    
    
    @GetMapping("/downLoadThree")
    public OutputStream downLoadThree(HttpServletResponse response) throws Exception{  
        System.out.println("-------------开始下载第三方流水模板--------------");
        String sheetName = "导出Excel";  
        String fileName = "第三方流水模板.xls";  
        String[] title = new String[]{
            "序号","交易流水号","付款支付账号","付款银行卡银行名称","付款银行卡号",
            "交易金额","交易余额","收款支付账号","收款银行卡银行名称","收款银行卡号",
            "交易类型","交易时间","支付类型","交易主体的出入账标识","币种",
            "消费POS机编号","收款方的商户号","交易设备类型","交易支付设备IP","MAC地址",
            "交易地点经度","交易地点纬度","交易设备号","银行外部取道交易流水号","备注"
        }; // 显示列  
        String[][] values=null;  
          
        // 导出xls格式  
        HSSFWorkbook workbook =ExportUtil.getHssFWorkbook(sheetName, title, values);  
               // 导出xlsx格式  
               // SXSSFWorkbook workbook =ExportUtil.getSXSSFWorkbook(sheetName, title, values);  
                // 网页端弹出下载框，将文件保存到指定位置  
        OutputStream os=response.getOutputStream();  
        try {  
            this.setResponseHeader(response,fileName);  
            workbook.write(os);  
            os.flush();  
            os.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return os;
    } 





    /**
     * 设置返回浏览器头文件信息
     */
    private void setResponseHeader(HttpServletResponse response, String fileName) {  
        try {  
            try {  
                fileName = new String(fileName.getBytes(),"UTF-8");  
            } catch (UnsupportedEncodingException e) {  
                 e.printStackTrace();  
            }  
              
            response.setContentType("application/octet-stream;charset=UTF-8");  
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);  
            response.addHeader("Pargam", "no-cache");  
            response.addHeader("Cache-Control", "no-cache");  
              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
    } 


    /**
     * 银行数据分析
     */
    @GetMapping(value="/bankDateShow/{id}",produces = "application/json")
    public Map BankDateShow(@PathVariable String id){
        System.out.println("------------------查询开始--------------");
        ProjectBankFlow projectBankFlow = new ProjectBankFlow();
        projectBankFlow.setProjectId(Integer.parseInt(id));
        projectBankFlow.setStatus(Constant.PROJECT_FLOW_PASS);
        Map<String,List<ProjectBankFlow>> map = new HashMap<>();
        List<ProjectBankFlow> list = projectFlowService.selectBankFlowByProject(projectBankFlow);
        List<ProjectBankFlow> list2 = projectFlowService.selectBankDataShow(projectBankFlow);
        map.put("node", list2);
        map.put("edge", list);
        return map;
    }

    /**
     * 银行数据分析---查询项目所有初始节点
     */
    @PostMapping(value="/bankInitialNode",produces = "application/json")
    public List<ProjectBankFlow> selectBankInitialNode(@RequestBody ProjectBankFlow projectBankFlow){
        System.out.println("------------------查询项目所有初始节点selectBankInitialNode--------------");
        List<ProjectBankFlow> list = projectFlowService.selectBankInitialNode(projectBankFlow);
        return list;
    }

    /**
     * 银行数据分析---查询项目节点下数据
     */
    @PostMapping(value="/selectBankDataAfterDate",produces = "application/json")
    public Result selectBankDataAfterDate(@RequestBody ProjectBankFlow projectBankFlow){
        System.out.println("------------------查询项目节点下数据selectBankDataAfterDate--------------");
        System.out.println("传值日期："+projectBankFlow.getTransactionDateStrStart());
        System.out.println("account: "+projectBankFlow.getAccount());
        System.out.println("toAccount: "+projectBankFlow.getToAccount());
        Result results = new Result();
        List<List<ProjectBankFlow>> result = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar Cal=java.util.Calendar.getInstance();  
        int intervals = projectBankFlow.getIntervals()==null?24:projectBankFlow.getIntervals();
        int level = projectBankFlow.getLevel()==null?5:projectBankFlow.getLevel();
        try {
            Date start = new Date();
            if(projectBankFlow.getTransactionDateStrStart()==null){
                List<ProjectBankFlow> temp = projectFlowService.selectBankEarliestByAccount(projectBankFlow);
                start = temp.get(0).getTransactionDate();
            }else{
                start = sdf.parse(projectBankFlow.getTransactionDateStrStart());
            }
            results.setMsg(sdf.format(start));
            System.out.println(sdf.format(start));
            for(int i=1;i<=level;i++){
                projectBankFlow.setTransactionDateStrStart(String.valueOf(start.getTime()));
                System.out.println(String.valueOf(start.getTime()));
                System.out.println(sdf.format(start.getTime()));
                Cal.setTime(start);
                Cal.add(java.util.Calendar.HOUR_OF_DAY,intervals); 
                Date end = Cal.getTime();
                projectBankFlow.setTransactionDateStrEnd(String.valueOf(end.getTime()));
                System.out.println(String.valueOf(end.getTime()));
                List<ProjectBankFlow> list = projectFlowService.selectBankDataAfterDate(projectBankFlow);
                System.out.println("原："+list.size());
                list = cleanDate(list,list);
                System.out.println("现："+list.size());
                result.add(list);
                start = end;
                System.out.println(list.size());
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        results.setData(result);
        return results;
    }

    /**
     * 银行数据分析---查询某帐号节点某时间段内银行流水数据
     */
    @PostMapping(value="/selectBankFlow",produces = "application/json")
    public List<ProjectBankFlow> selectBankFlow(@RequestBody ProjectBankFlow projectBankFlow){
        System.out.println("------------------查询帐号节点银行流水数据selectBankFlow--------------");
        System.out.println(projectBankFlow.getToAccount());
        System.out.println(projectBankFlow.getTransactionDateStrStart());
        System.out.println(projectBankFlow.getIntervals());
        System.out.println(projectBankFlow.getLevel());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar Cal=java.util.Calendar.getInstance();  
        int intervals = projectBankFlow.getIntervals()==null?24:projectBankFlow.getIntervals();
        int level = projectBankFlow.getLevel()==null?5:projectBankFlow.getLevel();
        try {
            Date start = sdf.parse(projectBankFlow.getTransactionDateStrStart());
            Cal.setTime(start);
            Cal.add(java.util.Calendar.HOUR_OF_DAY,intervals*level); 
            start = Cal.getTime();
            projectBankFlow.setTransactionDateStrStart(String.valueOf(start.getTime()));
            Cal.add(java.util.Calendar.HOUR_OF_DAY,intervals);
            Date end = Cal.getTime();
            projectBankFlow.setTransactionDateStrEnd(String.valueOf(end.getTime()));
            System.out.println(projectBankFlow.getTransactionDateStrStart());
            System.out.println(projectBankFlow.getTransactionDateStrEnd());
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("111");
        }
        projectBankFlow.setAccount(null);
        List<ProjectBankFlow> list = projectFlowService.selectBankFlow(projectBankFlow);
        System.out.println(list.size());
        
        return list;
    }

    /**
     * 银行资金转出统计---绘制表格
     */
    @PostMapping(value="/selectBankTablOut",produces = "application/json")
    public Map selectBankTablOut(@RequestBody ProjectBankFlow projectBankFlow){
        System.out.println("------------------银行资金转出统计--绘制表格selectBankTablOut--------------");
        System.out.println("传值日期："+projectBankFlow.getTransactionDateStrStart());
        System.out.println("account: "+projectBankFlow.getAccount());
        System.out.println("toAccount: "+projectBankFlow.getToAccount());
        Map map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar Cal=java.util.Calendar.getInstance(); 
        //间隔时间 
        int intervals = projectBankFlow.getIntervals()==null?24:projectBankFlow.getIntervals();
        //展示等级
        int level = projectBankFlow.getLevel()==null?8:projectBankFlow.getLevel();
        try {
            Date start = new Date();
            if(projectBankFlow.getTransactionDateStrStart()==null){
                List<ProjectBankFlow> temp = projectFlowService.selectBankEarliestByAccount(projectBankFlow);
                start = temp.get(0).getTransactionDate();
            }else{
                start = sdf.parse(projectBankFlow.getTransactionDateStrStart());
            }

            Cal.setTime(start);
            Cal.add(java.util.Calendar.HOUR_OF_DAY,intervals*level);
            Date end = Cal.getTime();
            projectBankFlow.setTransactionDateStrStart(String.valueOf(start.getTime()));
            projectBankFlow.setTransactionDateStrEnd(String.valueOf(end.getTime()));
            List<ProjectBankFlow> toAccounts = projectFlowService.selectBankOutToAccountByDate(projectBankFlow);
            List<Map> label = new ArrayList();
            for(int i=0;i<toAccounts.size();i++){
                Map m = new HashMap<>();
                m.put("toAccount", toAccounts.get(i).getToAccount());
                m.put("toName", toAccounts.get(i).getToName());
                label.add(i,m);
            }
            map.put("label", label);  //返回对方账号集合，作为表格表头
            
            List<Map> dataResult = new ArrayList<>();
            for(int i=1;i<=level;i++){
                projectBankFlow.setTransactionDateStrStart(String.valueOf(start.getTime()));
                Cal.setTime(start);
                Cal.add(java.util.Calendar.HOUR_OF_DAY,intervals); 
                Date endTemp = Cal.getTime();
                projectBankFlow.setTransactionDateStrEnd(String.valueOf(endTemp.getTime()));
                System.out.println(String.valueOf(endTemp.getTime()));
                projectBankFlow.setBorrowFlag("借");
                List<ProjectBankFlow> data = projectFlowService.selectBankDataForTable(projectBankFlow);
                Map time = new HashMap<>();
                time.put("时间", sdf.format(start));
                for(int j=0;j<toAccounts.size();j++){
                    List<String> t = findOutMoneyCount(toAccounts.get(j).getToAccount(),data);
                    time.put(toAccounts.get(j).getToAccount(), t.get(1)+"元 -- "+t.get(0)+"次");
                }
                dataResult.add( time);
                System.out.println(sdf.format(start));
                System.out.println(sdf.format(endTemp));
                start = endTemp;
            }
            map.put("data", dataResult);
        } catch (Exception e) {
            //TODO: handle exception
        }
        return map;
    }

    /**
     * 银行数据表格分析---查询时间段内向某账号转出资金具体明细
     */
    @PostMapping(value="/selectBankOutInfo",produces = "application/json")
    public List<ProjectBankFlow> selectBankOutInfo(@RequestBody ProjectBankFlow projectBankFlow){
        System.out.println("------------------银行数据表格分析详细selectBankOutInfo--------------");
        System.out.println(projectBankFlow.getToAccount());
        System.out.println(projectBankFlow.getTransactionDateStrStart());
        System.out.println(projectBankFlow.getIntervals());
        System.out.println(projectBankFlow.getLevel());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar Cal=java.util.Calendar.getInstance();  
        int intervals = projectBankFlow.getIntervals()==null?24:projectBankFlow.getIntervals();
        try {
            Date start = sdf.parse(projectBankFlow.getTransactionDateStrStart());
            Cal.setTime(start);
            Cal.add(java.util.Calendar.HOUR_OF_DAY,intervals); 
            Date end = Cal.getTime();
            projectBankFlow.setTransactionDateStrStart(String.valueOf(start.getTime()));
            projectBankFlow.setTransactionDateStrEnd(String.valueOf(end.getTime()));
            System.out.println(projectBankFlow.getTransactionDateStrStart());
            System.out.println(projectBankFlow.getTransactionDateStrEnd());
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("111");
        }
        List<ProjectBankFlow> list = projectFlowService.selectBankOutInfo(projectBankFlow);
        System.out.println(list.size());
        
        return list;
    }


    /**
     * 银行数据分析---数据明细列表
     */
    @PostMapping(value="/selectBankFlowList",produces = "application/json")
    public List<ProjectBankFlow> selectBankFlowList(@RequestBody ProjectBankFlow projectBankFlow){
        System.out.println("------------------银行数据明细列表selectBankFlowList--------------");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(StringUtils.isNotBlank(projectBankFlow.getTransactionDateStrStart())){
                System.out.println(projectBankFlow.getTransactionDateStrStart());
                projectBankFlow.setTransactionDateStrStart(String.valueOf(sdf.parse(projectBankFlow.getTransactionDateStrStart()).getTime()));
            }
            if(StringUtils.isNotBlank(projectBankFlow.getTransactionDateStrEnd())){
                projectBankFlow.setTransactionDateStrEnd(String.valueOf(sdf.parse(projectBankFlow.getTransactionDateStrEnd()).getTime()));
            }
            
        } catch (Exception e) {
            //TODO: handle exception
        }
        List<ProjectBankFlow> list = projectFlowService.selectBankFlow(projectBankFlow);
        return list;
    }








    


    //获取集合中对方帐号为toAccount的金额和转账次数
    public List<String> findOutMoneyCount(String toAccount,List<ProjectBankFlow> list){
        List result = new ArrayList<>();
        result.add(0, "0");
        result.add(1, "0");
        for(ProjectBankFlow projectBankFlow : list){
            if(projectBankFlow.getToAccount().equals(toAccount)){
                result.set(0, projectBankFlow.getCount().toString());
                result.set(1, projectBankFlow.getTotalMoney().toString());
            }
        }
        return result;
    }

    public List<ProjectBankFlow> cleanDate(List<ProjectBankFlow> list1,List<ProjectBankFlow> list2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean flag = false;
        int size = list2.size();
        for(int j = list1.size()-1;j>=0;j--){
            for(int i = 0;i<size;i++){
                System.out.println("size1   " +size);
                try {
                    if(sdf.format(list1.get(j).getTransactionDate()).equals(sdf.format(list2.get(i).getTransactionDate())) && 
                    list1.get(j).getCount().toString().equals(list2.get(i).getCount().toString()) && 
                    list1.get(j).getTotalMoney().toString().equals(list2.get(i).getTotalMoney().toString()) && 
                    list1.get(j).getAccount().equals(list2.get(i).getToAccount())){
                        flag = true;
                    }
                    
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            if(flag){
                list2.remove(list1.get(j));
                System.out.println(list2.size());
                flag = false;
                size--;
            }
            System.out.println("size2    "+size);
        }
        return list2;
    }


}