package blog.entity;

import javax.persistence.*;

@Entity
@Table(name="comments")
public class Comment {
    private Integer id;

    private String comment;

    private String authorName;

    private Article article;

    public Comment(){}

    public Comment(String comment,String authorName,Article article){
        this.comment=comment;
        this.authorName=authorName;
        this.article=article;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="comment",nullable = false,columnDefinition = "text")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Column(nullable = false,name="authorName")
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String author) {
        this.authorName = author;
    }

    @ManyToOne()
    @JoinColumn(name="articleId")
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
