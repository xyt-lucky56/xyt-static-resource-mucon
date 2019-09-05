package com.xyt.resource.controller;

import com.xyt.common.base.utils.StringUtils;
import com.xyt.rescource.model.SysFile;
import com.xyt.resource.service.FileUploadService;
import com.xyt.resource.service.SysFileService;
import com.xyt.resource.utill.RequestHolder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author yk
 * @date 2019/8/28 14:10
 */
@RestController
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private SysFileService sysFileService;

    @Value("${xyt.upload.BasePath}")
    private String fileUploadBasePath;


    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Map<String, Object> uploadImage(@RequestParam("scene") String scene,
                                           HttpServletRequest request) {
        logger.info("进入文件上传方法，开始处理...");
        ArrayList idsList = new ArrayList();
        HashMap idsMap = null;
        if (scene == null) {
            logger.info("缺少必传参数：scene");
            return idsMap;
        }
        try {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    // 取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    SysFile sysFile = fileUploadService.upload(file, scene);
                    if (sysFile != null && !StringUtils.isEmpty(sysFile.getId())) {
                        idsList.add(sysFile.getId());
                    }

                }
            }
            if (!CollectionUtils.isEmpty(idsList)) {
                idsMap = new HashMap();
                idsMap.put("idsList", idsList);
            }
        } catch (Exception e) {
            logger.error("文件上传失败，失败原因：", e);
            return idsMap;
        }

        logger.info("进入文件上传方法，文件上传完成");
        return idsMap;
    }

    /**
     * 根据路径删除图片
     *
     * @param imgPath
     * @return
     */
    @Transactional
    @RequestMapping(value = "/deleteImgByPath", method = RequestMethod.GET)
    public Boolean deleteImg(@RequestParam("imgPath") String imgPath) {
        logger.info("进入图片删除功能===,传入的图片路径为：imgPath=" + imgPath);
        Boolean flag = true;
        try {
            if (StringUtils.isEmpty(imgPath)) {
                logger.info("传入的图片的路径为空");
                throw new RuntimeException("传入的图片路径为空");
            }
            // 从服务器路径中删除
            String diskPath = fileUploadBasePath + "/";
            String supDiskPath = diskPath + imgPath.substring(0, imgPath.lastIndexOf("/")) + "/min/";
            fileUploadService.deleteByFilePath(diskPath + imgPath);
            fileUploadService.deleteByFilePath(supDiskPath + imgPath.substring(imgPath.lastIndexOf("/") + 1));
            // 从数据库中删除
            SysFile sysFile = new SysFile();
            sysFile.setFilePath(imgPath);
            SysFile oldSysFile = sysFileService.selectOne(sysFile);
            if (oldSysFile != null) {
                sysFileService.deleteById(oldSysFile.getId());
            }
        } catch (Exception e) {
            flag = false;
            logger.info("图片删除失败！");
            throw new RuntimeException("图片删除失败");
        }

        return flag;

    }

    /**
     * 根据文件路径删除文件
     *
     * @param filePath
     * @return
     */
    @Transactional
    @RequestMapping(value = "/deleteFileByPath", method = RequestMethod.GET)
    public Boolean deleteFileByPath(@RequestParam("filePath") String filePath) {
        logger.info("进入文件删除功能===,传入的文件路径为：filePath=" + filePath);
        Boolean flag = true;
        try {
            if (StringUtils.isEmpty(filePath)) {
                logger.info("传入的文件的路径为空");
                throw new RuntimeException("传入的文件路径为空");
            }
            // 从服务器路径中删除
            String diskPath = fileUploadBasePath + "/";
//            String supDiskPath = diskPath + imgPath.substring(0, imgPath.lastIndexOf("/")) + "/min/";
            fileUploadService.deleteByFilePath(diskPath + filePath);//删除原图
//            fileUploadService.deleteByFilePath(supDiskPath + imgPath.substring(imgPath.lastIndexOf("/") + 1));//删除压缩图
            // 从数据库中删除
            SysFile sysFile = new SysFile();
            sysFile.setFilePath(filePath);
            SysFile oldSysFile = sysFileService.selectOne(sysFile);
            if (oldSysFile != null) {
                sysFileService.deleteById(oldSysFile.getId());
            }
        } catch (Exception e) {
            flag = false;
            logger.error("文件删除失败！", e);
            throw new RuntimeException("文件删除失败");
        }

        return flag;

    }

    /**
     * 根据文件ID删除图片
     *
     * @param id
     * @return Boolean
     */
    @Transactional
    @RequestMapping(value = "/deleteImgById", method = RequestMethod.GET)
    public Boolean deleteImgById(@RequestParam("id") String id) {
        logger.info("进入图片删除功能===,传入的图片id为：id=" + id);
        Boolean flag = true;
        try {
            if (StringUtils.isEmpty(id)) {
                logger.info("传入的图片的id为空");
                throw new RuntimeException("传入的图片id为空");
            }
            SysFile oldSysFile = sysFileService.selectOneById(id);
            if (oldSysFile != null) {
                // 从服务器路径中删除
                String diskPath = fileUploadBasePath + "/";
                String imgPath = oldSysFile.getFilePath();
                String supDiskPath = diskPath + imgPath.substring(0, imgPath.lastIndexOf("/")) + "/min/";
                fileUploadService.deleteByFilePath(diskPath + imgPath);//删除原图
                fileUploadService.deleteByFilePath(supDiskPath + imgPath.substring(imgPath.lastIndexOf("/") + 1));//删除压缩图
                // 从数据库中删除
                sysFileService.deleteById(oldSysFile.getId());
            }
        } catch (Exception e) {
            flag = false;
            logger.error("图片删除失败！", e);
            throw new RuntimeException("图片删除失败");
        }

        return flag;

    }

    /**
     * 根据文件ID删除文件
     *
     * @param id
     * @return
     */
    @Transactional
    @RequestMapping(value = "/deleteFileById", method = RequestMethod.GET)
    public Boolean deleteFileById(@RequestParam("id") String id) {
        logger.info("进入文件删除功能===,传入的文件id为：id=" + id);
        Boolean flag = true;
        try {
            if (StringUtils.isEmpty(id)) {
                logger.info("传入的文件的id为空");
                throw new RuntimeException("传入的文件id为空");
            }
            SysFile oldSysFile = sysFileService.selectOneById(id);
            if (oldSysFile != null) {
                // 从服务器路径中删除
                String diskPath = fileUploadBasePath + "/";
                String filePath = oldSysFile.getFilePath();
                fileUploadService.deleteByFilePath(diskPath + filePath);//删除文件
                // 从数据库中删除
                sysFileService.deleteById(oldSysFile.getId());
            }
        } catch (Exception e) {
            flag = false;
            logger.error("文件删除失败！", e);
            throw new RuntimeException("文件删除失败");
        }

        return flag;

    }

    /**
     * 根据业务ID删除图片
     *
     * @param relId
     * @return
     */
    @Transactional
    @RequestMapping(value = "/deleteImgByRelId", method = RequestMethod.GET)
    public Boolean deleteImgByRelId(@RequestParam("relId") String relId) {
        logger.info("进入图片删除功能===,传入的图片id为：relId=" + relId);
        Boolean flag = true;
        try {
            if (StringUtils.isEmpty(relId)) {
                logger.info("传入的图片的业务ID为空");
                throw new RuntimeException("传入的图片的业务ID为空");
            }
            SysFile sysFile = new SysFile();
            sysFile.setFkRelId(relId);
            List<SysFile> oldSysFileList = sysFileService.selectList(sysFile);
            if (!CollectionUtils.isEmpty(oldSysFileList)) {
                for (SysFile sf : oldSysFileList) {
                    if (sf != null) {
                        // 从服务器路径中删除
                        String diskPath = fileUploadBasePath + "/";
                        String imgPath = sf.getFilePath();
                        String supDiskPath = diskPath + imgPath.substring(0, imgPath.lastIndexOf("/")) + "/min/";
                        fileUploadService.deleteByFilePath(diskPath + imgPath);//删除原图
                        fileUploadService.deleteByFilePath(supDiskPath + imgPath.substring(imgPath.lastIndexOf("/") + 1));//删除压缩图
                        // 从数据库中删除
                        sysFileService.deleteById(sf.getId());
                    }

                }

            }
        } catch (Exception e) {
            flag = false;
            logger.error("图片删除失败！", e);
            throw new RuntimeException("图片删除失败");
        }

        return flag;

    }


    /**
     * 根据业务ID删除图片
     *
     * @param relId
     * @return Boolean
     */
    @Transactional
    @RequestMapping(value = "/deleteFileByRelId", method = RequestMethod.GET)
    public Boolean deleteFileByRelId(@RequestParam("relId") String relId) {
        logger.info("进入文件删除功能===,传入的文件id为：relId=" + relId);
        Boolean flag = true;
        try {
            if (StringUtils.isEmpty(relId)) {
                logger.info("传入的文件的业务ID为空");
                throw new RuntimeException("传入的文件的业务ID为空");
            }
            SysFile sysFile = new SysFile();
            sysFile.setFkRelId(relId);
            List<SysFile> oldSysFileList = sysFileService.selectList(sysFile);
            if (!CollectionUtils.isEmpty(oldSysFileList)) {
                for (SysFile sf : oldSysFileList) {
                    if (sf != null) {
                        // 从服务器路径中删除
                        String diskPath = fileUploadBasePath + "/";
                        String filePath = sf.getFilePath();
                        fileUploadService.deleteByFilePath(diskPath + filePath);//删除文件
                        // 从数据库中删除
                        sysFileService.deleteById(sf.getId());
                    }

                }

            }
        } catch (Exception e) {
            flag = false;
            logger.error("文件删除失败！", e);
            throw new RuntimeException("文件删除失败");
        }

        return flag;

    }

    /**
     * 加载业务场景上传的原始图片
     *
     * @param scene    场景
     * @param fileName 文件名称，必须携带扩展名
     * @return
     */
    @RequestMapping(value = "/view/img/{yyyyMMdd}/{scene}/{fileName:.+}")
    public ResponseEntity<byte[]> viewImgByPath(@PathVariable("yyyyMMdd") String yyyyMMdd
            , @PathVariable("scene") String scene, @PathVariable("fileName") String fileName) {

        File file;
        byte[] body = null;
        InputStream is = null;
        ResponseEntity<byte[]> entity;
        try {
            System.out.println(fileName);
            file = new File(fileUploadBasePath + "/" + yyyyMMdd + "/" + scene + "/" + fileName);
            is = new FileInputStream(file);
            body = new byte[is.available()];
            is.read(body);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            HttpStatus statusCode = HttpStatus.OK;
            entity = new ResponseEntity<byte[]>(body, headers, statusCode);

            return entity;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("系统找不到指定的文件", e);
        } catch (IOException e) {
            throw new RuntimeException("文件读取失败", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载业务场景上传的小型图片
     *
     * @param scene    场景
     * @param fileName 文件名称，必须携带扩展名
     * @return
     */
    @RequestMapping(value = "/view/img/min/{yyyyMMdd}/{scene}/{fileName:.+}")
    public ResponseEntity<byte[]> viewMinImgByPath(@PathVariable("yyyyMMdd") String yyyyMMdd,
                                                   @PathVariable("scene") String scene,
                                                   @PathVariable("fileName") String fileName) {
        File file;
        byte[] body = null;
        InputStream is = null;
        ResponseEntity<byte[]> entity = null;
        try {
            file = new File(fileUploadBasePath + "/" + yyyyMMdd + "/" + scene + "/" + "min" + "/" + fileName);
            if (file.exists()) {
                is = new FileInputStream(file);
                body = new byte[is.available()];
                is.read(body);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                HttpStatus statusCode = HttpStatus.OK;
                entity = new ResponseEntity<byte[]>(body, headers, statusCode);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("系统找不到指定的文件", e);
        } catch (IOException e) {
            throw new RuntimeException("文件读取失败", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    /**
     * 加载业务场景上传的图片
     *
     * @param id   文件的id
     * @param type pc电脑端，mobile移动端
     * @return
     */
    @RequestMapping(value = "/view/img/{id}")
    public ResponseEntity<byte[]> viewImgById(@PathVariable("id") String id, @RequestParam(required = false, value = "type") String type) {

        File file;
        byte[] body = null;
        InputStream is = null;
        ResponseEntity<byte[]> entity;
        try {
            SysFile sysFile = sysFileService.selectOneById(id);
            if (sysFile == null) {
                throw new RuntimeException("文件不存在");
            }
            String fullPath = "";
            if (type == null || "pc".equalsIgnoreCase(type)) {
                fullPath = fileUploadBasePath + "/" + sysFile.getFilePath();
            } else {
                String imgPath = sysFile.getFilePath();
                fullPath = fileUploadBasePath + "/" + imgPath.substring(0, imgPath.lastIndexOf("/")) + "/min/" + imgPath.substring(imgPath.lastIndexOf("/") + 1);
            }
            file = new File(fullPath);
            is = new FileInputStream(file);
            body = new byte[is.available()];
            is.read(body);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            HttpStatus statusCode = HttpStatus.OK;
            entity = new ResponseEntity<byte[]>(body, headers, statusCode);

            return entity;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("系统找不到指定的文件", e);
        } catch (IOException e) {
            throw new RuntimeException("文件读取失败", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通用上传对应的通用下载
     *
     * @param sysFileId
     * @return
     */
    @RequestMapping(value = "/download/{sysFileId}")
    public void download(@PathVariable String sysFileId) {
        if (StringUtils.isEmpty(sysFileId)) {
            throw new RuntimeException("文件不存在");
        }
        SysFile sysFile = sysFileService.selectOneById(sysFileId);
        if (sysFile == null) {
            throw new RuntimeException("文件不存在");
        }
        String filePath = fileUploadBasePath + "/" + sysFile.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        export(file, file.getName());
    }

    /**
     * 文件下载
     *
     * @param file
     * @param fileName
     */
    protected void export(File file, String fileName) {
        if (file == null) {
            throw new RuntimeException("下载文件不能为空");
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            HttpServletRequest request = RequestHolder.getRequest();
            HttpServletResponse response = RequestHolder.getResponse();

            inputStream = new FileInputStream(file);
            byte[] data = IOUtils.toByteArray(inputStream);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            if (StringUtils.isEmpty(fileName)) {
                fileName = file.getName();
            }

            String header = request.getHeader("User-Agent").toUpperCase();
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                fileName = URLEncoder.encode(fileName, "utf-8");
                fileName = fileName.replace("+", "%20");    //IE下载文件名空格变+号问题
            } else {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            }
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream;charset=UTF-8");
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("下载文件异常：{}", e, file.getAbsolutePath());
            throw new RuntimeException("下载文件异常");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("下载文件失败：", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("下载文件失败：", e);
                }
            }
        }
    }

}
