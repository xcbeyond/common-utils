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
 * @Auther: xcbeyond
 * @Date: 2019/5/9 0009 22:56
 */
@RestController
public class FileController {
    @Resource
    private FileService fileService;

    @RequestMapping(value = "/file/chunk/download", method = RequestMethod.GET)
    public void fileChunkDownload(@RequestHeader(value = "Range") String range,
                                  HttpServletRequest request, HttpServletResponse response) {
        fileService.fileChunkDownload(range,request,response);
    }
}