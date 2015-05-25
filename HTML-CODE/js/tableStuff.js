/**
 * Created by moohdyy on 5/25/15.
 */

var diffbotClient;

function createTable(){
    //create the 64-table
    var content = $('#content');
    var tableStr = "<table style='width:100%;table-layout: fixed;'>";
    tableStr+="<tr><th>url</th><th>date</th><th>title</th><th>diffbot</th>";
    var resID = "";
    for (var page = 0; page < 8; page++){
        for ( var result = 0; result < 8 ; result++){
            resID = "res_"+page+"_"+result;
            tableStr+="<tr id='"+resID+ "'></tr>";
        }
    }
    tableStr+="</table> ";
    content.html(tableStr);
}


function writeResultToTable(pageNr,resultNr, content){
    var rowID = "res_"+pageNr+"_"+resultNr;
    var row = "";
    row+="<td style='word-wrap:break-word'><href>"+content.url + "</href></td>";
    row+="<td>"+content.date + "</td>";
    row+="<td>"+content.title + "</td>";
    row+="<td id='"+rowID+"_button'><button onclick='getDiffbotResultForURL(\""+content.url+"\",\"#"+rowID+"_button\")' >prepare Data </button></td>";
    $("#"+rowID).html(row);
}

function getDiffbotResultForURL(url, container){
    diffbotClient.article.get({
        url: url
    }, function onSuccess(response) {
        // output the summary
        console.log(response);

        downloadJSON({title:response.title,url:url,text:response.text,date:response.meta.DISPLAYDATE},container);
    }, function onError(response) {
        console.log(response);
        switch(response.errorCode) {
            case "401":
                break;
            case "404":
                break;
            case "500":
                break;
        }
    });
}

function downloadJSON(json, container) {
    var data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(json));
    $('<a href="data:' + data + '" download="'+json.title+'.json">download JSON</a>').appendTo(container);
}
