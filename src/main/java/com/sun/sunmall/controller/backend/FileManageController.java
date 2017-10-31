package com.sun.sunmall.controller.backend;

import com.google.common.collect.Maps;
import com.sun.sunmall.common.Const;
import com.sun.sunmall.common.ResponseCode;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.pojo.User;
import com.sun.sunmall.service.IFileService;
import com.sun.sunmall.service.IUserService;
import com.sun.sunmall.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by sun on 2017/5/18.
 */
@Controller
@RequestMapping("/manage/file")
public class FileManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping("/img/upload")
    @ResponseBody
    public ServerResponse uploadImgFile(HttpSession session, MultipartFile multipartFile, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //todo 测试不加request 直接session.
            String path = request.getSession().getServletContext().getRealPath("upload");//request.getSession().getServletContext() 获取的是Servlet容器对象，相当于tomcat容器了。
                                                                                            // getRealPath("/") 获取实际路径，“/”指代项目根目录，所以代码返回的是项目在容器中的实际发布运行的根路径

            String targetFilePath = iFileService.uploadImageFile(multipartFile, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFilePath;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFilePath);
            fileMap.put("url", url);

            return ServerResponse.createBySuccess(fileMap);//返回uri和url给前端，供调用图片地址
        }
        else
            return ServerResponse.createByErrorMessage("用户无管理员权限");
    }
    @RequestMapping("/richtxt/upload")
    @ResponseBody
    public Map uploadRichtxtFile(HttpSession session, MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        Map resultMap = Maps.newHashMap();//富文本传输格式 前端一般有要求 所以按要求，采用Map形式传递数据。 此处采用simditor格式
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "用户未登录，请登录管理员");
            return resultMap;
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //todo 测试不加request 直接session.
            String path = request.getSession().getServletContext().getRealPath("upload");//request.getSession().getServletContext() 获取的是Servlet容器对象，相当于tomcat容器了。
            // getRealPath("/") 获取实际路径，“/”指代项目根目录，所以代码返回的是项目在容器中的实际发布运行的根路径
            String targetFilePath = iFileService.uploadImageFile(multipartFile, path);
            if (StringUtils.isBlank(targetFilePath)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFilePath;

            resultMap.put("success", false);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Controll-Allow-Headers","X-File-Name");//同样是各式需求 需要改变response的Header

            return resultMap;//返回给前端，供调用地址
        }
        else
            resultMap.put("success", false);
        resultMap.put("msg", "无管理员权限");
        return resultMap;
    }
}
