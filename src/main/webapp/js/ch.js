var CHAT_USER_MESSAGE_EMPTY = 'Введите сообщение, прежде чем нажимать Enter.';
var CHAT_USER_MESSAGE_ERROR = 'Ошибка при отправке сообщения.';
var CHAT_USER_BANNED = 'Вы были забанены.';
var CHAT_USER_NO_CAPS = 'КАПСИТЬ нельзя. Проверьте caps lock.';
var CHAT_USER_NO_SPAM_SMILES = 'В одном сообщении нельзя использовать 3 и более смайла.';
var CHAT_TOO_LONG_URL = 'Слишком длинный URL. Используйте сокращатели ссылок.';
var user_name = '';

// chat reload interval in ms
var CHAT_RELOAD_INTERVAL = 5000;
var CHAT_CHANNEL_RELOAD_INTERVAL = 300000;
var SC2TV_URL = 'http://sc2tv.ru';
var CHAT_URL =  'http://chat.sc2tv.ru/';
var CHAT_GATE = CHAT_URL + 'gate.php';
var chatTimerId = 0;
var channelList = [];
var userInfo = [];
var moderatorData = '';
var moderatorMessageList = [];
var prevModeratorMessageList = [];
var ignoreList = [];
var isModerator = false;
var isModeratorActive;
var processReplacesMessageInfo = [];
var anon = true;
if ( anon !== true ) {
    $.ajaxSetup( { async: false, cache: false } );

    $.getJSON( CHAT_GATE + '?task=GetUserInfo&ref=' + document.referrer, function( data ) {
        userInfo = data;
    });

    $.ajaxSetup({ async: true, cache: true });
}
else {
    userInfo.type = 'anon';
}

if ( userInfo.type == 'anon' || userInfo.type == 'newbie'
    || userInfo.type == 'bannedInChat' || userInfo.type == 'bannedOnSite' ) {
    smileHtml = '';
}
else {
    smileHtml = '<div id="smile-panel-tab-1">';
    smilePanelTabsHtml = '<span id="smile-panel-pager-1" data-tab-number="1">[ 1 ]</span>';
    var privateStarted = false;
    for( i=0,t=2; i < smilesCount; i++ ) {
        inactiveSmileClass = '';
        if ( smiles[i].private ) {
            if ( !privateStarted ) {
                privateStarted = true;
                smileHtml += '</div><div id="smile-panel-tab-' + t + '">';
                smilePanelTabsHtml += '<span id="smile-panel-pager-' + t
                    + '" data-tab-number="' + t +'">prime</span>';
                smileHtml += '<a href="http://prime.sc2tv.ru/donate" target="_blank">Получить смайлы</a><br/>';
            }

            inactiveSmileClass = '-not-available';
            for( k=0; k < userInfo.roleIds.length; k++){
                if (smiles[i].roles.indexOf( userInfo.roleIds[k] ) !== -1) {
                    inactiveSmileClass = '';
                    break;
                }
            }
        }
        smileHtml += '<img src="' + CHAT_IMG_DIR + smiles[i].img
            +'" title="' + smiles[i].code +'" width="' + smiles[i].width
            + '" height="' + smiles[i].height+ '"class="chat-smile'
            + inactiveSmileClass + '" alt="' + smiles[i].code + '"/>';

        if ( i > 0 && i % 37 == 0 && i < ( smilesCount - 1 ) && !privateStarted ) {
            smileHtml += '</div><div id="smile-panel-tab-' + t + '">';
            smilePanelTabsHtml += '<span id="smile-panel-pager-' + t
                + '" data-tab-number="' + t +'">[ ' + t + ' ]</span>';
            t++;
        }
    }
    smileHtml += '</div>' + smilePanelTabsHtml;
}
var chat_channel_id = 80833;
chat_rules_link = '<a title="Правила чата" href="' + SC2TV_URL + '/chat-rules" target="_blank">rules</a>';
chat_history_link = '<a title="История чата" href="/history.htm" target="_blank">history</a>';
chat_ban_history_link = '<a title="История банов чата" href="/automoderation_history.htm" target="_blank">bans</a>';
chat_vkl_btn = '<span id="chat-on" title="включить чат" style="display:none;">chat</span><span title="отключить чат" id="chat-off">chat</span>';
img_btn = '<span id="smile-text" title="текстовые смайлы" style="display:none;">img</span><span id="smile-img" title="включить смайлы" style="display:none;">img</span><span id="smile-off" title="выключить смайлы">img</span>';
color_btn = '<span id="clr_nick_on" title="включить цветные ники">col</span><span id="clr_nick_off" title="выключить цветные ники">col</span>';
smiles_btn = '<span id="smile-btn">smile</span>';
smile_panel = '<div id="chat-smile-panel">' + smileHtml + '<div id="chat-smile-panel-close">X</div></div>';
divForFullScreen = '<div id="full-screen-place">.</div>';

form_chat = '<div id="chat-form"><form id="chat-form-id" method="post" action=""><input maxlength="1024" type="text" name="chat-text" class="chat-text"/></form>' + chat_vkl_btn + ' ' + img_btn + ' ' + color_btn + ' ' + smiles_btn + ' ' + chat_rules_link + ' ' + chat_history_link + ' ' + chat_ban_history_link + smile_panel + '</div>';

form_anon = '';

form_banned = '<div id="chat-form">' + divForFullScreen + chat_vkl_btn + ' ' + img_btn + ' ' + chat_history_link + ' ' + chat_rules_link +  ' <span>Вы были забанены. </span> <a href="/automoderation_history.htm" target="_blank">Причина</a></div>';

form_newbie = '<div id="chat-form">' + divForFullScreen + chat_vkl_btn + ' ' + img_btn + ' ' + color_btn + ' ' + chat_history_link + ' <span>Вы зарегистрированы менее суток назад.</span></div>';

var chat_channel_id = 0;
autoScroll = 1;

$(document).ready(function(){
    chat_channel_id = QueryString["id"];
    if(QueryString["mod"]!=='1')
        isModerator = false;
    else
        isModerator = true;

    whoStopChat = getParameterByName( 'stop' );
    userInfo.type = 'anon';
    BuildChat( userInfo );
});


function getParameterByName( name ) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.href);

    if( results == null ) {
        return '';
    }
    else {
        return decodeURIComponent(results[1].replace(/\+/g, " "));
    }
}


function StartChat(){
    $.cookie( 'chat-on', '1', { expires: 365, path: '/'} );

    $( '#chat-on').hide();
    $( '#chat-off' ).show();
    $( '#smile-btn' ).show();

    chatTimerId = setInterval( 'ReadChat()', CHAT_RELOAD_INTERVAL );
    if( isModerator ) {
        channelListTimerId = setInterval( 'GetChannelsList()', CHAT_CHANNEL_RELOAD_INTERVAL );
    }

    $( '#chat-form-id' ).show();
    ReadChat( true );
}


function GetChannelId( id ) {
    id = id.replace( /[^0-9]/ig, '' );
    if( id == '' ) {
        id = 0;
    }
    return id;
}


function ReadChat( firstRead ) {
    // проверка, чтобы после отключения чат не обновился
    if ( $.cookie( 'chat-on' ) == '0' ) {
        return;
    }

    if ( firstRead == true ){
        $.ajaxSetup({ ifModified: false, cache: false });
    }
    else {
        $.ajaxSetup({ ifModified: true, cache: true });
    }
    channelId= 0;
    // модеры читают все каналы
    if( QueryString["mod"] == "1" ) {
        $.getJSON( '/json/modChannel', function( jsonData ){
            if ( jsonData != undefined ) {
                var messageList = [];
                messageList = jsonData.messages;
                data = BuildHtml( messageList );
                PutDataToChat( data );
            }
        });
    }
    else {
        $.getJSON( '/json/channel?channelId=' + chat_channel_id, function( jsonData ){
            if ( jsonData != undefined ) {
                var messageList = [];
                messageList = jsonData.messages;
                data = BuildHtml( messageList );
                PutDataToChat( data );
            }
        });
    }
}

function setHeader(xhr) {
    xhr.setRequestHeader('Host', SC2TV_URL);
    xhr.setRequestHeader('Referer', 'http://chat.sc2tv.ru/index.htm?channelId='+channelId);
}
function PutDataToChat( data ) {
    channelId = GetChannelId( chat_channel_id );

   if( isModerator ) {
        data = data.replace('class="censured"', 'class="red"');
        $( '#chat' ).html( data );
        channelClassPath = 'div.channel-' + channelId;
        $( channelClassPath ).attr(
            'style', 'background-color:#333333 !important;'
        );
    }
    else {
        // TODO убрать?
        DIV = document.createElement( 'DIV' );
        DIV.innerHTML = data;
        $( '#chat' ).html( $( 'div.channel-' + channelId, DIV) );


    if (autoScroll == 1) {
        $("#chat").scrollTop(10000000);
    }
   }
}


// всевозможные замены
function ProcessReplaces( messageInfo ) {
    processReplacesMessageInfo = messageInfo;
    var message = messageInfo.message;
    // bb codes
    message = bbCodeToHtml( message );

    // смайлы
    var smilesMode = $.cookie( 'chat-img' );
    message = message.replace( /:s(:[-a-z0-9]{2,}:)/gi, function( match, code ) {
        var indexOfSmileWithThatCode = -1;
        for ( var i = 0; i < smilesCount; i++ ) {
            if ( smiles[ i ].code == code ) {
                indexOfSmileWithThatCode = i;
                break;
            }
        };

        var replace = '';
        if ( indexOfSmileWithThatCode != -1 ) {
            switch( smilesMode ) {
                // text code smiles
                case '2':
                    replace = code;
                    break;
                // img smiles
                case '1':
                default:
                    replace = smileHtmlReplacement[ indexOfSmileWithThatCode ];
                    break;
                // no smiles
                case '0':
                    replace = '';
                    break;
            }
        } else {
            replace = match;
        }

        return replace;
    });

    return message;
}



function BuildChat( dataForBuild ) {
    userInfo.type = 'anon';
    switch( userInfo.type ){
        case 'anon':
            myform = form_anon;
            break;
        case 'newbie':
            myform = form_newbie;
            break;
        case 'bannedInChat':
        case 'bannedOnSite':
            myform = form_banned;
            break;
        default:
            myform = form_chat;
    }

    if ( userInfo.type === 'chatAdmin' && userInfo.roleIds.indexOf( 5 ) !== -1 ) {
        isModerator = true;
    }

    $('#dialog2').html('<div id="add_styles"></div><div class="chat-channel-name"></div><div id="chat"></div>'+myform);

    if ( top === self ) {
        $( '#dialog2' ).css( 'background-color', '#000000' );
    }

    // chat window size
    var chatWindowHeight = getParameterByName( 'height' );

    if ( chatWindowHeight == undefined || chatWindowHeight == '' ) {
        $('#dialog2').css( 'height', $(window).height() +'px' );
        $('#chat').css( 'height', $(window).height() - 20 +'px' );
    }
    else {
        $('#dialog2').css( 'height', chatWindowHeight );
        $('#chat').css( 'height', parseInt( chatWindowHeight ) - 65 + 'px' );
    }

    var chatWindowWidth = getParameterByName( 'width' );

    if ( chatWindowWidth == undefined || chatWindowWidth == '' ) {
        $('#dialog2').css( 'width', '410px' );
        $('#chat').css( 'width', '400px' );
        $('.chat-text').css( 'width', '191px' );
    }
    else {
        $('#dialog2').css( 'width', chatWindowWidth );
        $('#chat').css( 'width', parseInt( chatWindowWidth ) - 10 + 'px' );
        $('.chat-text').css( 'width', parseInt( chatWindowWidth ) - 33 + 'px' );
    }

    chatObj = document.getElementById( 'chat' );

    $( '#chat' ).scroll( function(){
        autoScroll = (chatObj.scrollHeight-chatObj.scrollTop<chatObj.clientHeight+500) ? 1:0;
    });


    StartChat();


    $( '#chat-smile-panel > span').click( function(){
        $( '#chat-smile-panel > div' ).hide();
        smilePanelTabNum = $(this).data('tabNumber');
        $( '#chat-smile-panel > div#smile-panel-tab-' + smilePanelTabNum ).show();
        $( '#chat-smile-panel > span').removeClass( 'active' );
        $(this).addClass( 'active' );
    });

    //toogle color nick btn
    //need refactoring
    $( '#clr_nick_on').click( function(){
        $.cookie( 'chat_color_nicks_off', '0', { expires: 365, path: '/'} );
        $(this).hide();
        $( '#clr_nick_off').show();
    });

    $( '#clr_nick_off').click( function(){
        $.cookie( 'chat_color_nicks_off', '1', { expires: 365, path: '/'} );
        $(this).hide();
        $( '#clr_nick_on').show();
    });
}




// сборка html для канала
function BuildHtml( messageList ) {
    var channelHTML = '';
    /*var messageToUserRegexp = new RegExp(
        '\\[b\\]' + RegExp.escape( userInfo.name ) + '\\[/b\\],',
        'gi'
    );*/

    var messageCount = messageList.length;

    if ( messageCount == 0 ) {
        return '';
    }

    var chatNoColorNicknames = $.cookie( 'chat_color_nicks_off') == '1';

    for( i=0; i < messageCount; i++ ) {
        var nicknameClass = 'nick';
        var color = '';
        var customColorStyle = '';
        var namePrefix = '';

        // сообщения пользователей и системы выглядят по-разному
        switch( messageList[ i ].uid ) {
            // primetime bot
            case '-2':
                nicknameClass = 'primetimebot-nick';
                var textClass = 'primetime_text';
                namePrefix = '<img src="/img/primetime_bot.png" width="16" height="16" class="primetime-bot" />';
                break;
            // system message
            case '-1':
                nicknameClass = 'system-nick';
                var textClass = 'system_text';
                customColorStyle = '';
                break;
            // user message
            default:
                var textClass = 'text';

                // подсветка ников выключена
                if ( chatNoColorNicknames ) {
                    nicknameClass += ' user-2';
                }
                else {
                    color = GetSpecColor( messageList[ i ].uid );
                    // если не блат, то цвет по классу группы
                    if ( color == '' ) {
                        nicknameClass += ' role-' + messageList[ i ].role;
                    }
                    else {
                        customColorStyle = ' style="color:' + color + ';"';
                    }
                }

                if ( messageList[ i ].roleIds.indexOf( 24 ) !== -1 ) {
                    namePrefix = '<img src="/img/donate_01.png" width="12" height="11" class="top-supporter" />';
                }
                break;
        }

        channelId = messageList[ i ].channelId;

        // TODO убрать лишнее

        var userMenu = '';
        if ( userInfo.rid != 8 ) {
            userMenu = 'onClick="getmenu(this,' + messageList[ i ].id + ',' + messageList[ i ].uid + ', ' + channelId + ')" ';
        }

        // подсветка своих сообщений

        var currentMessage = ProcessReplaces( messageList[ i ] );

        channelHTML = '<div class="channel-' + channelId + ' mess message_' + messageList[ i ].id + '">' + namePrefix + '<span' + customColorStyle + ' class="' + nicknameClass + '"' + userMenu + 'title="' + messageList[ i ].date + '">' + messageList[ i ].name + '</span><p class="' + textClass + '">' + currentMessage + '</p></div>' + channelHTML;
    }

    return channelHTML;
}function GetSpecColor( uid ) {
    var color = '';
    switch( uid ) {
        // Laylah
        case '20546':
        // Kitsune
        case '11378':
        // Mary_zerg
        case '65377':
        // Siena
        case '8324':
        //milkSHake
        case '22600':
        //Cuddlez
        case '63034':
            color = '#FFC0CB';
            break;
        // Kas
        case '62395':
            color = '#5DA130';
            break;
            // Usual color of regular users
            break;
        case '7787':// Unstable.
        case '60490':// Twilight_Sparkle
        case '108457':// abilisk
        case '84873':// abilisk
        case '14929':// [7x]Atlant
        case '102924':// Hyperon
            color = '#C9D5E5';
            break;
        default:
            color = '';
    }
    return color;
}

var QueryString = function () {
    // This function is anonymous, is executed immediately and
    // the return value is assigned to QueryString!
    var query_string = {};
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        // If first entry with this name
        if (typeof query_string[pair[0]] === "undefined") {
            query_string[pair[0]] = pair[1];
            // If second entry with this name
        } else if (typeof query_string[pair[0]] === "string") {
            var arr = [ query_string[pair[0]], pair[1] ];
            query_string[pair[0]] = arr;
            // If third or later entry with this name
        } else {
            query_string[pair[0]].push(pair[1]);
        }
    }
    return query_string;
} ();