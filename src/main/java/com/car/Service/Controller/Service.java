package com.car.Service.Controller;
import com.car.Class.Car.PrivateCar;
import com.car.Class.Car.Truck;
import com.car.Class.Response;
import com.car.Class.User;
import com.car.dao.Select;
import com.car.dao.UpdateUser;
import com.chenerzhu.common.util.SecureUtils;
import com.google.gson.Gson;

import java.util.List;

public class Service {
    Response response = new Response();
    public String login(String username, String password) {
        if (new Select().SelectUserLogin(username, password) == null) {
            response.status_code = 1;
            response.status_message = "用户名或者密码错误";
        } else {
            response.status_code = 0;
            response.status_message = "登录成功";
        }
        return new Gson().toJson(response);
    }
    public String register(String username, String password) {  // 注册
        password = SecureUtils.getMD5(password);
        if (new Select().addUser(username, password) == null) {
            response.status_code = 1;
            response.status_message = "用户名已存在";
        } else {
            response.status_code = 0;
            response.status_message = "注册成功";
        }
        return new Gson().toJson(response);
    }

    public String rent(String username, Integer id, String type) { // 租车
        User user = new Select().SelectUserByUsername(username);
        if (new Select().FindUserRentOrNot(user) == true) {
            response.status_code = 1;
            response.status_message = "已租车";
        } else {
           new UpdateUser().rent(user,id,type);
        }
        return "";
    }

    public String getprivatecarlist(Integer userid) {  // 获取小车列表
        List<PrivateCar> privateCars = new Select().GetRentedPrivateCarList(userid);
        Gson gson = new Gson();
        response.status_code = 0;
        response.status_message = null;
        return gson.toJson(response) + gson.toJson(privateCars);
    }

    public String gettrucklist(Integer userid) {   // 获取卡车列表
        List<Truck> trucks = new Select().GetRentedTruckList(userid);
        Gson gson = new Gson();
        return gson.toJson(trucks);
    }

    public String recharge(String username, Integer money) {  // 充值
        User user = new Select().SelectUserByUsername(username);
        if (user == null) {
            response.status_code = 1;
            response.status_message = "用户名错误";
        } else {
            new UpdateUser().charge(user, money);
            response.status_code = 0;
            response.status_message = "充值成功";
        }
        return new Gson().toJson(response);
    }

}
