package com.project.manutenza;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    //Forse non mi serve sto affare
    private FacebookController facebookController;

    public UserController(FacebookController facebookController) {
        this.facebookController = facebookController;
    }

    //Tramite le annotazioni RequestParam, prendo i dati dal template
    @RequestMapping("editProfile/complete")
    @ResponseBody
    public String saveProfile(@RequestParam("email") String email,
                              @RequestParam("firstName") String firstName,
                              @RequestParam("lastName") String lastName,
                              @RequestParam("birthday") String birthday,
                              @RequestParam("id") String id){

        return email +" "+ firstName +" "+ lastName +" "+ birthday +" "+ id;
    }


}
