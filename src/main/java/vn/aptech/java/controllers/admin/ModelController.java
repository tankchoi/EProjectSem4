package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.services.ModelService;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ModelController {
//    @Autowired
//    private ModelService modelService;
    @RequestMapping("/model")
    public String index(Model model) {
        model.addAttribute("activePage", "model");
        return "admin/pages/model/index";
    }


}
