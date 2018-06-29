package com.kawnnor.oreo.api.mapper;


import java.util.List;

import com.kawnnor.oreo.api.model.FlowDataCount;
import com.kawnnor.oreo.api.model.ProjectBankFlow;
import com.kawnnor.oreo.api.model.ProjectThreeFlow;

public interface ProjectFlowMapper{

    List<ProjectBankFlow> selectProjectFlow(ProjectBankFlow projectBankFlow);

    
    List<ProjectThreeFlow> selectProjectThreeFlow(ProjectThreeFlow projectBankFlow);
    
    int saveProjectBankFlowTemp(ProjectBankFlow projectBankFlow);
    
    List<FlowDataCount> selectFlowDataCount (ProjectBankFlow projectBankFlow);
    
    int deleteBankFlowById (ProjectBankFlow projectBankFlow);
    
    int deleteThreeFlowById (ProjectThreeFlow projectThreeFlow);
    
    int saveProjectThreeFlowTemp(ProjectThreeFlow projectThreeFlow);
    
    List<FlowDataCount> selectThreeFlowDataCount (ProjectThreeFlow projectThreeFlow);
    
    //---------------银行数据分析--------------

    //查询项目中所有节点
    List<ProjectBankFlow> selectBankDataShow(ProjectBankFlow projectBankFlow);

    //查询项目中所有初始节点,并按数据量排序
    List<ProjectBankFlow> selectBankInitialNode(ProjectBankFlow projectBankFlow);

    //查询某账号节点最早的一条记录
    List<ProjectBankFlow> selectBankEarliestByAccount(ProjectBankFlow projectBankFlow);

    //查询某账号节点第一次发生交易后发生交易的记录
    List<ProjectBankFlow> selectBankDataAfterDate(ProjectBankFlow projectBankFlow);

    //查询正式表中银行数据
    List<ProjectBankFlow> selectBankFlow(ProjectBankFlow projectBankFlow);

    //---------------------银行转出表格绘制------------------

    //查询项目中对方帐号
    List<ProjectBankFlow> selectBankOutToAccountByDate(ProjectBankFlow projectBankFlow);

    //查询转出记录
    List<ProjectBankFlow> selectBankDataForTable(ProjectBankFlow projectBankFlow);

    //查询转出详情
    List<ProjectBankFlow> selectBankOutInfo(ProjectBankFlow projectBankFlow);


}
