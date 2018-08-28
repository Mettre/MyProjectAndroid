package com.example.mettre.myaprojectandroid.bean;

public class LoginBean {

    /**
     * access_token : eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MzI2MTI2MjU5OTAzMjgzMiIsImV4cCI6MTUzNTQ2NDAzN30.Hy-A8uqY2ld2w5jqoSauu-vbh-35mhUd7F-8ZG1Xo8U
     * token_type : eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MzI2MTI2MjU5OTAzMjgzMiIsImV4cCI6MTUzNTQ2NDAzN30.Hy-A8uqY2ld2w5jqoSauu-vbh-35mhUd7F-8ZG1Xo8U
     * expires_in : 60
     */

    private String access_token;
    private String token_type;
    private int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
