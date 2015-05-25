var newsSearch;


function startSearch(){
    var topic = $("#txt-in").val();
    var site = $("#site-in").val();
	console.log( "topic: "+ topic + ", site: " +site);
    newsSearch.setSiteRestriction(site);
	newsSearch.execute(topic);
	
}

function searchComplete() {

    // Check that we got results
    var currentPage = newsSearch.cursor.currentPageIndex;
    if (newsSearch.results && newsSearch.results.length > 0) {
        console.log(newsSearch);
        console.log("currentPage "+currentPage);

        for (var i = 0; i < newsSearch.results.length; i++) {
            var resultData ={};
            resultData.url = newsSearch.results[i].unescapedUrl;
            resultData.date = newsSearch.results[i].publishedDate;
            resultData.title = newsSearch.results[i].title;
            writeResultToTable(currentPage,i,resultData);
        }
    }
    if(currentPage <7) {
        newsSearch.gotoPage(currentPage+1);
    }
}

function createNewssearch(){
    // Create a News Search instance.
    newsSearch = new google.search.NewsSearch();

    // Set searchComplete as the callback function when a search is
    // complete.  The newsSearch object will have results in it.
    newsSearch.setSearchCompleteCallback(this, searchComplete, null);
    newsSearch.setResultSetSize(8);  // 8 x 8 = maximum
    newsSearch.setRestriction(google.search.Search.RESTRICT_EXTENDED_ARGS, {"ned": "en"}); // english
    //newsSearch.setResultOrder(google.search.Search.ORDER_BY_DATE);   //default is ordered by relevance
    // Include the required Google branding
    google.search.Search.getBranding('branding');
}



function myIP() {
    $.get( "http://api.hostip.info/get_html.php", function( data ) {
        var hostipInfo = data.split("\n");
        for (i=0; hostipInfo.length >= i; i++) {
            var ipAddress = hostipInfo[i].split(": ");
            if ( ipAddress[0] == "IP" ) {
                console.log(ipAddress[1]);
                return;
            }
        }
    });
}