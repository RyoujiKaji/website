package com.example.demo.models.Users.ModelsForRequest;
import com.example.demo.models.Users.ModelsForResponse.UserPrivateInfo;

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
