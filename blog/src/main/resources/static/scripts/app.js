$(document).ready(function () {
    window.page = 0;
    window.numberOfPagesInCategory=$('.pageNumber').attr('number-of-pages');

    var categoryId = $('#search_in_category').attr('data-categoryId');

    $('#previous').addClass('disabled')
    if(numberOfPagesInCategory==1){
        $('#next').addClass('disabled')
    }

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/listArticless/" + categoryId + "/page/" + 0 + "/size/" + 4,
        dataType: 'json',
        success: function (articles) {
            loadArticles(articles)
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });

    $('#search_in_category').keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            var dInput = this.value;
            console.log("search value in category: " + dInput);
            var categoryId = $('#search_in_category').attr('data-categoryId');
            console.log('categoryId = ' + categoryId)

            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/searchInCategory/" + categoryId,
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    var options = {
                        shouldSort: true,
                        threshold: 0.3,
                        location: 0,
                        distance: 100,
                        maxPatternLength: 32,
                        minMatchCharLength: 1,
                        keys: ['title']
                    };
                    var fuse = new Fuse(data, options);
                    var result = fuse.search(dInput + '');
                    console.log(result);
                    $('#search_result_from_category').empty();
                        clone2(result);
                },
                error: function (e) {
                    console.log("ERROR: ", e)
                }
            });
            event.preventDefault();
        }

    });

    $('.pageNumber').click(function (event) {
        $('#search_result_from_category').empty()
        var $a = event.target;
        var pageNumber = $a.text;

        page = pageNumber-1;

        if(page>0){
            $('#previous').removeClass('disabled')
        }else{
            $('#previous').addClass('disabled')
        }

        if(page<numberOfPagesInCategory-1){
            $('#next').removeClass('disabled')
        }else{
            $('#next').addClass('disabled')
        }

        var categoryId = $('#search_in_category').attr('data-categoryId');
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/listArticless/" + categoryId + "/page/" + page + "/size/" + 4,
            dataType: 'json',
            success: function (articles) {
                console.log(articles)
                $('#articlesContainer').empty()
                loadArticles(articles)
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    })

    $('#previous').click(function (event) {
        $('#search_result_from_category').empty()
        var currentPage = page - 1;
        page=currentPage;

        $('#next').removeClass('disabled')
        if(currentPage<=0){
            $('#previous').addClass('disabled');
        }

        var categoryId = $('#search_in_category').attr('data-categoryId');
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/listArticless/" + categoryId + "/page/" + currentPage + "/size/" + 4,
            dataType: 'json',
            success: function (articles) {
                console.log(articles)
                $('#articlesContainer').empty()
                loadArticles(articles)
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    })

    $('#next').click(function (event) {
        $('#search_result_from_category').empty()
        var currentPage=page+1;
        page=currentPage;

        $('#previous').removeClass('disabled')
        if(currentPage>=numberOfPagesInCategory-1){
            $('#next').addClass('disabled');
        }

        var categoryId = $('#search_in_category').attr('data-categoryId');
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/listArticless/" + categoryId + "/page/" + currentPage + "/size/" + 4,
            dataType: 'json',
            success: function (articles) {
                console.log(articles)
                $('#articlesContainer').empty();
                loadArticles(articles)
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    })


});



function clone2(articles) {

    articles.forEach(function (article) {
        var $a = $('a[id^="search_clone"]:last');
        // Read the Number from that DIV's ID (i.e: 3 from "klon3")
        // And increment that number by 1
        var num = parseInt($a.prop("id").match(/\d+/g), 10) + 1;

        // Clone it and assign the new ID (i.e: from num 4 to ID "klon4")
        var $klon = $a.clone().prop('id', 'search_clone' + num);
        $klon.prop('style', 'display: block');
        $klon.prop('href', '/article/' + article.id);
        $klon.find('h4').text(article.title);
        $klon.find('p').text(article.summary);
        // Finally insert $klon wherever you want
        $('#search_result_from_category').append($klon);
    })

}

function loadArticles(articles) {

    articles.forEach(function (article) {
        // get the last a which ID starts with ^= "search_clone"
        var $div = $('div[id^="pesho"]:last');
        // Read the Number from that DIV's ID (i.e: 3 from "klon3")
        // And increment that number by 1
        var num = parseInt($div.prop("id").match(/\d+/g), 10) + 1;

        // Clone it and assign the new ID (i.e: from num 4 to ID "klon4")
        var $klon = $div.clone().prop('id', 'pesho' + num);
        $klon.prop('style', 'display: block');

        $klon.find('h2').text(article.title);
        $klon.find('.summary').text(article.summary);
        $klon.find('.author').text(article.author.fullName);
        var tags = article.tags;
        var $p =  $klon.find('.tags');
        $p.empty();
        tags.forEach(function (tag) {
            if (tag.name != ''){
                var $a = $('<a></a>');
                $a.text(tag.name);
                $a.prop('href', '/tag/' + tag.name);

                $p.append($a);
            }
        })

        $klon.find('.connectionWithArticleFromCategory').prop('href', '/article/' + article.id);
        // Finally insert $klon wherever you want
        $('#articlesContainer').append($klon);

    })
}
