package com.xcbeyond.common.file.chunk.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: xcbeyond
 * @Date: 2019/5/9 23:01
 */
public interface FileService {
    void fileChunkUpload(long chunkId, MultipartFile multipartFile);
    /**
     * 文件分片下载
     * @param range
     * @param request
     * @param response
     */
    void fileChunkDownload(String range, HttpServletRequest request, HttpServletResponse response);
}
