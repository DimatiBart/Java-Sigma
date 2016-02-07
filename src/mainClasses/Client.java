package com.DimatiBart.sigma.mainClasses;
import com.DimatiBart.sigma.client.controller.Controller;
import com.DimatiBart.sigma.client.model.ClientModel;
import com.DimatiBart.sigma.client.view.LoginForm;
import com.DimatiBart.sigma.client.view.MFrame;

/**
 * Created by Dimati_Bart on 01.12.15.
 */
public class Client {
    public static void main( String args[] ) {
        ClientModel model = new ClientModel();
        LoginForm form = new LoginForm(model);
        //MFrame frame = new MFrame();
        //frame.setContent(form.panel);
    }
}
