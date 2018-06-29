package com.kawnnor.oreo.api.mapper;

import java.util.List;

import com.kawnnor.oreo.api.model.Project;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface ProjectMapper{
    // 根据 ID 查询
    @Select("SELECT * FROM project WHERE id=#{id}")
    Project select(int id);
    
    List<Project> selectProject(Project project);

    @Select("SELECT * FROM project order by createDate desc")
    List<Project> selectAll();

    @Insert("INSERT INTO project(name,instructions,createDate,modifyDate) VALUES(#{name},#{instructions},#{createDate},#{modifyDate})")
    int insert(Project project);

    @Update("UPDATE project SET name=#{name},instructions=#{instructions},modifyDate=#{modifyDate} WHERE id=#{id}")
    int update(Project project);

    @Delete("DELETE FROM project WHERE id IN (#{id})")
    int delete(String id);
}