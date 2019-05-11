package com.xcbeyond.common.file.chunk.controller;

import com.xcbeyond.common.file.chunk.service.FileService;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件分片操作Controller
 * @Auther: xcbeyond
 * @Date: 2019/5/9 22:56
 */
@RestController
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 文件分片下载
     * @param range http请求头Range，用于表示请求指定部分的内容。
     *              格式为：Range: bytes=start-end  [start,end]表示，即是包含请求头的start及end字节的内容
     * @param request   http请求
     * @param response  http响应
     */
    @RequestMapping(value = "/file/chunk/download", method = RequestMethod.GET)
    public void fileChunkDownload(@RequestHeader(value = "Range") String range,
                                  HttpServletRequest request, HttpServletResponse response) {
        fileService.fileChunkDownload(range,request,response);
    }
}