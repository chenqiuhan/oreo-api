package com.kawnnor.oreo.api.service;

import java.util.List;

import javax.annotation.Resource;

import com.kawnnor.oreo.api.mapper.ProjectMapper;
import com.kawnnor.oreo.api.model.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProjectService{
    @Autowired
    private ProjectMapper projectMapper;


    public Project select(int id) {
        return projectMapper.select(id);
    }

    public List<Project> select(Project project) {
        return projectMapper.selectProject(project);
    }


    public List<Project> selectAll(){
        return projectMapper.selectAll();
    }


    public int insert(Project project){
        return projectMapper.insert(project);
    }

    public int update (Project project){
        return projectMapper.update(project);
    }

    public int delete(String id){
        return projectMapper.delete(id);
    }
}