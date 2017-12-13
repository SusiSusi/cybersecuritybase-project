package sec.project.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private HttpSession httpSession;

    @RequestMapping("*")
    public String defaultMapping() {
        // how to fix A6-Sensitive Data Exposure
        //  httpSession.invalidate();
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        // how to fix A6-Sensitive Data Exposure
        //    httpSession.invalidate();
        return "form";
    }

    @RequestMapping(value = "/topsecret", method = RequestMethod.GET)
    public String topSecret() {

        // how to fix A7-Missing Function Level Access Control
        //   if (null == httpSession.getAttribute("name")) {
        //      return "redirect:/";
        //  }
        return "topsecret";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(Model model, @RequestParam String name, @RequestParam String address) {

        if (name.isEmpty() || address.isEmpty()) {
            return "redirect:/";
        }

        //  if (signupRepository.findByName(name)) {
        //      return "redirect:/form";
        //  }
        signupRepository.save(new Signup(name, address));
        httpSession.setAttribute("name", name);
        return "done";
    }

}
