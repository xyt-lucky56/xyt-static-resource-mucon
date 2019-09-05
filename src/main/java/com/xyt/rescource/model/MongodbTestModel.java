package com.xyt.rescource.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Document(collection = "test_Collect")
public class MongodbTestModel {
    private Integer sortNo;
    private Date createData;
    private Boolean stopSign;
    private String _id;
    private String testContent;
    private String testOrContent;
    private Float floatNumber;
    private Double aDouble;
    private LocalTime createTime;
    private Byte aByte;

    public MongodbTestModel() {
        super();
        createData = new Date();
        createTime = LocalTime.now();
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Date getCreateData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        ca.setTime(createData);
        ca.add(Calendar.HOUR_OF_DAY, 8);
//        return String2Date(sdf.format(ca.getTime()));
//        return createData;
        return ca.getTime();
    }

    public void setCreateData(Date createData) {
        this.createData = createData;
    }

    public Boolean getStopSign() {
        return stopSign;
    }

    public void setStopSign(Boolean stopSign) {
        this.stopSign = stopSign;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTestContent() {
        return testContent;
    }

    public void setTestContent(String testContent) {
        this.testContent = testContent;
    }

    public Float getFloatNumber() {
        return floatNumber;
    }

    public void setFloatNumber(Float floatNumber) {
        this.floatNumber = floatNumber;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    public LocalTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalTime createTime) {
        this.createTime = createTime;
    }

    public Byte getaByte() {
        return aByte;
    }

    public void setaByte(Byte aByte) {
        this.aByte = aByte;
    }

    public String getTestOrContent() {
        return testOrContent;
    }

    public void setTestOrContent(String testOrContent) {
        this.testOrContent = testOrContent;
    }
}
