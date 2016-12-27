package com.localdelivery.driver.controller;


public class ModelManager {

    LoginManager loginManager;
    SignUpManager signUpManager;
    UpdateManager updateManager;
    ForgotPassManager forgotPassManager;
    UserDetailManager userDetailManager;
    VerifyEmailManager verifyEmailManager;


    private static ModelManager modelManager;

    public ModelManager() {
        loginManager = new LoginManager();
        signUpManager = new SignUpManager();
        updateManager = new UpdateManager();
        forgotPassManager = new ForgotPassManager();
        userDetailManager = new UserDetailManager();
        verifyEmailManager = new VerifyEmailManager();

    }

    public static ModelManager getInstance() {
        if (modelManager == null)
            return modelManager = new ModelManager();
        else
            return modelManager;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public SignUpManager getSignUpManager() {
        return signUpManager;
    }
    public UpdateManager getUpdateManager() {
        return updateManager;
    }

    public ForgotPassManager getForgotPassManager() {
        return forgotPassManager;
    }

    public UserDetailManager getUserDetailManager() {
        return userDetailManager;
    }

    public VerifyEmailManager getVerifyEmailManager() {
        return verifyEmailManager;
    }
}
