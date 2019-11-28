package com.changgou.file;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/14 14:53
 * @description ：文件上穿测试类
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
public class UploadFileTest {
    @Test
    public void uploadFile() throws Exception {
        ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
        // 流 只认绝对路径 不要给相对路径
        ClientGlobal.init(resource.getFile().getAbsolutePath());
        //创建Tracker客户端  192.168.200.128
        TrackerClient trackerClient = new TrackerClient();
        //连接  (保存让你去连接Stoage的地址）
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建Stoage的客户端
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, null);
        //上传图片  参数1：上传的文件名（路径）
        String path = storageClient1.upload_file1("D:\\360Downloads\\324682.jpg", "jpg", null);
        System.out.println("http://192.168.200.128:8080/"+path);

    }



}
