package blog.entity;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="pictures")
public class Picture {
    private Integer id;

    private String pictureData;

    private Article article;

    public Picture(String pictureData, Article article){
        this.pictureData=pictureData;
        this.article = article;
    }

    public Picture() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="picture",nullable = false, columnDefinition = "text")
    public String getPictureData() {
        return pictureData;
    }

    public void setPictureData(String pictureData) {
        this.pictureData = pictureData;
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
