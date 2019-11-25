package pl.milk.aggregator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.milk.aggregator.persistance.model.Test;
import pl.milk.aggregator.persistance.model.TestContainer;
import pl.milk.aggregator.persistance.repository.TestContainerRepository;
import pl.milk.aggregator.persistance.repository.TestRepository;

import java.util.Optional;

//https://dzone.com/articles/spring-boot-jpa-hibernate-oracle
@Controller
@RequestMapping(path="/persistance")
public class PersistanceTestController {

    @Autowired
    private TestContainerRepository testContainerRepository;
    @Autowired
    private TestRepository testRepository;


    @GetMapping("/container")
    ModelAndView getContainerForId(@RequestParam("id") Long id) {
        Optional<TestContainer> testContainer = testContainerRepository.findById(id);
        ModelAndView modelAndView;
        if(testContainer.isPresent()) {
            modelAndView = new ModelAndView("test_container");
            modelAndView.addObject("id", testContainer.get().getId());
            modelAndView.addObject("name", testContainer.get().getName());
            modelAndView.addObject("tests", testContainer.get().getTests());
        } else {
            modelAndView = new ModelAndView("not_found");
        }
        return modelAndView;
    }

    @GetMapping("/test")
    ModelAndView getTestForId(@RequestParam("id") Long id) {
        Optional<Test> testContainer = testRepository.findById(id);
        ModelAndView modelAndView;
        if(testContainer.isPresent()) {
            modelAndView = new ModelAndView("test");
            modelAndView.addObject("id", testContainer.get().getId());
            modelAndView.addObject("description", testContainer.get().getDescription());
        } else {
            modelAndView = new ModelAndView("not_found");
        }
        return modelAndView;
    }
}
