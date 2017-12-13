/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Message;
import sec.project.repository.MessageRepository;
import sec.project.repository.SignupRepository;

/**
 *
 * @author susisusi
 */
@Controller
public class EventController {

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private HttpSession httpSession;

    @PostConstruct
    public void init() {
        this.messageRepository.save(new Message("First message"));
    }

    @RequestMapping(value = "/event", method = RequestMethod.GET)
    public String loadEvent(Model model) {

        if (null == httpSession.getAttribute("name")) {
            return "redirect:/form";
        }

        String signedUpname = httpSession.getAttribute("name").toString();

        model.addAttribute("list", signupRepository.findAll());
        model.addAttribute("signedUp", signupRepository.findByName(signedUpname));
        model.addAttribute("messageList", messageRepository.findAll());

        return "event";
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String submitForm(Model model, @RequestParam String message) {

        if (message.isEmpty()) {
            return "redirect:/event";
        }

        messageRepository.save(new Message(message));

        return "redirect:/event";
    }

}
