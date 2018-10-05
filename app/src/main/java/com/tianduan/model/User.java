package com.tianduan.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tianduan.MyApplication;
import com.tianduan.annotation.Column;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class User extends Model {

    @Column
    private String username;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String password;
    @Column
    private String sex;
    @Column
    private String company;
    @Column
    private String address;
    @Column
    private String picture;
    @Column
    private String duty;
    @Column
    private String registertime;
    @Column
    private String logintime;
    @Column
    private String token;
    @Column
    private Set<Role> roles;

    public User() {
    }

    public User(String json) throws JSONException {
        super(json);
    }

    public Bitmap getHeadBitmap() {
        Bitmap bitmap;
        if (picture != null) {
            String path = MyApplication.newInstance().getContext().getFilesDir().getAbsolutePath() + "/" + getObjectId() + "/user/head_pic";
            String[] path_split = picture.replace("\\", "/").split("/");
            File file = new File(path, path_split[path_split.length - 1]);
            FileInputStream fis = null;
            if (file.exists()) {
                try {
                    fis = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(fis);
                    fis.close();
                    if (bitmap != null) {
                        return bitmap;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getRegistertime() {
        return registertime;
    }

    public void setRegistertime(String registertime) {
        this.registertime = registertime;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
