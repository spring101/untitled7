var url = "http://localhost:8080/action/";

if (!String.prototype.format) {
    String.prototype.format = function() {
        var args = arguments;
        var sprintfRegex = /\{(\d+)\}/g;
        var sprintf = function (match, number) {
            return number in args ? args[number] : match;
        };
        return this.replace(sprintfRegex, sprintf);
    };
}
if (!String.prototype.trim) {
    String.prototype.fulltrim=function(){return this.replace(/(?:(?:^|\n)\s+|\s+(?:$|\n))/g,'').replace(/\s+/g,' ');
    };
}
function makePatternOfTags(avaliableTags, fName){
    var toReturn ="-[";
    var parms = [];
    parms.push(availableTags[fName]);
    if(parms.length>0){
        toReturn+=parms.join("||");
        toReturn+="]+";
        return new RegExp(toReturn, "g");
    }
    else {
        return "";
    }
}
function paramMapToMatrix(fName, map){
    var toReturn= fName+";";
    var params=[];
    var key;
    for (var m in map){
        params.push("{0}={1}".format(m.replace("-", ""), map[m]));
        key = m;
    }
    toReturn+=params.join(";")
    return toReturn;
}
function findTextBetweenTags(foundedTags, text){
    var stringFormatPattern = "(?:{0})(.*)(?:{1})";
    var toReturn = [];
    foundedTags.push("$");
    for(var i=0;i<foundedTags.length-1;i++){
        var pattern = new RegExp(stringFormatPattern.format(foundedTags[i], foundedTags[i+1]), "g");
        var temp = text.match(pattern)[0];
        temp =temp.replace(foundedTags[i], "");
        temp =temp.replace(foundedTags[i+1], "");
        toReturn.push(temp.trim());
    }
    return toReturn;
}
function findTags(avaliableTags, fName, text){
    var pattern = makePatternOfTags(avaliableTags, fName);
    return text.match(pattern);
}
function getParametersConsole(fName, avaliableTags, text){
    var parameters = findTags(avaliableTags,fName, text);
    var values = findTextBetweenTags(parameters, text);
    var paramMap = {};
    for(var i=0; i < parameters.length; i++){
        paramMap[parameters[i]] = values[i];
    }
    return paramMap;
}