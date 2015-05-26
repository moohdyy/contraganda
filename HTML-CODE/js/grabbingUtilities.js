/**
 * Created by moohdyy on 5/25/15.
 */

var diffbotClient;

function createTable(){
    var content = $('#content');
    var tableStr = "<table id='myTable'>";
    tableStr+="<tr><th>url</th><th>date</th><th>title</th><th>diffbot</th><th>select</th></tr></table> ";
    content.html(tableStr);
}

function initSelectAll(){
        $('#selectALL').click(function(event) {  //on click
            if(this.checked) { // check select status
                $('.article-checkbox').each(function() { //loop through each checkbox
                    this.checked = true;  //select all checkboxes with class "checkbox1"
                });
            }else{
                $('.article-checkbox').each(function() { //loop through each checkbox
                    this.checked = false; //deselect all checkboxes with class "checkbox1"
                });
            }
        });
}


function writeResultToTable(pageNr,resultNr, content){
    var rowID = "res_"+pageNr+"_"+resultNr;
    var row = "<tr>";
    row+="<td><href>"+content.url + "</href></td>";
    row+="<td>"+content.date + "</td>";
    row+="<td>"+content.title + "</td>";
    row+="<td><button id='"+rowID+"_button' onclick='getDiffbotResultForSingleURL(\""+content.url+"\",\"#"+rowID+"_button\")' >prepare Data </button></td>";
    row+="<td><input type='checkbox' class='article-checkbox' name='articles' value='"+content.url+"'></td>"
    row+="</tr>";
    $("#myTable").append(row);
}

function getDiffbotResultForSingleURL(url, container){
    $(container).prop("disabled",true);
    diffbotClient.article.get({
        url: url
    }, function onSuccess(response) {
        // output the summary
        console.log(response);
        var link = getDownloadLink({title:response.title,url:url,text:response.text,date:response.meta.DISPLAYDATE});
        $(container).replaceWith(link);
    }, function onError(response) {
        console.log(response);
    });
}

function getDownloadLink(json) {
    var data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(json));
    var title;
    if(json.title){
        title = json.title;
    }else{
        title =$("#site-in").val() +": " +$("#txt-in").val();
    }
    return '<a class="myButt" href="data:' + data + '" download="'+title+'.json">download</a>';
}


function getAllSelectedArticles(){
    var length = $('.article-checkbox:checked').size();
    if(length>0){
        $("#dialog" ).dialog({
            width: 500
        });
        $("#dialog_text").html("Processing " +length +" articles with Diffbot..." );
        var allURLs = [];
        $('.article-checkbox:checked').each(function() {
            allURLs.push($(this).val());
        });
        processURLRecursive(allURLs,0,length);
    }else{
        $("#dialog" ).dialog({
            width: 500
        });
        $("#dialog_text").html("No articles selected!" );
    }

}

function processURLRecursive(urls,index,length){

    if(index <length){
        $("#dialog_actualURL").html("Current URL: "+urls[index]);
        $( "#dialog_progressBar" ).progressbar({
            value: (index/length*100)
        });
        diffbotClient.article.get({
            url: urls[index]
        }, function onSuccess(response) {
            // output the summary
            var json = {title:response.title,url:urls[index],text:response.text,date:response.meta.DISPLAYDATE};
            console.log(json);
            urls[index] = json;
            processURLRecursive(urls,index+1,length);
        }, function onError(response) {
            console.log(response);
        });
    }else{
        $("#dialog_text").html("Processing finished.");
        var urlsJSON = urls;
        $("#dialog_actualURL").html(getDownloadLink(urlsJSON));
        $( "#dialog_progressBar" ).progressbar("destroy");
    }

}






