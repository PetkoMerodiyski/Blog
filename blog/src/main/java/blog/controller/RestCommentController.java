package blog.controller;

import blog.entity.Article;
import blog.entity.Comment;
import blog.entity.User;
import blog.repository.ArticleRepository;
import blog.repository.CommentRepository;
import blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestCommentController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("article/comment/{id}/page/{page}/size/{size}")
    @PreAuthorize("isAuthenticated()")
    public List<Comment> postCommentByArticleId(@PathVariable("id") Integer id, HttpServletRequest request, @PathVariable("page") Integer page,
                                                @PathVariable("size") Integer size){
        UserDetails user=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity=this.userRepository.findByEmail(user.getUsername());

        String content=request.getParameter("text");

        Comment comment=new Comment(content,userEntity.getFullName(),articleRepository.findOne(id));
        commentRepository.save(comment);

        PageRequest pageRequest = new PageRequest(page, size, Sort.Direction.DESC, "id");

        List<Comment> comments=new ArrayList<>();
        if(commentRepository.findAllByArticle_IdOrderByIdDesc(id)!=null){
            comments=commentRepository.findAllByArticle_IdOrderByIdDesc(id, pageRequest);
        }
        return comments;

    }

    @GetMapping("article/listComments/{articleId}/page/{page}/size/{size}")
    public List<Comment> showCommentsByArticleId(@PathVariable("articleId") Integer articleId,@PathVariable("page") Integer page,
                                                 @PathVariable("size") Integer size){
        PageRequest pageRequest = new PageRequest(page, size, Sort.Direction.DESC, "id");

        List<Comment> comments=new ArrayList<>();
        if(commentRepository.findAllByArticle_IdOrderByIdDesc(articleId)!=null){
            comments=commentRepository.findAllByArticle_IdOrderByIdDesc(articleId, pageRequest);
        }
        return comments;
    }

    @GetMapping("article/commentsSize/{articleId}")
    public Integer getCommentsSize(@PathVariable("articleId") Integer articleId){
        Article article=articleRepository.findOne(articleId);
        Integer pageSize=4;
        int commentsSize=article.getComments().size()+1;
        int numberOfPages=commentsSize/pageSize;

        if(commentsSize%pageSize!=0){
            numberOfPages++;
        }
        return numberOfPages;
    }

}
