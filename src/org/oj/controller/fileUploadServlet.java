package org.oj.controller;

import utils.Tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Created by xanarry on 18-1-4.
 */
@MultipartConfig
@WebServlet(name = "fileUploadServlet", urlPatterns = {"/upload-image"})
public class fileUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().equals("/upload-image")) uploadImage(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    private void uploadImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part imagePart = request.getPart("upload");
        // CKEditor提交的很重要的一个参数
        String callback = request.getParameter("CKEditorFuncNum");


        Collection<String> headers = imagePart.getHeaderNames();

        for (String t : headers) {
            System.out.println(t + ": " + imagePart.getHeader(t));
        }

        String contentType = imagePart.getContentType();
        Long contentSize = imagePart.getSize();

        System.out.println("size: " + contentSize);

        String jsPattern = "<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(%s, '%s', '')</script>";
        PrintWriter printWriter = response.getWriter();

        if (contentType.startsWith("image")) {

            String sbName = imagePart.getSubmittedFileName();
            String expandedName = sbName.substring(sbName.lastIndexOf(".")); // 文件扩展名
            System.out.println("suffix: " + expandedName);


            //图片上传路径
            String uploadPath = getServletContext().getRealPath("/img/problemImage");
            String fileName = java.util.UUID.randomUUID().toString() + expandedName; // 采用时间+UUID的方式随即命名
            uploadPath += ("/" + fileName);

            String webPath = getServletContext().getContextPath() + "/img/problemImage/" + fileName;


            Tools.saveFile(imagePart.getInputStream(), uploadPath);


            System.out.println("webpath:" + webPath);
            System.out.println("uploadpath:" + uploadPath);
            // 返回"图像"选项卡并显示图片  request.getContextPath()为web项目名

            String js = String.format(jsPattern, callback, webPath);
            System.out.println(js);
            printWriter.write(js);
        } else {
            String js = String.format(jsPattern, callback, "文件非图片类型");
            System.out.println(js);
            printWriter.write(js);
        }
        printWriter.close();
    }

}
