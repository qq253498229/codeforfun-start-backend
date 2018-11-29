package com.example.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author wangbin
 */
@Service
public class GenerateService {
    public static final String EUREKA_PATH = "classpath:static/eureka";
    @Value("${attachment.path}")
    private String attachmentPath;

    public File generate(List<Services> services) throws IOException {
        for (Services service : services) {
            switch (service.getKey()) {
                case "eureka": {
                    return generateEureka(service);
                }
                case "oauth": {
                    return generateOauth(service);
                }
                default: {
                    break;
                }
            }
        }
        return null;
    }

    public File generateOauth(Services service) {
        return null;
    }

    /**
     * 生成eureka
     */
    public File generateEureka(Services service) throws IOException {
        // 复制新目录
        String newPath = attachmentPath + File.separator + System.currentTimeMillis();
        File newFolder = new File(newPath + File.separator + "eureka");
        FileUtils.copyDirectory(ResourceUtils.getFile(EUREKA_PATH), newFolder);
        // 替换字符串
        File source = replaceWords(service, newFolder);
        // 压缩目录
        File output = new File(attachmentPath + File.separator + System.currentTimeMillis() + ".zip");
        ZipUtil.pack(source, output);
        // 删除新目录
        FileUtils.deleteDirectory(new File(newPath));
        return output;
    }

    /**
     * 替换字符串
     */
    private File replaceWords(Services service, File newFolder) throws IOException {
        // 替换application.properties
        replace(new File(newFolder.getPath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties"), service.toMap());
        return newFolder;
    }

    /**
     * 替换基础方法
     */
    public void replace(File file, Map<String, String> param) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder oldText = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            oldText.append(line).append("\r\n");
        }
        reader.close();

        for (Map.Entry<String, String> entry : param.entrySet()) {
            oldText = new StringBuilder(oldText.toString().replaceAll(entry.getKey(), entry.getValue()));
        }
        FileWriter writer = new FileWriter(file.getAbsolutePath());
        writer.write(oldText.toString());
        writer.close();
    }

    public File download(String fileName) {
        return new File(attachmentPath + File.separator + fileName);
    }
}
