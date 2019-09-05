package com.xyt.resource.dao;

import com.xyt.rescource.model.InfDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：xxj
 * @create 2019-08-26 09:08
 * @function
 * @editLog
 */
@Mapper
public interface InfDictionaryMapper {
    /**
     * 根据标识查询主数据
     *
     * @param signName 标识
     * @return 主数据集合
     */
    List<InfDictionary> getListOfInfDictionary(@Param ("signName") String signName);

    /**
     * 新增主数据
     * @param infDictionary
     * @return
     */
    int saveInfDictionary(InfDictionary infDictionary);

    /**
     * 更新主数据
     * @param infDictionary
     * @return
     */
    int updateInfDictionaryByPrimaryKey(InfDictionary infDictionary);

    /**
     * 删除主数据
     * @param id
     * @param stopSign
     * @return
     */
    int  updateInfDictionaryToDisable(@Param ("id") String id,@Param ("stopSign") int stopSign);

    /**
     * 查询主数据通过相关条件
     * @param signName
     * 标识
     * @param sortNo
     * 序号
     * @return
     */
    InfDictionary  selInfDictionaryByJointCondition(@Param ("signName")String signName,@Param ("contentName")String contentName);

}

