package com.xcbeyond.common.file.chunk.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: xcbeyond
 * @Date: 2019/5/9 0009 23:01
 */
public interface FileService {
    /**
     * 文件分片下载
     * @param range
     * @param request
     * @param response
     */
    void fileChunkDownload(String range, HttpServletRequest request, HttpServletResponse response);
}
