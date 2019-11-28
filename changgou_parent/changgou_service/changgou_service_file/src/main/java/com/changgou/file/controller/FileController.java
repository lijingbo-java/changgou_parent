package com.changgou.file.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.file.pojo.FastDFSFile;
import com.changgou.file.utils.FastDFSClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/15 20:50
 * @description ：文件上传
 * @version: 1.0
 */

@RestController
@RequestMapping("/file")
public class FileController {
    /**
     * 上传图片
     *
     * @return
     */
    @RequestMapping("/upload")//前后端参数不一致的时候,用@RequedtParam映射
    public Result upload(@RequestParam(value = "file") MultipartFile name) {
        String extension = FilenameUtils.getExtension(name.getOriginalFilename());
        try {
            FastDFSFile fastDFSFile = new FastDFSFile(name.getOriginalFilename(), name.getBytes(), extension);
            String s = FastDFSClient.uploadFile(fastDFSFile);
            return new Result(true,StatusCode.OK,"上传成功",FastDFSClient.getTrackerUrl()+s);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //throw  new RuntimeException("上传失败,请重新上传");
        return new Result(false,StatusCode.ERROR,"上传失败");
    }
}
