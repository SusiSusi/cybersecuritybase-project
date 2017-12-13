/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

/**
 *
 * @author susisusi
 */
@Controller
public class LoginController {

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private HttpSession httpSession;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, @RequestParam String name, @RequestParam String address) {

        if (name.isEmpty() || address.isEmpty()) {
            return "redirect:/login";
        }

        List<Signup> customer = signupRepository.findAllByName(name);

        for (Signup customer1 : customer) {
            if (customer1.getAddress().contentEquals(address)) {
                httpSession.setAttribute("name", name);
                return "done";
            }
        }

        return "redirect:/login";
    }

}
