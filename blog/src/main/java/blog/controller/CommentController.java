package blog.controller;

import blog.entity.Comment;
import blog.entityServices.CommentService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/article/{articleId}/deleteComment/{commentId}")
    public String deleteCommentById(@PathVariable("commentId") Integer commentId,@PathVariable("articleId") Integer articleId) throws NotFoundException {
        this.commentService.delete(commentId);
        return "redirect:/article/"+articleId;
    }
}
