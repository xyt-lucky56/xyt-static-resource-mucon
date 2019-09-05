package com.xyt.resource.utill;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 上传配置类
 * @author zhaojz
 * @date 2018年7月26日
 *
 */
@Component
@ConfigurationProperties(prefix = "xyt.upload")
public class UploadSceneConfig {
	
	private Map<String, String>  diskdir;
	private Map<String, String> scene;
	
	public String getScene(String key) {
		if(scene.containsKey(key)) {
			return scene.get(key);
		}
		return null;
	}

	public void setScene(Map<String, String> scene) {
		this.scene = scene;
	}

	public String getDiskdir(String key) {
		if(diskdir.containsKey(key)) {
			return diskdir.get(key);
		}
		return null;
	}

	public void setDiskdir(Map<String, String> diskdir) {
		this.diskdir = diskdir;
	}

	
}
