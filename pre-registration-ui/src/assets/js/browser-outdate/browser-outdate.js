$buoop = {
    required:{e:-4,f:-1,o:-3,s:-1,c:-3},insecure:true,unsupported:true,style:"top",api:2020.12,
    reminder: 24,
    reminderClosed: 150,
    onshow: function(infos){},
    onclick: function(infos){},
    onclose: function(infos){},
    l: false,
    text: "Votre navigateur, {brow_name}, est trop ancien : <a {up_but}> mise à jour </a>",
    newwindow: true,
    url: null,
    noclose:false,
    nomessage: false,
    jsshowurl: "//browser-update.org/update.show.min.js",
    container: document.body,
    no_permanent_hide: false,
};

function $buo_f(){
    var e = document.createElement("script");
    e.src = "//browser-update.org/update.min.js";
    document.body.appendChild(e);
};
try {document.addEventListener("DOMContentLoaded", $buo_f,false)}
catch(e){window.attachEvent("onload", $buo_f)}
