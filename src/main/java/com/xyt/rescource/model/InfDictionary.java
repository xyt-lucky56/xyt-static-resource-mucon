package com.xyt.rescource.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ：xxj
 * @create 2019-08-26 09:08
 * @function
 * @editLog
 */
@Document(collection = "inf_dictionary_collect")
public class InfDictionary {
    /**
     *
     */
    private String _id;

    /**
     * 内容
     */
    private String contentName;
    /**
     * 标识
     */
    private String signName;
    /**
     * 是否选择（0：不选择，1：选择）
     */
    private Integer defaultSelect;
    /**
     * 备注
     */
    private String remark;
    private  Integer sortNo;
    private Integer stopSign;

    public Integer getStopSign() {
        return stopSign;
    }

    public void setStopSign(Integer stopSign) {
        this.stopSign = stopSign;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public String getContentName(){
        return contentName;
    }

    public void setContentName(String contentName){
        this.contentName = contentName;
    }

    public String getSignName(){
        return signName;
    }

    public void setSignName(String signName){
        this.signName = signName;
    }

    public Integer getDefaultSelect(){
        return defaultSelect;
    }

    public void setDefaultSelect(Integer defaultSelect){
        this.defaultSelect = defaultSelect;
    }

    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }
}
