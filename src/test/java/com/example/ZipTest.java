package com.example;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileNotFoundException;

public class ZipTest {
    @Test
    @Ignore
    public void test() throws FileNotFoundException {
        File file = new File("/Users/wangbin/out/start/" + System.currentTimeMillis() + ".zip");
        ZipUtil.pack(ResourceUtils.getFile("classpath:static/eureka"), file);
    }
}
