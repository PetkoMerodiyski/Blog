package blog.bindingModel;

import blog.entity.Article;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

public class SearchDTO {

    @JsonProperty("category")
    private String categoryName;
    @JsonProperty("article")
    private Article articles;
    @JsonProperty("author")
    private String authorName;

    public SearchDTO(String categoryName, Article articles,String authorName) {
        this.categoryName = categoryName;
        this.articles = articles;
        this.authorName=authorName;
    }

    public SearchDTO() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Article getArticles() {
        return articles;
    }

    public void setArticles(Article articles) {
        this.articles = articles;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
