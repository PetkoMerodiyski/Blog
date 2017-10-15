package blog.repository;

import blog.entity.Article;
import blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Integer> {

    List<Article> findAllByCategory_IdOrderByIdAsc(Integer categoryId, Pageable pageable);
    List<Article> findAllByCategory_IdOrderByIdAsc(Integer categoryId);


    Page<Article> findAll(Pageable pageable);

    Integer countAllByCategory_Id(Integer categoryId);

}
