package blog.bindingModel;


import javax.validation.constraints.NotNull;

public class CommentBindingModel {

    @NotNull
    private String comment;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
