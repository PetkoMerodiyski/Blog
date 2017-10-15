
$(document).ready(function () {

    var page=0;
    var numberOfPages=$('#numOfCommentPages').attr('numOfPages');

    window.articleId=$('#comentar').attr('articleId');

    $("#previousComment").addClass('disabled');
    if(numberOfPages<=1){
        $("#nextComment").addClass('disabled')
    }

    $.ajax({
        type:"GET",
        contentType: "application/json",
        url:"listComments/"+articleId+"/page/"+page+"/size/"+4,
        dataType: 'json',
        success: function(comments){
            $('#commentContainer').empty();
            console.log(comments.length)
            cloneComment(comments);
        },
        error:function (e) {
            console.log("ERROR",e)
        }
    });


    $("#comment_button").click(function (event) {
        var commentContent=$("#article_comment").val();
        console.log("comment content")
        console.log(commentContent)
        if(!commentContent=="") {
            $.ajax({
                type: "POST",
                url: "comment/" + articleId + "/page/" + 0 + "/size/" + 4,
                data: {text: commentContent},
                success: function (comments) {
                    $("#previousComment").addClass('disabled')
                    if (numberOfPages > 1) {
                        $("#nextComment").removeClass('disabled')
                    }
                    $('#commentContainer').empty();
                    cloneComment(comments);
                },
                error: function (e) {
                    console.log("ERROR", e)
                }
            });
            document.getElementById("errorMessage").style="display:none";

            $.ajax({
                type:"GET",
                url:"commentsSize/"+articleId,
                dataType: 'json',
                success:function(size){
                    console.log("new size")
                    console.log(size);
                    numberOfPages=size;
                    page=0;
                },
                error:function (e) {
                    console.log("ERROR",e)
                }
            });

        }else{
            document.getElementById("errorMessage").style="color: red;dysplay:inherit"
            document.getElementById("errorMessage").innerHTML="Comment cannot be empty";
        }

    });

    $("#previousComment").click(function (event) {
        document.getElementById("errorMessage").style="display:none";
        $('#commentContainer').empty();
        var currentPage=page-1;
        page=currentPage;
        console.log("page")
        console.log(page)

        $('#nextComment').removeClass('disabled')
        if(page<=0){
            $("#previousComment").addClass('disabled');
        }

        $.ajax({
            type:"GET",
            contentType: "application/json",
            url:"listComments/"+articleId+"/page/"+currentPage+"/size/"+4,
            dataType: 'json',
            success: function(comments){
                $('#commentContainer').empty();
                cloneComment(comments);

            },
            error:function (e) {
                console.log("ERROR",e)
            }
        });
    })

    $("#nextComment").click(function (event) {
        document.getElementById("errorMessage").style="display:none";
        $('#commentContainer').empty();
        var currentPage=page+1;
        page=currentPage;
        console.log("page")
        console.log(page)

        $('#previousComment').removeClass('disabled');
        if(page>=numberOfPages-1){
            $('#nextComment').addClass('disabled');
        }

        $.ajax({
            type:"GET",
            contentType: "application/json",
            url:"listComments/"+articleId+"/page/"+currentPage+"/size/"+4,
            dataType: 'json',
            success: function(comments){
                $('#commentContainer').empty();
                cloneComment(comments);

            },
            error:function (e) {
                console.log("ERROR",e)
            }
        });

    })

    /*$('#deleteComment').click(function (event) {
        console.log("tuka sum")
        $.ajax({
            type:"GET",
            contentType: "application/json",
            url:"/deleteComment/"+273,
            dataType: 'json',
            success: function(comments){
                $('#commentContainer').empty();
                cloneComment(comments);

            },
            error:function (e) {
                console.log("ERROR",e)
            }
        });
    })*/


});

function cloneComment(comments){
    comments.forEach(function (comment) {
        var $div = $('div[id^="comment_clone"]:last');

        var num = parseInt($div.prop("id").match(/\d+/g), 10) + 1;

        var $klon = $div.clone().prop('id', 'comment_clone' + num);
        $klon.prop('style', 'display:block');
        $klon.find('#userName').text(comment.authorName);
        $klon.find('#comment_content').text(comment.comment);
        $klon.find('#deleteComment').prop('href','/article/'+articleId+"/comment/"+comment.id);


        $('#commentContainer').append($klon);
    })
}
