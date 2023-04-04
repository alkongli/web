package com.example.controller;

import com.example.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/img")
@Api(tags="上传下载的相关接口")
public class imgController {

//    在yml配置的路径
    @Value("${img.path}")
    private String imgPath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @ApiOperation(value = "上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){


//        原始文件名
        String originalFilename = file.getOriginalFilename();
//        截取原始文件名的后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

//        使用UUID重新生成文件名，防止文件重名发生覆盖

        String fileName = UUID.randomUUID().toString()+substring;

//        判断imgPath是否存在
        File dir=new File(imgPath);
        if(!dir.exists())
            dir.mkdirs();

//        将file用transferTo()进行转存
        try {
            file.transferTo(new File(imgPath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success(fileName);

    }

    /**
     * 文件下载
     * @throws IOException
     */
    @ApiOperation(value = "下载")
    @GetMapping("/download")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "product中img的值"),
    })
    public void download(HttpServletResponse response,String name) {
        try {
            //        输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(imgPath + name));

            //        输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

//            设置响应回去的是图片文件
//            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
//            关闭资源
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
