package com.xyt.resource.service;

import com.xyt.rescource.model.InfDictionary;

import java.text.ParseException;
import java.util.List;

/**
 * @author ：xxj
 * @create 2019-08-26 09:08
 * @function
 * @editLog
 */
public interface InfDictionaryService {
    /**
     * 根据标识查询主数据
     *
     * @param signName 标识
     * @return 主数据集合
     */
    List<InfDictionary> getListOfInfDictionary(String signName) throws ParseException, ClassNotFoundException;

    /**
     * 保存主数据
     * @param infDictionary
     * @return
     */
     int saveInfDictionary(InfDictionary infDictionary);

    /**
     *  删除主数据
     * @param _id
     * @return
     */
     int deleteInfDitionary(String _id);

    /***
     *  更新主数据
     * @param infDictionary
     * @return
     */
     int updateInfDitionary(InfDictionary infDictionary);

}
