var jsonContents = {};


function init(){
    var fileInput = document.getElementById('fileInput');
    var fileDisplayArea = document.getElementById('fileDisplayArea');

    fileInput.addEventListener('change', function(e) {
        var file = fileInput.files[0];
        var reader = new FileReader();
        reader.onload = function(e) {
            console.log("give Name AAAAA");
            //fileDisplayArea.innerText = reader.result;
            jsonContents["AAAAA"] = JSON.parse(reader.result);
            console.log(jsonContents);
        }
        reader.readAsText(file);
    });
}

function getJSONFromFile(){
    console.log(this);
}