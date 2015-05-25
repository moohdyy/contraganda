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
    var row = "";
    for(var i =0;i<content.length;i++){
        row+="<td>"+content[i] + "</td>";

    }
    $("#res_"+pageNr+"_"+resultNr).html(row);
}


