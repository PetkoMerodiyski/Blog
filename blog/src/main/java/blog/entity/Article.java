package blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="articles")
public class Article {
    private Integer id;

    private String title;

    private String content;

    private User author;

    @JsonBackReference
    private Category category;

    private Set<Tag> tags;

    @JsonBackReference
    private Set<Picture> pictures;

    @JsonBackReference
    private List<Comment> comments;

    public Article(String title, String content, User author, Category category, HashSet<Tag> tags){
        this.title=title;
        this.content=content;
        this.author=author;
        this.category=category;

        this.tags=tags;

        this.pictures = new HashSet<>();

        this.comments=new LinkedList<>();
    }

    public Article(){}

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable=false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(columnDefinition = "text",nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne()
    @JoinColumn(nullable = false,name="authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Transient
    public String getSummary(){
        if(this.getContent().length()>50){
            return this.getContent().substring(0,50)+"...";
        }
        return this.getContent().substring(0,this.getContent().length())+"...";
    }

    @ManyToOne()
    @JoinColumn(nullable = false,name="categoryId")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToMany()
    @JoinColumn(table="articles_tags")
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }


    @OneToMany(mappedBy = "article")
    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public void addPicture(Picture picture){
        this.pictures.add(picture);
    }

    @OneToMany(mappedBy = "article")
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
