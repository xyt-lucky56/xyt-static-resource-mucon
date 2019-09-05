package com.xyt.resource.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xyt.rescource.model.InfDictionary;
import com.xyt.resource.service.InfDictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lh.toolclass.LhClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xxj
 * @create 2019-08-26 09:08
 * @function
 * @editLog
 */
@RestController
@RequestMapping("/infDictionaryController")
@Api(value = "Swagger说明", description = "Swagger描述")
public class InfDictionaryController {
    @Autowired
    InfDictionaryService infDictionaryService;
    @Value("${server.port}")
    private String port;

    /**
     * 根据标识查询主数据，方法ID：SE20190820153146564
     *
     * @param signName 表inf_dictionary,字段名signName:标识
     * @param pageNo   当前页数
     * @param pageSize 每页条数
     * @return 主数据集合
     */
    @ApiOperation(value = "根据标识查询主数据", notes = "主数据集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "signName", value = "标识", dataType = "String"),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int")
    })
    @PostMapping("/getListOfInfDictionary")
    public PageInfo getListOfReceivingPlan(@RequestParam(value = "signName", required = false) String signName
            , @RequestParam(value = "pageNo", defaultValue = "1") int pageNo
            , @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) throws ParseException, ClassNotFoundException {
        signName = signName == null ? signName : signName.trim();


        PageHelper.startPage(pageNo, pageSize);
        List<InfDictionary> infDictionarys = infDictionaryService.getListOfInfDictionary(signName);
        PageInfo pageInfo = new PageInfo<>(infDictionarys, pageSize);
        return pageInfo;
    }

    @PostMapping("/saveInfDictionary")
    public Map<String, Object> saveInfDictionary(@RequestBody InfDictionary infDictionary) {
        infDictionary.set_id(LhClass.getMainDataLineKey(Short.parseShort(port)));
        Map<String, Object> resultMap = new HashMap<>();
        int size = infDictionaryService.saveInfDictionary(infDictionary);
        if (size > 0) {
            resultMap.put("code", "S");
            resultMap.put("detail", "保存成功");
        } else {
            resultMap.put("code", "F");
            resultMap.put("detail", "保存失败");
        }
        return resultMap;

    }

    /**
     * 删除
     *
     * @param _id
     * @return
     */
    @PostMapping("/deleteInfDictionary")
    public Map<String, Object> deleteInfDictionary(@RequestParam(value = "_id", required = false) String _id) {
        Map<String, Object> resultMap = new HashMap<>();
        int size = infDictionaryService.deleteInfDitionary(_id);
        if (size > 0) {
            resultMap.put("code", "S");
            resultMap.put("detail", "保存成功");
        } else {
            resultMap.put("code", "F");
            resultMap.put("detail", "保存失败");
        }

        return resultMap;
    }

    @PostMapping("/updateInfDitionary")
    public Map<String, Object> updateInfDitionary(@RequestBody InfDictionary infDictionary) {
        Map<String, Object> resultMap = new HashMap<>();
        int size = infDictionaryService.updateInfDitionary(infDictionary);
        if (size > 0) {
            resultMap.put("code", "S");
            resultMap.put("detail", "保存成功");
        } else {
            resultMap.put("code", "F");
            resultMap.put("detail", "保存失败");
        }
        return resultMap;


    }


}