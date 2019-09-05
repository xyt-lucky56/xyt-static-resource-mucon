package com.xyt.fileupload.dao;

import com.xyt.common.base.BaseMapper;
import com.xyt.fileupload.model.SysFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author copyright by yk
 * @category
 * @since 2019/08/28
 */
@Mapper
public interface SysFileMapper extends BaseMapper<SysFile> {

    /**
     *
     *
     * @param relId
     */
    void deleteSysFileByRelId(@Param("relId") String relId, @Param("fileType") String fileType);
    
    /**
     * <pre>
     * 批量插入附件
     * </pre>
     *
     * @param fileList
     * @return
     */
	void saveSysFileBatch(List<SysFile> fileList);
}