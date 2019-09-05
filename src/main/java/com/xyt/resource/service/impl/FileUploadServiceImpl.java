package com.xyt.resource.service.impl;

import com.xyt.common.base.BaseEntity;
import com.xyt.rescource.model.SysFile;
import com.xyt.resource.service.FileUploadService;
import com.xyt.resource.service.SysFileService;
import com.xyt.resource.utill.DateUtil;
import com.xyt.resource.utill.DelFileUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("fileUploadService")
public class FileUploadServiceImpl implements FileUploadService {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private static final int BUFFER = 8192;

    @Autowired
    private SysFileService sysFileService;
    @Value("${xyt.upload.BasePath}")
    private String fileUploadBasePath;
    /**
     * 支持的最大的文件大小
     */
    @Value("${xyt.upload.file.filesize}")
    private long fileSize;
    /**
     * 支持的上传类型
     */
    @Value("${xyt.upload.file.extensions}")
    private String exts;

    public FileUploadServiceImpl() throws ClassNotFoundException {
        //Class.forName("com.fengunion.fastdfs.client.FastDFSClient");
    }

    @Override
    public SysFile uploadAndSaveDB(MultipartFile file, String relId, String fileType, String savePath) throws RuntimeException {
        SysFile sysFile = this.upload(file, savePath);
        sysFile.setFkRelId(relId);
        sysFile.setFileType(fileType);
        sysFileService.save(sysFile);
        return sysFile;
    }

    @Override
    public SysFile uploadAndSaveDB(String originalFilename, MultipartFile file, String relId, String fileType, String savePath) throws RuntimeException {
        SysFile sysFile = this.upload(originalFilename, file, savePath);
        sysFile.setFkRelId(relId);
        sysFile.setFileType(fileType);
        sysFileService.save(sysFile);
        return sysFile;
    }

    @Override
    public SysFile upload(MultipartFile file, String savePath) throws RuntimeException {
        try {
            return this.upload(file.getOriginalFilename(), file, savePath);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("上传文件到文件服务器出错",e);
        }
    }

    @Override
    @Transactional
    public SysFile upload(String originalFilename, MultipartFile file, String savePath) throws RuntimeException {
        String extName = ""; // 扩展名格式：
        if (originalFilename.lastIndexOf(".") >= 0) {
            extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        if (exts.indexOf(extName.substring(1)) < 0) {
            //throw new PlatformException(ResStatus.FILEUPLOAD_EXT_NOT_SUPPORT);
            throw new RuntimeException("上传文件只能是" + exts + "格式!");
        }
        long fSize = file.getSize() / 1024;
        System.err.println("文件大小：" + file.getSize() + " " + fSize);
        if (fSize > fileSize) {
            throw new RuntimeException("上传文件大小不能超过" + fileSize + "M!");
        }
        String fileName = file.getOriginalFilename();
        String basePath = fileUploadBasePath + "/";//根目录
        String relativePath = savePath + "/";//业务目录
        String businessPath = DateUtil.dateToString(new Date(), "yyyyMMdd") + "/" + relativePath;//业务目录+年月日目录
        String absolutePcPath = basePath + businessPath;//全路径
        String uuid = UUID.randomUUID().toString();
        String filePath = uuid + extName;
        File targetFile = new File(absolutePcPath, filePath);
        //生成文件
        try {
            if (!targetFile.exists()) {
                if (!targetFile.getParentFile().exists()) {
                    Boolean booleanMkdirs = targetFile.getParentFile().mkdirs();
                    if (booleanMkdirs) {
                        logger.info("上传文件的所有上级文件夹创建成功！");
                    } else {
                        logger.info("上传文件的相关上级文件夹创建失败！");
                    }
                }
                Boolean booleanCreateNewFile = targetFile.createNewFile();
                if (booleanCreateNewFile) {
                    //保存原图
                    file.transferTo(targetFile);
                    FileInputStream fis = new FileInputStream(targetFile);
                    boolean flag = isImage(fis);//判断是否为图片
                    if (flag) {
                        //先生成压缩图片
                        String path = businessPath + filePath;
                        //设置图片大小，单位：b+
                        logger.info("开始压缩！");
                        byte[] bytes = compressUnderSize(file.getBytes(), 204800);
                        logger.info("压缩结束！");
                        //小型图片文件上传
                        this.uploadMinFile(bytes, path);
                    }
                    logger.info("空白文件创建成功！");
                } else {
                    logger.info("空白文件创建失败！");
                }

            }
        } catch (Exception e) {
            logger.error("生成文件失败", e);
        }
        SysFile sysFile = new SysFile();
        sysFile.setId(uuid);
        sysFile.setFileName(filePath);
        sysFile.setFileRealName(originalFilename);
        sysFile.setFileFormat(extName.substring(1));
        sysFile.setCreateTime(new Date());
        sysFile.setLastModifyTime(new Date());
        sysFile.setFileSize(fSize);
        sysFile.setFilePath((businessPath + filePath).replaceAll("\\\\", "/"));
        sysFileService.saveSysFile(sysFile);
        return sysFile;
    }

    public static boolean isImage(InputStream inputStream) {
        if (inputStream == null) {
            return false;
        }
        Image img;
        try {
            img = ImageIO.read(inputStream);
            return !(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteAndUpdateDB(String relId, String fileType) throws RuntimeException {
        SysFile file = this.getSysFile(relId, fileType);
        if (file != null) {
            try {
                String delUrl = fileUploadBasePath + file.getFilePath();
                DelFileUtil.delete(delUrl);// + File.separator + key);
            } catch (Exception e) {
                throw new RuntimeException("文件服务器上删除文件出错");
            }
            SysFile sysFile = new SysFile();
            sysFile.setId(file.getId());
            sysFile.setValid(BaseEntity.INVALID);
            sysFileService.update(sysFile);
        }
    }

    @Override
    public void delete(String relId, String fileType) throws RuntimeException {
        SysFile file = this.getSysFile(relId, fileType);
        if (file != null) {
            try {
                String delUrl = fileUploadBasePath + file.getFilePath();
                DelFileUtil.delete(delUrl);// + File.separator + key);
            } catch (Exception e) {
                throw new RuntimeException("文件服务器上删除文件出错");
            }
        }
    }

    @Override
    public byte[] download(String relId, String fileType) throws RuntimeException {
        SysFile file = this.getSysFile(relId, fileType);
        byte[] fileContent = null;
        try {
            fileContent = this.getBytes(fileUploadBasePath + file.getFilePath());
        } catch (Exception e) {
            throw new RuntimeException("文件服务器上下载文件出错");
        }
        return fileContent;
    }

    @Override
    public byte[] downloadById(String id) throws RuntimeException {
        SysFile file = sysFileService.selectOneById(id);
        byte[] fileContent = null;
        try {
            fileContent = this.getBytes(fileUploadBasePath + file.getFilePath());
        } catch (Exception e) {
            throw new RuntimeException("文件服务器上下载文件出错");
        }
        return fileContent;
    }

    @Override
    public void deleteByFilePath(String filePath) throws RuntimeException {
        try {
            String delUrl = filePath;
            DelFileUtil.delete(delUrl);// + File.separator + key);
        } catch (Exception e) {
            throw new RuntimeException("文件服务器上删除文件出错");
        }
    }

    private SysFile getSysFile(String relId, String fileType) {
        SysFile sysFile = new SysFile();
        sysFile.setFkRelId(relId);
        sysFile.setFileType(fileType);
        List<SysFile> files = sysFileService.selectList(sysFile);
        if (CollectionUtils.isNotEmpty(files)) {
            SysFile file = files.get(0);
            return file;
        } else {
            return null;
        }
    }

    /**
     * 获得指定文件的byte数组
     */
    private byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 压缩图片 文件上传
     *
     * @param bytes 压缩后的图片数据
     * @param path  图片保存路径
     * @author lml
     */
    private void uploadMinFile(byte[] bytes, String path) {
        //设置文件保存路径
        String diskPath = fileUploadBasePath+"/";
        String fullPath = diskPath + path.substring(0, path.lastIndexOf("/")) + "/" + "min" + "/";
        String newFileName = path.substring(path.lastIndexOf("/"));
        File tempFile = new File(fullPath, newFileName);
        if (!tempFile.exists()) {
            //创建文件夹
            if (!tempFile.getParentFile().exists()) {
                Boolean booleanMkdirs = tempFile.getParentFile().mkdirs();
                if (booleanMkdirs) {
                    logger.info("上传文件的所有上级文件夹创建成功！");
                } else {
                    logger.info("上传文件的相关上级文件夹创建失败！");
                }
            }
            FileOutputStream fos = null;
            try {
                Boolean booleanCreateNewFile = tempFile.createNewFile();
                if (booleanCreateNewFile) {
                    //文件上传
                    fos = new FileOutputStream(tempFile);
                    fos.write(bytes);
                    logger.info("空白文件创建成功！");
                } else {
                    logger.info("空白文件创建失败！");
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("文件保存失败");
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 将图片压缩到指定大小以内
     *
     * @param srcImgData 源图片数据
     * @param maxSize    目的图片大小
     * @return 压缩后的图片数据
     * @author lml
     */
    public static byte[] compressUnderSize(byte[] srcImgData, long maxSize) {
        byte[] imgData = Arrays.copyOf(srcImgData, srcImgData.length);
        if (imgData.length > maxSize) {
            logger.info("开始执行压缩！");
            try {
                BigDecimal decimalscale = new BigDecimal(maxSize).divide(new BigDecimal(imgData.length), 6, BigDecimal.ROUND_HALF_UP);
                double scale = new Double(1);
                ;
                if (null != decimalscale && decimalscale.compareTo(BigDecimal.ZERO) > 0) {
                    if (decimalscale.compareTo(new BigDecimal(0.02)) <= 0) {
                        scale = new Double(0.2);
                    } else if (decimalscale.compareTo(new BigDecimal(0.02)) > 0 && decimalscale.compareTo(new BigDecimal(0.08)) <= 0) {
                        scale = new Double(0.4);
                    } else if (decimalscale.compareTo(new BigDecimal(0.08)) > 0 && decimalscale.compareTo(new BigDecimal(0.2)) <= 0) {
                        scale = new Double(0.8);
                    } else {
                        scale = new Double(1);
                    }
                } else {
                    scale = new Double(0.3);
                }
                imgData = compress(imgData, scale);
            } catch (IOException e) {
                throw new IllegalStateException("压缩图片过程中出错，请及时联系管理员！", e);
            }

        }
        return imgData;
    }

    /**
     * 按照 宽高 比例压缩
     *
     * @param srcImgData 待压缩图片输入流
     * @param scale      压缩刻度
     * @return 压缩后图片数据
     * @throws IOException 压缩图片过程中出错
     * @author lml
     */
    public static byte[] compress(byte[] srcImgData, double scale) throws IOException {
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(srcImgData));
        int width = (int) (bi.getWidth() * scale); // 源图宽度
        int height = (int) (bi.getHeight() * scale); // 源图高度

//		Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = tag.getGraphics();
        g.setColor(Color.RED);
//		g.drawImage(bi, 0, 0, null); //绘制处理后的图
        g.drawImage(bi, 0, 0, width, height, null);
        g.dispose();

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ImageIO.write(tag, "JPEG", bOut);

        return bOut.toByteArray();
    }

}
