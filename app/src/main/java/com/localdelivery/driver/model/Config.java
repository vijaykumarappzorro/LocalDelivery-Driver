package com.localdelivery.driver.model;



 public class Config {

    static final String login_url = "http://traala.com/deliver/ws/api.php?action=login";
  public   static final String signUp_url = "http://traala.com/deliver/ws/api.php?action=register";
    static final String check_vichletype ="http://traala.com/deliver/ws/api.php?action=check_vehicle_type";
    static final String update_vehicletype="http://traala.com/deliver/ws/api.php?action=update_vehicle_type";
    static final String forgot_password ="http://traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=forgot_password";
    static final String user_detail ="http://traala.com/deliver/ws/api.php?action=user_detail";
    static final String verif_email="http://traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=verify_account";
    static final String pending_requests_url = "http://traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=get_customers_pending_requests&driver_id=";
    static final String accept_Rquest_customer="http://traala.com/deliver/ws/api.php?action=requestAcceptedByDriver";
    static final String completed_Trips="http://traala.com/deliver/ws/api.php?action=driver_history&driver_id=";

   public  static  String user_type = "driver";
   public  static  String device_type ="A";

}
