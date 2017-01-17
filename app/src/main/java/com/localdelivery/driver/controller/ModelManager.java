package com.localdelivery.driver.controller;


public class ModelManager {

    private LoginManager loginManager;
    private FacebookLoginManager facebookLoginManager;
    private SignUpManager signUpManager;
    private UpdateManager updateManager;
    private ForgotPassManager forgotPassManager;
    private UserDetailManager userDetailManager;
    private VerifyEmailManager verifyEmailManager;
    private PendingRequestsManager pendingRequestsManager;


    private static ModelManager modelManager;

    private ModelManager() {
        loginManager = new LoginManager();
        facebookLoginManager = new FacebookLoginManager();
        signUpManager = new SignUpManager();
        updateManager = new UpdateManager();
        forgotPassManager = new ForgotPassManager();
        userDetailManager = new UserDetailManager();
        verifyEmailManager = new VerifyEmailManager();
        pendingRequestsManager = new PendingRequestsManager();

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

    public FacebookLoginManager getFacebookLoginManager() {
        return facebookLoginManager;
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

    public PendingRequestsManager getPendingRequestsManager() {
        return pendingRequestsManager;
    }
}
