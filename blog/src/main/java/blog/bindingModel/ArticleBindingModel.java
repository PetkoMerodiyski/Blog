package blog.bindingModel;

import blog.entity.Comment;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class ArticleBindingModel {

    @NotBlank(message = "Title cannot be empty")
    @Size(min=5,max=30,message = "Title must be between 5 and 30 letters")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    @Size(min=20,message = "Content cannot be less than 20 letters.")
    private String content;

    private Integer categoryId;

    private String tagString;

    private MultipartFile[] pictures;

    private Set<Comment> comments;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public MultipartFile[] getPictures() {
        return pictures;
    }

    public void setPictures(MultipartFile[] pictures) {
        this.pictures = pictures;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
