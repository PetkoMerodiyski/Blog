package blog.controller;

import blog.entity.Article;
import blog.entity.User;
import blog.repository.ArticleRepository;
import blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestTestController {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/testttt")
    public List<Article> testingAJAX(){
        List<Article> list = new ArrayList<>();

        list.addAll(this.articleRepository.findAll());

        return list;
    }

}
