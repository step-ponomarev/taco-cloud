package tacos.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import tacos.order.data.Ingredient;
import tacos.order.data.Taco;
import tacos.order.data.TacoOrder;
import tacos.order.TacoService;
import tacos.web.model.IngredientCategories;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {
    @Autowired
    private TacoService tacoService;

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        final List<IngredientCategories> ingredientCategories = new ArrayList<>();

        for (Ingredient.Type type : Ingredient.Type.values()) {
            ingredientCategories.add(new IngredientCategories(getTitle(type), tacoService.getIngredients(type)));
        }

        model.addAttribute("ingredientCategories", ingredientCategories);
    }

    private String getTitle(Ingredient.Type type) {
        return switch(type) {
            case WRAP -> "Design your wrap:";
            case PROTEIN -> "Pick your protein:";
            case CHEESE -> "Choose your cheese:";
            case VEGGIES -> "Determine your veggies:";
            case SAUCE -> "Select your sauce:";
        };
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return tacoService.order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return tacoService.taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processTaco(@Valid Taco taco,
                              Errors error,
                              @ModelAttribute(name = "tacoOrder") TacoOrder tacoOrder,
                              Model model) {
        if (error.hasErrors()) {
            return showDesignForm();
        }

        log.info("Processing taco: " + taco);
        tacoOrder.addTaco(tacoService.saveTaco(taco));

        return "redirect:/orders/current";
    }
}
