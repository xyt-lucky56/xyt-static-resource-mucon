package com.xyt.resource.service.impl;

import com.xyt.common.base.BaseEntity;
import com.xyt.common.base.BaseServiceImpl;
import com.xyt.common.base.utils.StringUtils;
import com.xyt.rescource.model.SysFile;
import com.xyt.resource.dao.SysFileMapper;
import com.xyt.resource.service.SysFileService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author copyright by 蜂向科技
 * @since 2018/03
 * @version R1.0
 * @category 银行调用接口通用记录表
 */
@Service
public class SysFileServiceImpl extends BaseServiceImpl<SysFileMapper,SysFile> implements SysFileService {

    @Autowired
	SysFileMapper sysFileMapper;

//	@Value("${file.view.path}")
//    private String realFilePath;

	public int updateByRelId(SysFile sysFile){
    	Example example = new Example(SysFile.class); 
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fileType", sysFile.getFileType());
        criteria.andEqualTo("fkRelId", sysFile.getFkRelId());
        criteria.andEqualTo("valid", BaseEntity.VALID);
    	return sysFileMapper.updateByExampleSelective(sysFile, example);
    }
    
    @Override
    public SysFile selectByRelIdAndType(String fkRelId, String fileType) {
    	SysFile sysFile = new SysFile();
    	sysFile.setFkRelId(fkRelId);
    	sysFile.setFileType(fileType);
    	sysFile.setValid(BaseEntity.VALID);
    	return sysFileMapper.selectOne(sysFile);
    }
    
	@Override
	public List<SysFile> findByRelIdAndFileType(String fkRelId, String fileType) {
        Example example = new Example(SysFile.class);
        example.createCriteria().andEqualTo("fkRelId", fkRelId).andEqualTo("fileType", fileType).andEqualTo("valid", BaseEntity.VALID);
        example.setOrderByClause("createTime  asc");
		return sysFileMapper.selectByExample(example);
	}
	
	/**
     * 根据类型和业务id查找附件记录，大于一条抛异常
     * @param fkRelId
     * @param fileType
     * @return
     */
	@Override
	public SysFile findOneSysFile(String fkRelId, String fileType) {
		Example example = new Example(SysFile.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("fkRelId", String.valueOf(fkRelId));
		criteria.andEqualTo("fileType", fileType);
		criteria.andEqualTo("valid", BaseEntity.VALID);
		List<SysFile> list = sysFileMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		if (list.size() > 1) {
			throw new RuntimeException("查出多余一条附件记录");
		}
		return list.get(0);
    }

	@Override
    public SysFile selectByRelIdAndTypeForViewUrl(String id, String fileType) {
    	SysFile sysFile = selectByRelIdAndType(id,  fileType);
    	if(null == sysFile){
    		return null;
    	}
//    	sysFile.setFileViewUrl(realFilePath + sysFile.getFilePath());
    	return sysFile;
    }
    
    /**
     * 根据relId、文件类型查询  附件列表
     */
    @Override
    public List<SysFile> findSysFileByRelId(SysFile sysFile) {
        Example example = new Example(SysFile.class);
        example.setOrderByClause("id asc "); //加入排序条件
        
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(sysFile.getFileType())) {
            criteria.andEqualTo("fileType", sysFile.getFileType());
        }
//        if (sysFile.getFkProjectId() != null) {
//            criteria.andEqualTo("fkProjectId", sysFile.getFkProjectId());
//        }
        criteria.andEqualTo("fkRelId", sysFile.getFkRelId());
        criteria.andEqualTo("valid", BaseEntity.VALID);
        return this.sysFileMapper.selectByExample(example);
    }

//	/**
//	 * 设置下载打包名称
//	 */
//	@Override
//	public List<SysFile> groupFile(List<SysFile> sysFileList) {
//		if (CollectionUtils.isEmpty(sysFileList)) {
//			return null;
//		}
//
//		// 1、分组，根据附件类型进行分组 Map<附件类型, List<SysFile>>
//		Map<String, List<SysFile>> sysFileMap = new HashMap<>();
//		for (SysFile sysFile : sysFileList) {
//			List<SysFile> sfList = sysFileMap.get(sysFile.getFileType());
//			if (sfList == null) {
//				sfList = new ArrayList<>();
//			}
//			sfList.add(sysFile);
//			sysFileMap.put(sysFile.getFileType(), sfList);
//		}
//
//		List<SysFile> list = new ArrayList<>();
//		// 2、遍历 map，重置附件名称
//		Set<String> keySet = sysFileMap.keySet();
//		for (String key : keySet) {
//			List<SysFile> sfList = sysFileMap.get(key);
//			String valueName = SysFileConstant.typeMap.get(sfList.get(0).getFileType());
//			if (sfList.size() == 1) {
//				String fileName = sfList.get(0).getFileName();
//				String[] nameArray = fileName.split("\\.");
//				String vName = valueName;
//				if (nameArray.length > 1) {
//					vName = vName + "." + nameArray[nameArray.length - 1];
//				}
//				sfList.get(0).setFileRealName(vName);
//				list.addAll(sfList);
//				continue;
//			}
//
//			int i = 1;
//			for (SysFile sysFile : sfList) {
//				String fileName = sysFile.getFileName();
//				String[] nameArray = fileName.split("\\.");
//				String vName = valueName + i;
//				if (nameArray.length > 1) {
//					vName = vName + "." + nameArray[nameArray.length - 1];
//				}
//				sysFile.setFileRealName(vName);
//
//				list.add(sysFile);
//				i++;
//			}
//		}
//
//		return list;
//	}
	
	/** 
     * 根据类型和关联id修改 附件
     */
    @Override
    public int updateSysFileByType(SysFile sysFile) {
        Example example = new Example(SysFile.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(sysFile.getFileType())) {
            criteria.andEqualTo("fileType", sysFile.getFileType());
        }
        if (StringUtils.isNotBlank(sysFile.getFileName())) {
            criteria.andEqualTo("fileName", sysFile.getFileName());
        }
        if (StringUtils.isNotBlank(sysFile.getFkRelId())) {
            criteria.andEqualTo("fkRelId", sysFile.getFkRelId());
        }
        if (StringUtils.isNotBlank(sysFile.getFilePath())) {
            criteria.andEqualTo("filePath", sysFile.getFilePath());
        }
//        if(sysFile.getFkProjectId() != null){
//        	criteria.andEqualTo("fkProjectId", sysFile.getFkProjectId());
//        }
        criteria.andEqualTo("fkRelId", sysFile.getFkRelId());
        return this.sysFileMapper.updateByExampleSelective(sysFile, example);
    }

	@Override
	public SysFile getSysFile(String relId, String fileType) {
		SysFile sysFile = new SysFile();
    	sysFile.setFileType(fileType);
    	sysFile.setFkRelId(relId);
    	sysFile.setValid(BaseEntity.VALID);
    	List<SysFile> list = this.findSysFileByRelId(sysFile);
    	if (list != null && !list.isEmpty()) {
    		sysFile = list.get(0);
    	}
    	return sysFile;
	}

	@Override
	public void saveSysFileBatch(List<SysFile> fileList) {
		sysFileMapper.saveSysFileBatch(fileList);
	}

	@Override
	public int saveSysFile(SysFile sysFile) {
        return this.sysFileMapper.insertSelective(sysFile);
	}

    @Override
    public void deleteByRelId(String relId, String fileType) {
        Example example = new Example(SysFile.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fkRelId", relId);
        criteria.andEqualTo("fileType", fileType);
        SysFile sysFile = new SysFile();
        sysFile.setValid(BaseEntity.INVALID);
        sysFileMapper.updateByExampleSelective(sysFile, example);
    }
}
