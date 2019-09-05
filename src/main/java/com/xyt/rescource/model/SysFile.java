package com.xyt.rescource.model;

import com.xyt.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author copyright by 武汉信运通信息产业有限公司
 * @since 2018/011
 * @version R1.0
 * @category 文件表
 */

@ApiModel("文件表")
@Table(name = "sys_file")
public class SysFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="业务id")
    @Column(name = "fk_rel_id")
    private String fkRelId;//业务id

    @ApiModelProperty(value="文件名称")
    @Column(name = "file_name")
    private String fileName;//文件名称

    @ApiModelProperty(value="文件格式 zip|pdf|jpg")
    @Column(name = "file_format")
    private String fileFormat;//文件格式 zip|pdf|jpg

    @ApiModelProperty(value="文件类型")
    @Column(name = "file_type")
    private String fileType;//文件类型

    @ApiModelProperty(value="文件真实名字")
    @Column(name = "file_real_name")
    private String fileRealName;//文件真实名字

    @ApiModelProperty(value="文件路径")
    @Column(name = "file_path")
    private String filePath;//文件路径

    @ApiModelProperty(value="文件大小")
    @Column(name = "file_size")
    private Long fileSize;//文件大小

    @ApiModelProperty(value="备注")
    @Column(name = "remark")
    private String remark;//备注

    @ApiModelProperty(value="创建人名称")
    @Column(name = "create_name")
    private String createName;//创建人名称

    @ApiModelProperty(value="最后修改人的id")
    @Column(name = "last_modify_id")
    private String lastModifyId;//最后修改人的id

    @ApiModelProperty(value="最后修改人名称")
    @Column(name = "last_modify_name")
    private String lastModifyName;//最后修改人名称

    @ApiModelProperty(value="lastModifyTime")
    @Column(name = "last_modify_time")
    private java.util.Date lastModifyTime;//

    @ApiModelProperty(value="最后修改人IP")
    @Column(name = "last_modify_ip")
    private String lastModifyIp;//最后修改人IP

    /** 文件预览路径 **/
    @Transient
    private String fileViewUrl;


    public String getFileViewUrl() {
        return fileViewUrl;
    }

    public void setFileViewUrl(String fileViewUrl) {
        this.fileViewUrl = fileViewUrl;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }
    public String getFkRelId(){
        return fkRelId;
    }

    public void setFkRelId(String fkRelId){
        this.fkRelId=fkRelId;
    }
    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName=fileName;
    }
    public String getFileFormat(){
        return fileFormat;
    }

    public void setFileFormat(String fileFormat){
        this.fileFormat=fileFormat;
    }
    public String getFileType(){
        return fileType;
    }

    public void setFileType(String fileType){
        this.fileType=fileType;
    }
    public String getFileRealName(){
        return fileRealName;
    }

    public void setFileRealName(String fileRealName){
        this.fileRealName=fileRealName;
    }
    public String getFilePath(){
        return filePath;
    }

    public void setFilePath(String filePath){
        this.filePath=filePath;
    }
    public Long getFileSize(){
        return fileSize;
    }

    public void setFileSize(Long fileSize){
        this.fileSize=fileSize;
    }
    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark){
        this.remark=remark;
    }
    public String getCreateName(){
        return createName;
    }

    public void setCreateName(String createName){
        this.createName=createName;
    }
    public String getLastModifyId(){
        return lastModifyId;
    }

    public void setLastModifyId(String lastModifyId){
        this.lastModifyId=lastModifyId;
    }
    public String getLastModifyName(){
        return lastModifyName;
    }

    public void setLastModifyName(String lastModifyName){
        this.lastModifyName=lastModifyName;
    }
    public java.util.Date getLastModifyTime(){
        return lastModifyTime;
    }

    public void setLastModifyTime(java.util.Date lastModifyTime){
        this.lastModifyTime=lastModifyTime;
    }
    public String getLastModifyIp(){
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp){
        this.lastModifyIp=lastModifyIp;
    }
}