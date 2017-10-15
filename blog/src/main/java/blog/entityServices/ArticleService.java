package blog.entityServices;

import blog.bindingModel.ArticleBindingModel;
import blog.entity.Article;
import blog.entity.Category;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    Article findById(Integer articleId) throws NotFoundException;

    List<Article> listAlLOrderedByIdAsc();

    void create(ArticleBindingModel articleBindingModel) throws IOException;

    Article edit(Integer articleId,ArticleBindingModel articleBindingModel) throws IOException, NotFoundException;

    void delete(Integer articleId) throws NotFoundException;
}
