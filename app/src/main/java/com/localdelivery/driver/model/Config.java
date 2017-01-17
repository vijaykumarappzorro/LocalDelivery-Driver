package com.localdelivery.driver.model;



class Config {
    static final String login_url = "http://www.traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=login";
    static final String signUp_url = "http://www.traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=register";
    static final String check_vichletype ="http://traala.com/deliver/ws/api.php?action=check_vehicle_type";
    static final String update_vehicletype="http://traala.com/deliver/ws/api.php?action=update_vehicle_type";
    static final String forgot_password ="http://traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=forgot_password";
    static final String user_detail ="http://traala.com/deliver/ws/api.php?action=user_detail";
    static final String verif_email="http://traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=verify_account";
    static final String pending_requests_url = "http://traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=get_customers_pending_requests&driver_id=";
}
