package com.kawnnor.oreo.api.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.kawnnor.oreo.api.constant.Constant;
import com.kawnnor.oreo.api.mapper.ProjectFlowMapper;
import com.kawnnor.oreo.api.model.FlowDataCount;
import com.kawnnor.oreo.api.model.ProjectBankFlow;
import com.kawnnor.oreo.api.model.ProjectThreeFlow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectFlowService{

    @Autowired
    private ProjectFlowMapper projectFlowMapper;

    @Transactional
    public void saveProjectBankFlow(List<ProjectBankFlow> list,String id){
        for (ProjectBankFlow projectBankFlow : list) {
            projectBankFlow.setProjectId(Integer.valueOf(id));
            
            if("-".equals(projectBankFlow.getToName()) || 
                "".equals(projectBankFlow.getToName()) ||
                projectBankFlow.getToName().indexOf("*")!=-1 || 
                "-".equals(projectBankFlow.getToAccount()) ||
                "".equals(projectBankFlow.getToAccount()) ){
                projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectBankFlow.setRemark("对方账户信息不完整");
                System.out.println("账户信息不完整");
            }else if("-".equals(projectBankFlow.getAccount())||
                "".equals(projectBankFlow.getAccount())||
                projectBankFlow.getAccount().indexOf("*")!=-1){
                projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectBankFlow.setRemark("查询账号信息不完整");
            }else if(projectBankFlow.getTransactionDate()==null){
                projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectBankFlow.setRemark("交易时间数据不正确");
                System.out.println("无效数据");
            }else if(projectBankFlow.getMoney()==null){
                projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectBankFlow.setRemark("交易金额不能为空");
                System.out.println("无效数据");
            }else if(projectBankFlow.getBalance()==null){
                projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectBankFlow.setRemark("余额不能为空");
                System.out.println("无效数据");
            }else if("".equals(projectBankFlow.getBorrowFlag()) || "-".equals(projectBankFlow.getBorrowFlag())){
                projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectBankFlow.setRemark("借贷标志不能为空");
                System.out.println("无效数据");
            }else if("".equals(projectBankFlow.getTransactionResult()) || "-".equals(projectBankFlow.getTransactionResult())){
                projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectBankFlow.setRemark("交易结果不能为空");
                System.out.println("无效数据");
            }
            
            if(projectBankFlow.getStatus()!=Constant.PROJECT_FLOW_ERROR){
                if(projectBankFlow.getTransactionDate()!=null){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    projectBankFlow.setTransactionDateStr(String.valueOf(projectBankFlow.getTransactionDate().getTime()));
                    System.out.println(String.valueOf(projectBankFlow.getTransactionDate().getTime()));
                }
                List<ProjectBankFlow> lists = projectFlowMapper.selectProjectFlow(projectBankFlow);
                if(lists.size()>0){
                    projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                    projectBankFlow.setRemark("重复数据");
                }
            }
            projectBankFlow.setCreateDate(new Date());
            System.out.println("保存吧，没毛病");
            projectFlowMapper.saveProjectBankFlowTemp(projectBankFlow);
        }

    }

    public List<ProjectBankFlow> selectBankFlowByProject(ProjectBankFlow projectBankFlow){
        List<ProjectBankFlow> list = projectFlowMapper.selectProjectFlow(projectBankFlow);
        return list;
    }

    public List<ProjectThreeFlow> selectThreeFlowByProject(ProjectThreeFlow projectThreeFlow){
        List<ProjectThreeFlow> list = projectFlowMapper.selectProjectThreeFlow(projectThreeFlow);
        return list;
    }

    public List<FlowDataCount> flowDataCount(ProjectBankFlow projectBankFlow){
        return projectFlowMapper.selectFlowDataCount(projectBankFlow);
    }

    public void deleteBankFlowById(ProjectBankFlow projectBankFlow){
        projectFlowMapper.deleteBankFlowById(projectBankFlow);
    }

    public void deleteThreeFlowById(ProjectThreeFlow projectThreeFlow){
        projectFlowMapper.deleteThreeFlowById(projectThreeFlow);
    }

    public void updateBankFlow(ProjectBankFlow projectBankFlow){
        if("-".equals(projectBankFlow.getToName()) || 
            "".equals(projectBankFlow.getToName()) ||
            projectBankFlow.getToName().indexOf("*")!=-1 || 
            "-".equals(projectBankFlow.getToAccount()) ||
            "".equals(projectBankFlow.getToAccount()) ){
            projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
            projectBankFlow.setRemark("对方账户信息不完整");
            System.out.println("账户信息不完整");
        }else if("-".equals(projectBankFlow.getAccount())||
            "".equals(projectBankFlow.getAccount())||
            projectBankFlow.getAccount().indexOf("*")!=-1){
            projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
            projectBankFlow.setRemark("查询账号信息不完整");
        }else if(projectBankFlow.getTransactionDate()==null){
            projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
            projectBankFlow.setRemark("交易时间数据不正确");
            System.out.println("无效数据");
        }else if(projectBankFlow.getMoney()==null){
            projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
            projectBankFlow.setRemark("交易金额不能为空");
            System.out.println("无效数据");
        }else if(projectBankFlow.getBalance()==null){
            projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
            projectBankFlow.setRemark("余额不能为空");
            System.out.println("无效数据");
        }else if("".equals(projectBankFlow.getBorrowFlag()) || "-".equals(projectBankFlow.getBorrowFlag())){
            projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
            projectBankFlow.setRemark("借贷标志不能为空");
            System.out.println("无效数据");
        }else if("".equals(projectBankFlow.getTransactionResult()) || "-".equals(projectBankFlow.getTransactionResult())){
            projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
            projectBankFlow.setRemark("交易结果不能为空");
            System.out.println("无效数据");
        }

        if(projectBankFlow.getStatus()!=Constant.PROJECT_FLOW_ERROR){
            if(projectBankFlow.getTransactionDate()!=null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                projectBankFlow.setTransactionDateStr(String.valueOf(projectBankFlow.getTransactionDate().getTime()));
                System.out.println(String.valueOf(projectBankFlow.getTransactionDate().getTime()));
            }
            List<ProjectBankFlow> list = projectFlowMapper.selectProjectFlow(projectBankFlow);
            if(list.size()>0){
                projectBankFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectBankFlow.setRemark("重复数据");
            }
        }
        projectFlowMapper.deleteBankFlowById(projectBankFlow);
        projectBankFlow.setId(null);
        projectBankFlow.setCreateDate(new Date());
        projectFlowMapper.saveProjectBankFlowTemp(projectBankFlow);
    }

    public void updateThreeFlow(ProjectThreeFlow projectThreeFlow){
        if(projectThreeFlow.getTransactionNum()==null
                ||projectThreeFlow.getTransactionNum().length()==0
                ||projectThreeFlow.getTransactionNum().indexOf("-")!=-1){
                    projectThreeFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                    projectThreeFlow.setRemark("交易流水号异常");
                }
            if(projectThreeFlow.getTransactionDate()==null){
                projectThreeFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectThreeFlow.setRemark("交易时间异常");
            }
    
            if(projectThreeFlow.getStatus()!=Constant.PROJECT_FLOW_ERROR){
                ProjectThreeFlow temp = new ProjectThreeFlow();
                temp.setProjectId(projectThreeFlow.getProjectId());
                temp.setStatus(projectThreeFlow.getStatus());
                temp.setTransactionNum(projectThreeFlow.getTransactionNum());
                if(projectThreeFlow.getTransactionDate()!=null){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    projectThreeFlow.setTransactionDateStr(String.valueOf(projectThreeFlow.getTransactionDate().getTime()));
                    System.out.println(String.valueOf(projectThreeFlow.getTransactionDate().getTime()));
                    temp.setTransactionDateStr(projectThreeFlow.getTransactionDateStr());
                }
                //通过交易流水号和交易时间，判断有无重复数据
                List<ProjectThreeFlow> lists = projectFlowMapper.selectProjectThreeFlow(temp);
                if(lists.size()>0){
                    projectThreeFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                    projectThreeFlow.setRemark("重复数据");
                }
            }
        projectFlowMapper.deleteThreeFlowById(projectThreeFlow);
        projectThreeFlow.setId(null);
        projectThreeFlow.setCreateDate(new Date());
        projectFlowMapper.saveProjectThreeFlowTemp(projectThreeFlow);
    }


    /**
     * 保存第三方项目流水（支付宝、财付通）
     */
    @Transactional
    public void saveProjectThreeFlow(List<ProjectThreeFlow> list,String id){
        for(ProjectThreeFlow projectThreeFlow : list){

            projectThreeFlow.setProjectId(Integer.valueOf(id));
            
            if(projectThreeFlow.getTransactionNum()==null
                ||projectThreeFlow.getTransactionNum().length()==0
                ||projectThreeFlow.getTransactionNum().indexOf("-")!=-1){
                    projectThreeFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                    projectThreeFlow.setRemark("交易流水号异常");
                }
            if(projectThreeFlow.getTransactionDate()==null){
                projectThreeFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                projectThreeFlow.setRemark("交易时间异常");
            }
    
            if(projectThreeFlow.getStatus()!=Constant.PROJECT_FLOW_ERROR){
                ProjectThreeFlow temp = new ProjectThreeFlow();
                temp.setProjectId(Integer.valueOf(id));
                temp.setStatus(projectThreeFlow.getStatus());
                temp.setTransactionNum(projectThreeFlow.getTransactionNum());
                if(projectThreeFlow.getTransactionDate()!=null){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    projectThreeFlow.setTransactionDateStr(String.valueOf(projectThreeFlow.getTransactionDate().getTime()));
                    System.out.println(String.valueOf(projectThreeFlow.getTransactionDate().getTime()));
                    temp.setTransactionDateStr(projectThreeFlow.getTransactionDateStr());
                }
                //通过交易流水号和交易时间，判断有无重复数据
                List<ProjectThreeFlow> lists = projectFlowMapper.selectProjectThreeFlow(temp);
                if(lists.size()>0){
                    projectThreeFlow.setStatus(Constant.PROJECT_FLOW_ERROR);
                    projectThreeFlow.setRemark("重复数据");
                }
            }
            projectThreeFlow.setCreateDate(new Date());
            System.out.println("保存吧，没毛病");
            projectFlowMapper.saveProjectThreeFlowTemp(projectThreeFlow);
        }

    }

    public List<FlowDataCount> threeFlowDataCount(ProjectThreeFlow projectThreeFlow){
        return projectFlowMapper.selectThreeFlowDataCount(projectThreeFlow);
    }

    public List<ProjectBankFlow> selectBankDataShow(ProjectBankFlow projectBankFlow){
        return projectFlowMapper.selectBankDataShow(projectBankFlow);
    }

    public List<ProjectBankFlow> selectBankInitialNode(ProjectBankFlow projectBankFlow){
        return projectFlowMapper.selectBankInitialNode(projectBankFlow);
    }

    public List<ProjectBankFlow> selectBankDataAfterDate(ProjectBankFlow projectBankFlow){
        return projectFlowMapper.selectBankDataAfterDate(projectBankFlow);
    }

    public List<ProjectBankFlow> selectBankFlow(ProjectBankFlow projectBankFlow){
        return projectFlowMapper.selectBankFlow(projectBankFlow);
    }

    public List<ProjectBankFlow> selectBankEarliestByAccount(ProjectBankFlow projectBankFlow){
        return projectFlowMapper.selectBankEarliestByAccount(projectBankFlow);
    }

    public List<ProjectBankFlow> selectBankOutToAccountByDate(ProjectBankFlow projectBankFlow){
        return projectFlowMapper.selectBankOutToAccountByDate(projectBankFlow);
    }

    public List<ProjectBankFlow> selectBankDataForTable(ProjectBankFlow projectBankFlow){
        return projectFlowMapper.selectBankDataForTable(projectBankFlow);
    }

    public List<ProjectBankFlow> selectBankOutInfo(ProjectBankFlow projectBankFlow){
        return projectFlowMapper.selectBankOutInfo(projectBankFlow);
    }
}