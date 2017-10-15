package blog.controller;

import blog.bindingModel.ArticleBindingModel;
import blog.entity.*;
import blog.entityServices.ArticleService;
import blog.repository.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Base64.getEncoder;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PictureRepository pictureRepository;
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, UserRepository userRepository,
                             CategoryRepository categoryRepository, TagRepository tagRepository,
                             PictureRepository pictureRepository, ArticleService articleService) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.pictureRepository = pictureRepository;
        this.articleService = articleService;
    }

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@ModelAttribute ArticleBindingModel articleBindingModel, Model model) {
        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("view", "article/create");

        return "index";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(@Valid @ModelAttribute ArticleBindingModel articleBindingModel, BindingResult bindingResult, Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            List<Category> categories = this.categoryRepository.findAll();

            model.addAttribute("categories", categories);
            model.addAttribute("view", "article/create");
            return "index";
        }

        this.articleService.create(articleBindingModel);
        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String details(Model model, @PathVariable Integer id) throws NotFoundException {
        if (!articleRepository.exists(id)) {
            return "redirect/";
        }

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            User entityUser = this.userRepository.findByEmail(principal.getUsername());
            model.addAttribute("user", entityUser);
        }


        Article article = this.articleService.findById(id);

        Integer pageSize = 4;

        int numberOfPages = article.getComments().size() / pageSize;
        if (article.getComments().size() % pageSize != 0) {
            numberOfPages++;
        }

        model.addAttribute("numberOfPages", numberOfPages);
        model.addAttribute("categoryId", article.getCategory().getId());
        model.addAttribute("article", article);
        model.addAttribute("view", "article/details");

        return "post";
    }

    @GetMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model) throws NotFoundException {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        Article article = this.articleService.findById(id);
        String tagString = article.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "));
        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("view", "article/edit");
        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tagString);
        return "index";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, ArticleBindingModel articleBindingModel) throws IOException, NotFoundException {

        Article articleEntity = this.articleService.edit(id,articleBindingModel);

        return "redirect:/article/" + articleEntity.getId();
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect/";
        }

        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        model.addAttribute("article", article);
        model.addAttribute("view", "article/delete");

        return "index";
    }

    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        List<Tag> tags=new ArrayList<>();
        tags.addAll(article.getTags());
        this.tagRepository.delete(tags);

        List<Picture> pictures = new ArrayList<>();
        pictures.addAll(article.getPictures());
        this.pictureRepository.delete(pictures);

        this.articleRepository.delete(article);
        return "redirect:/";
    }

    private boolean isUserAuthorOrAdmin(Article article) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }

    private HashSet<Tag> findTagsFromString(String tagString) {
        HashSet<Tag> tags = new HashSet<>();

        String[] tagNames = tagString.split(",\\s*");

        for (String tagName : tagNames) {
            Tag currentTag = this.tagRepository.findByName(tagName);

            if (currentTag == null) {
                currentTag = new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }
            tags.add(currentTag);
        }
        return tags;
    }

}
