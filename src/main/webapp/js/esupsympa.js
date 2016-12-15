/*
 * Top level javaScript file available to all pages.
 */

//IE does not define this by default
if ("undefined" === typeof window.console)
{
	    window.console = {
	        "log": function() { }
	    };	
}

//Another IE fix, prevents IE from cacheing ajax calls
$.ajaxSetup({
    cache: false
});

function styleTable(tableElem) {
	$("tbody tr:even", tableElem).addClass("even");
	$("tbody tr:odd", tableElem).addClass("odd");
}

function handleSendSimpleEmail(e) {
    console.log("Clicked");

    var from = $("#simple_email_from").html();
    var to = $("#simple_email_to").html();
    var subject = $("#simple_email_subject").val();
    var message = $("#simple_email_message").val();
    var portletNamespace = $("#portletNamespace").html();
    
    console.log("Clicked" + message);

    $.ajax({
        async: true,
        type: 'POST',
        url: '/esup-portlet-sympa/servlet-ajax/sendEmail',
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        data: {
            "fromAddress": from,
            "toAddress": to,
            "subject": subject,
            "message": message,
            "portletNamespace": portletNamespace
        },
        success: function (r) {
            console.log("newFileOrFolder success");
        },
        error: function (r) {
            console.log("newFileOrFolder error");
        },
        complete: function (r) {
            $("#simpleSmtpDialog").dialog("close");
        }
    });
}

function refreshUpdateListTable(invalidateCache) {
	console.log("refreshUpdateListTable");

  $("form.userListCriteriaForm input.tabIndex").val("2");
  $("form.userListCriteriaForm input.invalidateCache").val(invalidateCache);
  $("form.userListCriteriaForm").submit();
}

function refreshCreateListTable(invalidateCache) {
	console.log("refreshCreateListTable");

  $("form.userListCriteriaForm input.tabIndex").val("1");
  $("form.userListCriteriaForm input.invalidateCache").val(invalidateCache);
  $("form.userListCriteriaForm").submit();
}

function refreshMyListsTable(invalidateCache) {
	console.log("refreshMyListsTable" );
  
  $("form.userListCriteriaForm input.tabIndex").val("0");
  $("form.userListCriteriaForm input.invalidateCache").val(invalidateCache);
  $("form.userListCriteriaForm").submit();
}

(function ($) {

    $(document).ready(function () {

        console.log("doc ready");
        
        styleTable($("div.sympaTopLevel table.userListsTable"));

        $(".mailLink").bind('click', function (e) {

            console.log("mailLink");

            var type = $("#emailUtilType").val();
            var toObj = e.target;
            var toJqObj = getJqueryObj(toObj); 
            var to = $.trim(toJqObj.text());

            //Just show the dialog and return
            if (type == "SIMPLE") {
                $("#simpleSmtpDialog #simple_email_to").html(to);

                $("#simpleSmtpDialog").dialog({
                    width: 550,
                    dialogClass: "simpleSmptDialog sympaDialog",
                    buttons: [{
                        text: $("#simpleSmtpSubmitText").html(),
                        click: function (e) {
                            handleSendSimpleEmail(e);
                        }
                    }, {
                        text: $("#simpleSmtpCancelText").html(),
                        click: function (e) {
                            $("#simpleSmtpDialog").dialog("close");
                        }
                    }]
                });
                return false;
            }

            var url = $("#emailUtilURL").val();
            console.log("Mail link url is " + url);

            var title = $("#simpleSmtpDialog").attr("title");

            window.open(url + '&to=' + to, title, "toolbar=no,location=no,status=no,scrollbars=yes,resizable=yes,width=800,height=600,left=100,top=100");

            return false;

        });
        
        //Make sure ajax loaded data is present
        //refreshMyListsTable();

    }); // doc ready



})(jQuery);


/**
 * Utility function to get the jquery object.  
 * @param elem
 * @returns
 */

function getJqueryObj(elem) {

    if (elem instanceof jQuery) {
        //Already a jquery object
        return elem;
    } else {
        //A dom element
        return $(elem);
    }
}