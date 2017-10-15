$(document).ready(function (){

    window.indexPage=0;
    var numberOfPages=$('#numPages').attr('numOfPages')
    console.log("num of pg")
    console.log(numberOfPages)

    $('#previousInIndex').addClass('disabled')
    if(numberOfPages==1){
        $('#nextInIndex').addClass('disabled')
    }

    $.ajax({
        type:"GET",
        contentType:"application/json",
        url:"listAllArticles/page/" + indexPage + "/size/" +4,
        dataType:"json",
        success:function (articles) {
            loadAllArticles(articles);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }

    });

    $('#search_text').keypress(function (event) {

        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            var dInput = this.value;
            console.log('search word = ' + dInput);

            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/test",
                dataType: 'json',
                success: function (data) {
                    console.log(data)
                    var options = {
                        shouldSort: true,
                        threshold: 0.3,
                        location: 0,
                        distance: 100,
                        maxPatternLength: 32,
                        minMatchCharLength: 1,
                        keys: ['article.title', 'author']
                    };
                    var fuse = new Fuse(data, options);

                    var result = fuse.search(dInput + '');
                    console.log("RESULT");
                    console.log(result);
                    $('#search_result').empty();
                    result.forEach(function (entry) {
                        var category = entry['category'];
                        var article = entry['article'];
                        var author = entry['author'];
                        clone(category, article, author);
                    });
                },
                error: function (e) {
                    console.log("ERROR: ", e);
                },
                done: function (e) {
                    console.log("DONE");
                }
            });

            event.preventDefault();
        }
    });

    $('#previousInIndex').click(function (event) {
        $('#articlesContainer2').empty()
        var currentPage = indexPage - 1;
        indexPage=currentPage;

        $('#nextInIndex').removeClass('disabled')
        if(currentPage<=0){
            $('#previousInIndex').addClass('disabled');
        }

        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/listAllArticles/page/" + currentPage + "/size/" + 4,
            dataType: 'json',
            success: function (articles) {
                console.log(articles)
                $('#articlesContainer2').empty()
                loadAllArticles(articles)
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    })

    $('#nextInIndex').click(function (event) {
        $('#articlesContainer2').empty()
        var currentPage=indexPage+1;
        indexPage=currentPage;

        $('#previousInIndex').removeClass('disabled');
        if(currentPage>=numberOfPages-1){
            $('#nextInIndex').addClass('disabled');
        }

        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/listAllArticles/page/" + currentPage + "/size/" + 4,
            dataType: 'json',
            success: function (articles) {
                console.log(articles)
                $('#articlesContainer2').empty();
                loadAllArticles(articles)
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    })



})

function clone(category, article, author) {
    // get the last a which ID starts with ^= "search_clone"
    var $a = $('a[id^="search_clone_index"]:last');
    // Read the Number from that DIV's ID (i.e: 3 from "klon3")
    // And increment that number by 1
    var num = parseInt($a.prop("id").match(/\d+/g), 10) + 1;

    // Clone it and assign the new ID (i.e: from num 4 to ID "klon4")
    var $klon = $a.clone().prop('id', 'search_clone_index' + num);
    $klon.prop('style', 'display: block');
    $klon.prop('href', '/article/' + article.id);
    $klon.find('h4').text(article.title);
    $klon.find('p').text(article.summary);
    $klon.find('small').text('category: ' + category);
    $klon.find('p1').text('author: ' + author);
    // Finally insert $klon wherever you want
    $('#search_result').append($klon);
}

function loadAllArticles(articles) {

    articles.forEach(function (article) {

        var $div = $('div[id^="gosho"]:last');

        var num = parseInt($div.prop("id").match(/\d+/g), 10) + 1;

        var $klon = $div.clone().prop('id', 'gosho' + num);
        $klon.prop('style', 'display: block');

        $klon.find('h2').text(article.title);
        $klon.find('h3').text(article.summary);
        $klon.find('.author').text(article.author.fullName);
        var tags = article.tags;
        var $p = $klon.find('.tags');
        $p.empty();
        tags.forEach(function (tag) {
            if (tag.name != '') {
                var $a = $('<a></a>');
                /*$a.addClass("btn btn-default btn-xs");*/
                $a.text(tag.name);
                $a.prop('href', '/tag/' + tag.name);

                $p.append($a);
            }
        })
        $klon.find('.vruzka').prop('href','/article/'+article.id);

        $('#articlesContainer2').append($klon);
    })

}

/* When the user clicks on the button,
 toggle between hiding and showing the dropdown content */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.dropbtn')) {

        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}