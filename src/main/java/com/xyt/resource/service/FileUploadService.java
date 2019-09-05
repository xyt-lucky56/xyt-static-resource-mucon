package com.xyt.resource.service;


import java.util.List;

import com.xyt.rescource.model.SysFile;
import org.springframework.web.multipart.MultipartFile;


public interface FileUploadService {

    public SysFile uploadAndSaveDB(MultipartFile file, String relId, String fileType, String savePath) throws Exception;

    public SysFile uploadAndSaveDB(String originalFilename, MultipartFile file, String relId, String fileType, String savePath) throws Exception;

    public SysFile upload(MultipartFile file, String savePath) throws Exception;

    public SysFile upload(String originalFilename, MultipartFile file, String savePath) throws Exception;

    public void deleteAndUpdateDB(String relId, String fileType) throws Exception;

    public void delete(String relId, String fileType) throws Exception;

    public byte[] download(String relId, String fileType) throws Exception;

    /**
     * 根据主键下载
     * @param id
     * @return
     * @throws Exception
     */
    public byte[] downloadById(String id) throws Exception;

//    public byte[] downloadZip(List<SysFile> sysFiles) throws Exception;

    public void deleteByFilePath(String filePath) throws Exception;

}
