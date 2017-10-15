package blog.entityServices;

import blog.entity.Comment;
import javassist.NotFoundException;

public interface CommentService {
    Comment findById(Integer commentId) throws NotFoundException;

    void delete(Integer commentId) throws NotFoundException;
}
