package example.com.demo.model;

import java.util.ArrayList;

import example.com.demo.model.AlbumDataModel.Images;
import example.com.demo.utils.StringUtil;

public class UserInfoModel {
    public String birthdate;
    public String display_name;
    public String id;
    public String email;
    public ArrayList<Images> images;
    public boolean isFailure;
    public String error;


    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public String getInitial() {
        if (!StringUtil.isNullOrEmpty(getDisplay_name())) {
            return Character.toString(getDisplay_name().charAt(0)).toUpperCase();
        }
        return "";
    }

}
