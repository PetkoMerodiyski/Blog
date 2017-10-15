package blog.entityServices;

import blog.entity.Comment;
import blog.repository.CommentRepository;
import javassist.NotFoundException;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment findById(Integer commentId) throws NotFoundException {

        if (!checkIfCommentExist(commentId)){
            throw new NotFoundException("Comment Not Found!");
        }
        return this.commentRepository.findOne(commentId);
    }

    @Override
    public void delete(Integer commentId) throws NotFoundException {
        if (!checkIfCommentExist(commentId)){
            throw new NotFoundException("Comment Not Found!");
        }

        this.commentRepository.delete(commentId);
    }


    private boolean checkIfCommentExist(Integer commentId){
        if (!this.commentRepository.exists(commentId)){
            return false;
        }
        return true;
    }
}
