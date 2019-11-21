package com.kyle.face;
import com.kyle.domain.User;
import com.kyle.utils.Base64Util;
import com.kyle.utils.FileUtil;
import com.kyle.utils.GsonUtils;
import com.kyle.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 人脸注册
 * @author asus
 *
 */
public class FaceAdd {

    private static String accessToken = "24.1499181888425add49c99d88d339ef90.2592000.1576137558.282335-17691223";

    public static String add() throws Exception {

/*

        byte[] bytes1 = FileUtil.readFileByBytes("C:\\Users\\liushendu\\Pictures\\Camera Roll\\123.jpg");
        String image1 = Base64Util.encode(bytes1);
*/

            User user=new User();
            String image1=user.getUface();
            String username=user.getUname();
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", image1);
            map.put("group_id", "group");
            map.put("user_id","user3");
            map.put("user_info", "people");
            map.put("liveness_control", "NORMAL");
            map.put("image_type", "FACE_TOKEN");
          /*  map.put("image_type", "BASE64");*/
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);
            // 客户端可自行缓存，过期后重新获取。
            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println("返回结果为==>"+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        FaceAdd.add();
    }

}


