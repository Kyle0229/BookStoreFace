package com.kyle.controller;


import com.alibaba.fastjson.JSON;
import com.baidu.aip.face.AipFace;


import com.kyle.domain.User;
import com.kyle.domain.UserList;
import com.kyle.interfaces.LoginClient;
import com.kyle.interfaces.UserClient;
import com.kyle.request.MetaData;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class FaceController {
    private static final String APP_ID_FACE ="17691223";
    private static final String API_KEY_FACE ="R50zymIMpKtvOeEdUSLefCGi";
    private static final String SECRET_KEY_FACE ="1uHqWClywn3FSNZKhiaDzv5u1bBBQy3T";
    @Resource
    UserClient userClient;
    @Resource
    LoginClient loginClient;
    @GetMapping("face")
    public  String face(){
        return "user";
    }



    //人脸识别登录
    @ResponseBody
    @RequestMapping(value = "faceLogin",method = RequestMethod.POST)
    public Map faceLogin(@RequestBody MetaData metaData, HttpSession session) throws Exception{
        //使用Ajax提交base64字符串，需要经过去头转码
        String img_data = metaData.getSnapData().substring(22, metaData.getSnapData().length());
        AipFace client = new AipFace(APP_ID_FACE, API_KEY_FACE, SECRET_KEY_FACE);
        HashMap<String,String> options = new HashMap<String,String>();  //请求预置参数
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        options.put("match_threshold", "80");
        JSONObject res = client.search(img_data,"BASE64","group", options);    //人脸库搜索 group为后台用户组名称
        System.out.println("返回结果集为:"+res.toString(2));
        Map map = JSON.parseObject(res.toString());
        Map result = (Map) map.get("result");
        if(result!=null) {
            String user_list = JSON.toJSONString(result.get("user_list"));
            user_list = user_list.substring(1, user_list.length() - 1);
            user_list = user_list.replace("[", "");
            user_list = user_list.replace("]", "");
            UserList list = JSON.parseObject(user_list, UserList.class);
            System.out.println(list.getUser_id());
            User user = new User();
            user.setUname(list.getUser_id());
            User current = userClient.findByUserName(user);
            System.out.println(current);
            session.setAttribute("user",current);
        }
            return map;


    }
    /*    //人脸检测
    @ResponseBody
    @RequestMapping(value = "faceCheck",method = RequestMethod.POST)
    public Map faceCheck(@RequestParam("snapData") String data) throws Exception{
        //使用Ajax提交base64字符串，需要经过去头转码

        String img_data = data.substring(22, data.length());
        AipFace client = new AipFace(APP_ID_FACE, API_KEY_FACE, SECRET_KEY_FACE);
        JSONObject res = client.detect(img_data,"BASE64", new HashMap<String,String>());    //检测出人脸的位置
        System.out.println(res.toString(2));
        Map map = JSON.parseObject(res.toString());
        Map result = (Map) map.get("result");
        String face_list = JSON.toJSONString(result.get("face_list"));
        face_list  =face_list .substring(1,face_list .length()-1);
        face_list  = face_list .replace("[","");
        face_list  = face_list .replace("]","");
        FaceList list1 = JSON.parseObject(face_list ,FaceList.class);
        System.out.println("<=====>"+list1.getFace_token());
        return map;
        }*/



        //采集人脸图像并同步上传至百度人脸管理库
        @ResponseBody
        @RequestMapping(value = "faceAdd",method = RequestMethod.POST)
        public Map faceAdd(@RequestBody MetaData metaData,HttpSession session) {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                Map map = new HashMap();
                map.put("status", 404);
                return map;
            } else {
                String img_data = metaData.getSnapData().substring(22, metaData.getSnapData().length());
                AipFace client = new AipFace(APP_ID_FACE, API_KEY_FACE, SECRET_KEY_FACE);
                // 传入可选参数调用接口
                //User user = new User();
                HashMap<String, String> options = new HashMap<String, String>();
                options.put("quality_control", "NORMAL");
                options.put("liveness_control", "LOW");
                options.put("action_type", "REPLACE");

                String image = img_data;
                String imageType = "BASE64";
                String groupId = "group";
                String userId = user.getUname();
                /*  String userId=user.getUname();*/

                // 人脸注册
                JSONObject res = client.addUser(image, imageType, groupId, userId, options);
                Map map = JSON.parseObject(res.toString());
                return map;
            }
        }

    }

