package com.xcbeyond.common.file.chunk.controller;

import com.xcbeyond.common.file.chunk.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件分片操作Controller
 * @Auther: xcbeyond
 * @Date: 2019/5/9 22:56
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 文件分片上传
     * @param chunkId   分片ID
     * @param multipartFile 分片文件
     */
    @RequestMapping(value = "/chunk/upload", method = RequestMethod.POST)
    public void fileChunkUpload(@RequestParam("chunkId") long chunkId,
                                @RequestParam(value = "chunk") MultipartFile multipartFile) {
        fileService.fileChunkUpload(chunkId, multipartFile);
    }

    /**
     * 文件分片下载
     * @param range http请求头Range，用于表示请求指定部分的内容。
     *              格式为：Range: bytes=start-end  [start,end]表示，即是包含请求头的start及end字节的内容
     * @param request   http请求
     * @param response  http响应
     */
    @RequestMapping(value = "/chunk/download", method = RequestMethod.GET)
    public void fileChunkDownload(@RequestHeader(value = "Range") String range,
                                  HttpServletRequest request, HttpServletResponse response) {
        fileService.fileChunkDownload(range,request,response);
    }
}