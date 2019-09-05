package com.xyt.resource.service;

import com.xyt.common.base.BaseService;
import com.xyt.rescource.model.SysFile;

import java.util.List;
import java.util.Set;

/**
 * @author copyright by yk
 * @since 2019/08/28 15:08
 * @version R1.0
 */
public interface SysFileService extends BaseService<SysFile> {
	
	public int updateByRelId(SysFile sysFile);

    SysFile selectByRelIdAndType(String fkRelId, String fileType);
    

    /**
     * 根据类型和业务id查找附件记录
     * @param fkRelId
     * @param fileType
     * @return
     */
    List<SysFile> findByRelIdAndFileType(String fkRelId, String fileType);
    
    /**
     * 根据类型和业务id查找附件记录，大于一条抛异常
     * @param fkRelId
     * @param fileType
     * @return
     */
    SysFile findOneSysFile(String fkRelId, String fileType);
    
    /**
     * 
     * <pre>
     * 根据relId、文件类型查询  附件列表
     * </pre>
     *
     * @param sysFile
     * @return
     */
    public List<SysFile> findSysFileByRelId(SysFile sysFile);

	/**
	 * 设置下载打包名称
	 * 
	 * @param sysFileList
	 * @return
	 */
//	List<SysFile> groupFile(List<SysFile> sysFileList);
	
	/**
	 * 获取文件url
	 * 
	 * @return
	 */
	SysFile selectByRelIdAndTypeForViewUrl(String relId, String fileType);
	
    /**
     * 
     * <pre>
     * 根据关联id（附件类型或附件名称）修改 附件
     * </pre>
     *
     * @param sysFile
     * @return
     */
    public int updateSysFileByType(SysFile sysFile);

	public SysFile getSysFile(String relId, String fileType);

	public void saveSysFileBatch(List<SysFile> fileList);
	
	/**
     * 
     * <pre>
     * 保存 附件
     * </pre>
     *
     * @param sysFile
     * @return
     */
    public int saveSysFile(SysFile sysFile);

    void deleteByRelId(String relId, String fileType);
}