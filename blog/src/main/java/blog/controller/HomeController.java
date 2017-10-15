package blog.controller;

import blog.entity.Article;
import blog.entity.Category;
import blog.repository.ArticleRepository;
import blog.repository.CategoryRepository;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;


    @GetMapping("/")
    public String index(Model model){
        List<Category> categories=this.categoryRepository.findAll();
        List<Article> articles=this.articleRepository.findAll();

        Integer pageSize = 4;

        int numberOfPages=articleRepository.findAll().size()/pageSize;
        if(articleRepository.findAll().size()%pageSize!=0){
            numberOfPages++;
        }

        model.addAttribute("numberOfPages",numberOfPages);
        model.addAttribute("view","home/index");
        model.addAttribute("categories",categories);
        model.addAttribute("articles",articles);
        return  "index";
    }


    @GetMapping("/about")
    public String about(Model model){
        return  "about";
    }

    @GetMapping("/post")
    public String post(Model model){
        return  "post";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        return  "contact";
    }


    @RequestMapping("/error/403")
    public String accessDenied(Model model){
        model.addAttribute("view","error/403");

        return "base-layout";
    }

    @GetMapping("/category/{id}")
    public String listArticles(@PathVariable Integer id,Model model){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/";
        }

        Integer pageSize = 4;

        int numberOfPages=articleRepository.countAllByCategory_Id(id)/pageSize;
        if(articleRepository.countAllByCategory_Id(id)%pageSize!=0){
            numberOfPages++;
        }

        Category category=this.categoryRepository.findOne(id);

        model.addAttribute("numberOfPages",numberOfPages);
        model.addAttribute("categoryId", category.getId());
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("articlesNumber", category.getArticles().size());
        model.addAttribute("view","home/list-articles");
        return "index";
    }

}
