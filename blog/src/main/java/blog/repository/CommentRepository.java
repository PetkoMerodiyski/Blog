package blog.repository;


import blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer>{

    List<Comment> findAllByArticle_IdOrderByIdDesc(Integer articleId,Pageable pageable);
    List<Comment> findAllByArticle_IdOrderByIdDesc(Integer articleId);


}
