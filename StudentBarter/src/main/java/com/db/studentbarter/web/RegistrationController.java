package com.db.studentbarter.web;

import com.db.studentbarter.model.Account;
import com.db.studentbarter.model.User;
import com.db.studentbarter.service.SecurityService;
import com.db.studentbarter.service.UserService;
import com.db.studentbarter.service.AccountService;
import com.db.studentbarter.validator.AccountValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AccountValidator accountValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("accountForm", new Account());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("accountForm") Account accountForm, BindingResult bindingResult, Model model) {
        accountValidator.validate(accountForm, bindingResult);
        System.out.println("In registration");
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        accountService.save(accountForm);
        System.out.println("Aft account save");
        //create empty user with userrole and account
        User user = new User();
        user.setAccount(accountForm);
        userService.create(user);

        securityService.autologin(accountForm.getUsername(), accountForm.getPasswordConfirm());


        return "redirect:/userdetails";

    }

   
}
