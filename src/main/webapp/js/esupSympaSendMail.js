var esupSympa = esupSympa || {};


esupSympa.init = function ($, namespace, urlSendMail, userName, userAddr) {
	var priv= new Object();
	var portletId = namespace;
	
	var listAddr=""; //adresse de la liste destinatrice du message 
	
	esupSympa[namespace] = priv;
	
	priv.jq = $.noConflict(true);
	$=priv.jq;
	pId="#"+namespace+"_";
	
	console.log("OK INIT " + namespace + " url " + urlSendMail);
	
	function isBlank(str) {
	    return (!str || /^\s*$/.test(str));
	}
	
	function showErr(text, erreurDiv) {
		
	    var erreurTemp= 600;
	    erreurDiv.html(text);
	    erreurDiv.show(erreurTemp);
	    setTimeout(() => {
	    	 erreurDiv.hide(erreurTemp);
		}, 30000);
	}
	 
	
	priv.handleSendSimpleEmail = function handleSendSimpleEmail() {

	//    var from = $(pId+"simple_email_from").html();
	//    var userName = $(pId+"simple_email_from").html();
	    var subject = $(pId+"simple_email_subject").val();
	    var message = $(pId+"simple_email_message").val();
	    var erreurDiv=$(pId+"simple_email_erreur");
	    
	    if (isBlank(userAddr)) {
	    	showErr("Manque l'adresse de l'expéditeur du message", erreurDiv);
	    }
	    if (isBlank(userName)) {
	    	showErr("Manque le nom de l'expéditeur du message", erreurDiv);
	    }
	    if (isBlank(listAddr)) {
	    	showErr("Aucune adresse de destination.", erreurDiv);
	    	return;
	    }
	    if (isBlank(subject)) {
	    	showErr("Le sujet ne doit pas être vide.", erreurDiv);
	    	return;
	    } 
	    
	    if (isBlank(message)) {
	    	showErr("Le message ne doit pas être vide.", erreurDiv);
	    	return;
	    }
	    
	    priv.jq.ajax({
	        async: true,
	        type: 'POST',
	      //  url: '/esup-portlet-sympa/servlet-ajax/sendEmail',
	        url: urlSendMail,
	        contentType: "application/x-www-form-urlencoded;charset=utf-8",
	        data: {
	            "fromAddress": userAddr,
	            "fromName" : userName,
	            "toAddress": listAddr,
	            "subject": subject,
	            "message": message,
	            "portletNamespace": namespace
	        },
	        success: function (r) {
	            console.log("sendMail success" + r);
	            $(pId+"reciaModal").modal("hide");
	            listAddr=";"
	        },
	        error: function (r) {
	            console.log("senMail error");
	            console.log(r);
	            showErr("erreur: " + r.responseText, erreurDiv);
	        },
	        complete: function (r) {
	        }
	    });
	};
	
	priv.initMail = function initFormMail(mailTo) {
		listAddr = mailTo;
		$(pId+"simple_email_to").html(listAddr);
		$(pId+"simple_email_subject").val(null);
		$(pId+"simple_email_message").val("");
		$(pId+"simple_email_erreur").hide();
	}
	
	priv.checkList = function checkList(){
		var seeSubscriber = $(pId+"subscriber")[0].checked;
		var seeOwner = $(pId+"owner")[0].checked;
		var seeEditor = $(pId+"editor")[0].checked;
		var cptVisible = 0;
		var caption = $(pId+"sympa-result > caption" );
		var text = caption.html();
		
		var lignes = $(pId+"sympa-result > tbody > tr").each(
				function(index) {
			//		console.log(index);
					var show = false;
					show = seeSubscriber && $(this).hasClass('subscriber');
					show = show || (seeOwner && $(this).hasClass('owner')) ;
					show = show || (seeEditor && $(this).hasClass('editor')) ;
					if (show) {
						if ((++cptVisible % 2) == 0) {
							$(this).addClass('portlet-table-alternate');
						} else {
							$(this).removeClass('portlet-table-alternate');
						}
				//		console.log('show');
						$(this).show();
					} else {
					//	console.log('hide');
						$(this).hide();
					}
				}
		);
		text = " " + cptVisible + " Résultat";
		if (cptVisible > 1) {
			text = text + "s";
		} 
		caption.html(text);
		console.log(lignes);
	}
	
	return priv;
};
