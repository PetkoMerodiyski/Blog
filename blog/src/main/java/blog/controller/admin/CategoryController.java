package blog.controller.admin;

import blog.bindingModel.CategoryBiningModel;
import blog.entity.Article;
import blog.entity.Category;
import blog.repository.ArticleRepository;
import blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String list(Model model){
        List<Category> categories=this.categoryRepository.findAll();
        categories=categories.stream().sorted(Comparator.comparing(Category::getId)).collect(Collectors.toList());
        model.addAttribute("categories",categories);
        model.addAttribute("view","admin/category/list");

        return  "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("view","admin/category/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(CategoryBiningModel categoryBiningModel){
            if(StringUtils.isEmpty(categoryBiningModel.getName())){
                return "redirect:/admin/categories/create";
            }

            Category category=new Category(categoryBiningModel.getName());

            this.categoryRepository.saveAndFlush(category);

            return "redirect:/admin/categories/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }
        Category category=this.categoryRepository.findOne(id);
        model.addAttribute("category",category);
        model.addAttribute("view","admin/category/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id,CategoryBiningModel categoryBiningModel){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }

        Category category=this.categoryRepository.findOne(id);

        category.setName(categoryBiningModel.getName());
        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model,@PathVariable Integer id){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }

        Category category=this.categoryRepository.findOne(id);

        model.addAttribute("category",category);
        model.addAttribute("view","admin/category/delete");

        return "base-layout";

    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(CategoryBiningModel categoryBiningModel,@PathVariable Integer id){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }

        Category category=this.categoryRepository.findOne(id);

        for(Article article: category.getArticles()){
            this.articleRepository.delete(article);
        }

        this.categoryRepository.delete(category);

        return "redirect:/admin/categories/";
    }


}
