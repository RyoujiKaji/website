package com.example.demo.models.Users_folder.ModelsForRequest;
import com.example.demo.models.Users_folder.ModelsForResponse.UserPrivateInfo;

public class UserModifierPrivateInfo extends UserPrivateInfo{
    private int id;
    
    public UserModifierPrivateInfo(){super();}
    
    public void setI(int id){
        this.id=id;
    }

    public int getId(){
        return this.id;
    }
}
