package com.fire.admin.util;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @Description:
 * @ClassName: ExeclUtil
 * @Author: liuliu
 * @Date: 2021/7/22 10:49
 */
public class ExeclUtil {

    /***
     *@description: execl导出
     * @param fileName 导出文件名称
     * @param response
     *@return: void
     *@author: liuliu
     *@date: 2021-07-22 10:51
     */
    public static ExcelWriter ExeclOut(String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        return ExcelUtil.getWriter(true);

    }


}
