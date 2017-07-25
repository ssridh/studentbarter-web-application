package com.db.studentbarter.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.db.studentbarter.model.User;
import com.db.studentbarter.service.AccountService;
import com.db.studentbarter.service.SecurityService;
import com.db.studentbarter.service.UserService;
import com.db.studentbarter.validator.UserValidator;

@Controller
public class UserDetailsController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private UserService userService;
   
    @Autowired
    private UserValidator userValidator;
  
    @RequestMapping(value = "/userdetails", method = RequestMethod.GET)
    public String userdetails(Model model) {
    	
    	model.addAttribute("userForm", new User());
    	
        return "userdetails";
    }
   
    @RequestMapping(value = "/userdetails", method = RequestMethod.POST)
    public String userdetails(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "userdetails";
        }
        String username = securityService.findLoggedInUsername();
    	User user = userService.findByAccount(accountService.findByUsername(username));
        userForm.setUserId(user.getUserId());
        userService.update(userForm);
        
        return "redirect:/posts";
    }
}
