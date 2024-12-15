package com.example.demo.models.General.ModelsForResponse;

public class BasicResponse {
    private String error;
    private boolean success;

    public BasicResponse(){}

    public BasicResponse(String error, boolean success){
        this.error=error;
        this.success=success;
    }

    public void setError(String error){
        this.error=error;
    }

    public void setSuccess(boolean success){
        this.success=success;
    }

    public String getError(){
        return this.error;
    }

    public boolean getSuccess(){
        return this.success;
    }
}
