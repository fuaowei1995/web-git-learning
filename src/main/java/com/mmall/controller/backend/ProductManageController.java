package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    IUserService iUserService;

    @Autowired
    IProductService iProductService;

    @Autowired
    IFileService iFileService;

    @RequestMapping(value = "save_product.do")
    @ResponseBody
    public ServerResponse productSave(HttpServletRequest httpServletRequest, Product product){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录！");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            return iProductService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限！");
    }

    @RequestMapping(value = "set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest httpServletRequest,Integer productId,Integer productStatus){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorMessage("未登录，请先登陆！");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            return iProductService.setProductStatus(productId,productStatus);
        }
        return ServerResponse.createByErrorMessage("无权限，需要管理员权限！");
    }

    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpServletRequest httpServletRequest,Integer productId){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorMessage("未登录，请先登陆！");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            //填充业务逻辑
            return iProductService.manageProductDetail(productId);
        }
        return ServerResponse.createByErrorMessage("无权限，需要管理员权限！");
    }

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse getList(HttpServletRequest httpServletRequest,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登陆！");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            //填充业务逻辑
            return iProductService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限，需要管理员权限！");
    }

    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpServletRequest httpServletRequest,String productName,Integer producId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登陆！");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            //填充业务逻辑
            return iProductService.searchProduct(productName,producId,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限，需要管理员权限！");
    }

    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(HttpServletRequest httpServletRequest,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登陆！");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            //填充业务逻辑
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);

        }
        return ServerResponse.createByErrorMessage("无权限，需要管理员权限！");
    }

    @RequestMapping(value = "richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpServletRequest httpServletRequest, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            //填充业务逻辑
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }
}
