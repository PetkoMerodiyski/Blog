package blog.controller;

import blog.bindingModel.CategoryDTO;
import blog.bindingModel.CommentBindingModel;
import blog.bindingModel.SearchDTO;
import blog.entity.Article;
import blog.entity.Category;
import blog.entity.Comment;
import blog.entity.User;
import blog.repository.ArticleRepository;
import blog.repository.CategoryRepository;
import blog.repository.CommentRepository;
import blog.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.awt.print.Pageable;
import java.util.*;

@RestController
public class RestSearchController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/test")
    public List<SearchDTO> getCategory() {
        List<SearchDTO> dtos = new ArrayList<>();
        List<Article> articles = this.articleRepository.findAll();

        for (Article article : articles) {

            String categoryName = article.getCategory().getName();
            String authorName = article.getAuthor().getFullName();
            SearchDTO dto = new SearchDTO(categoryName, article, authorName);
            dtos.add(dto);
        }
        return dtos;
    }

    @GetMapping("/listArticless/{categoryId}/page/{page}/size/{size}")
    public List<Article> getArticlesInCategoryByPage(@PathVariable("categoryId") Integer categoryId,@PathVariable("page") Integer page,
                                               @PathVariable("size") Integer size) {
        PageRequest pageRequest = new PageRequest(page, size, Sort.Direction.DESC, "id");
        List<Article> articles=new ArrayList<>();
        if(categoryRepository.findOne(categoryId)!=null){
            articles = articleRepository.findAllByCategory_IdOrderByIdAsc(categoryId, pageRequest);
        }
        return articles;
    }

    @GetMapping("/searchInCategory/{categoryId}")
    public List<Article> getArticlesInCategoryAfterSearch(@PathVariable("categoryId") Integer categoryId) {
        List<Article> articles=new ArrayList<>();
        if(categoryRepository.findOne(categoryId)!=null){
            articles = articleRepository.findAllByCategory_IdOrderByIdAsc(categoryId);
        }
        return articles;
    }

    @GetMapping("/listAllArticles/page/{page}/size/{size}")
    public List<Article> getAllArticles(@PathVariable("page") Integer page,@PathVariable("size") Integer size){
        PageRequest pageRequest=new PageRequest(page,size,Sort.Direction.DESC,"id");
        Page<Article> articles=articleRepository.findAll(pageRequest);


        return articles.getContent();
    }

    @GetMapping("/getCategories")
    public List<CategoryDTO> getAllCategories(){
        List<CategoryDTO> dtos=new ArrayList<>();
        List<Category> categories=this.categoryRepository.findAll();

        for(Category category : categories){
            Integer categoryId=category.getId();
            String categoryName=category.getName();
            CategoryDTO dto=new CategoryDTO(categoryId,categoryName);
            dtos.add(dto);
        }
        return dtos;

    }

}
