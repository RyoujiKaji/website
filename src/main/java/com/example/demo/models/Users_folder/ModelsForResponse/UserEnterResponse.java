package com.example.demo.models.Users_folder.ModelsForResponse;

public class UserEnterResponse {
    private String role;
    private int id;
    private boolean success;

    public UserEnterResponse(){}

    public UserEnterResponse(String role, int id, boolean success){
        this.role=role;
        this.id=id;
        this.success=success;
    }

    public void setRole(String role){
        this.role=role;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setSuccess(boolean success){
        this.success=success;
    }

    public String getRole(){
        return this.role;
    }

    public int getId(){
        return this.id;
    }

    public boolean getSuccess(){
        return this.success;
    }
}
