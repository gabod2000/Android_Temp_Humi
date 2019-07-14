package io.terasyshub.services;

import android.app.Activity;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;


import java.util.Date;

import io.terasyshub.R;
import io.terasyshub.utils.TerasysAlert;

public class ValidationService {

    private static ValidationService validatorInstance;
    private Activity activity;

    private ValidationService(){
        if (validatorInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public synchronized static ValidationService getInstance(){
        if (validatorInstance == null) {
            validatorInstance = new ValidationService();
        }
        return validatorInstance;
    }


    public ValidationService setActivity(Activity activity) {
        this.activity = activity;
        return validatorInstance;
    }

    public boolean ValidateAuthentication(String email, String password){
       if(email.equals("") || email==null){
           TerasysAlert.show(activity.getString(R.string.error_empty_email_address), false, activity);
           return false;
       }
       else if(password.equals("") || password==null){
           TerasysAlert.show(activity.getString(R.string.error_empty_password), false, activity);
           return false;
       }
       else {
           return true;
       }
    }
}
