package blog.entityServices;

import blog.bindingModel.ArticleBindingModel;
import blog.entity.*;
import blog.repository.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Base64.getEncoder;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PictureRepository pictureRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository,
                              TagRepository tagRepository, PictureRepository pictureRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.pictureRepository = pictureRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Article findById(Integer articleId) throws NotFoundException {

        if(!checkIfArticleExists(articleId)){
            throw new NotFoundException("Article Not Found");
        }

        return this.articleRepository.findOne(articleId);
    }

    @Override
    public List<Article> listAlLOrderedByIdAsc() {
        return this.articleRepository.findAll();
    }

    @Override
    public void create(ArticleBindingModel bindingModel) throws IOException {

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());

        HashSet<Tag> tags = this.findTagsFromString(bindingModel.getTagString());
        Category category = this.categoryRepository.findOne(bindingModel.getCategoryId());

        Article articleEntity = new Article(bindingModel
                .getTitle(), bindingModel
                .getContent(), userEntity, category, tags);

        Article article = this.articleRepository.save(articleEntity);

        MultipartFile[] files = bindingModel.getPictures();

        for (MultipartFile file : files) {
            String encoded = getEncoder().encodeToString(file.getBytes());
            Picture picture = new Picture(
                    encoded,
                    article
            );
            Picture pictureEntity = this.pictureRepository.save(picture);
            article.addPicture(pictureEntity);
        }

        this.articleRepository.saveAndFlush(article);
    }

    @Override
    public Article edit(Integer articleId,ArticleBindingModel articleBindingModel) throws IOException, NotFoundException {
        if(!checkIfArticleExists(articleId)){
            throw new NotFoundException("Article Not Found");
        }

        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());

        Article articleEntity = this.articleRepository.findOne(articleId);

        if (!isUserAuthorOrAdmin(articleEntity)) {
            throw new NotFoundException("You don't have permission to edit this article");
        }

        articleEntity.setCategory(category);
        articleEntity.setContent(articleBindingModel.getContent());
        articleEntity.setTitle(articleBindingModel.getTitle());
        articleEntity.setTags(tags);

        Article article = this.articleRepository.save(articleEntity);

        MultipartFile[] files = articleBindingModel.getPictures();

        for (MultipartFile file : files) {
            String encoded = getEncoder().encodeToString(file.getBytes());
            Picture picture = new Picture(encoded, article);

            Picture pictureEntity = this.pictureRepository.save(picture);
            article.addPicture(pictureEntity);
        }

        this.articleRepository.saveAndFlush(article);
        return articleEntity;

    }

    @Override
    public void delete(Integer articleId) throws NotFoundException {
        if (!checkIfArticleExists(articleId)) {
            throw new NotFoundException("Article Not Found");
        }

        Article article = this.articleRepository.findOne(articleId);

        if (!isUserAuthorOrAdmin(article)) {
            throw new NotFoundException("You don't have permission to edit this article");
        }

        List<Picture> pictures = new ArrayList<>();
        pictures.addAll(article.getPictures());
        List<Tag> tags=new ArrayList<>();
        tags.addAll(article.getTags());
        this.tagRepository.delete(tags);
        this.pictureRepository.delete(pictures);
        this.articleRepository.delete(article);
    }

    private HashSet<Tag> findTagsFromString(String tagString){
        HashSet<Tag> tags=new HashSet<>();

        String[] tagNames=tagString.split(",\\s*");

        for(String tagName: tagNames){
            Tag currentTag=this.tagRepository.findByName(tagName);

            if(currentTag==null){
                currentTag=new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }
            tags.add(currentTag);
        }
        return tags;
    }

    private boolean checkIfArticleExists(Integer articleId){
        if(!this.articleRepository.exists(articleId)){
            return false;
        }
        return true;
    }

    private boolean isUserAuthorOrAdmin(Article article) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }
}
