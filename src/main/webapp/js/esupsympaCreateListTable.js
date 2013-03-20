
function handleOpenContextHelp(e) {
	$("#contextHelpDialog").dialog({
        width: 650,
        modal: true,
        buttons: [{
            text: $("#okButtonText").html(),
            click: function (e) {
            	$("#contextHelpDialog").dialog("close");
            }
        }],
        dialogClass: "sympaDialog"
    });
}

function handleOpenCreateList(e) {
    var uai = $("#userUAI").val();

    var source = e.target;
    console.log("handleOpenCreateList source : " + source);

    var row = getJqueryObj(source).closest("tr");
    
    console.log("Row inner html " + row.html());
    
    var modelIdInput = row.find("input[type='hidden'][name='modelId']");
    console.log("model id html : " + modelIdInput.html() + " Len: " + modelIdInput.length);

    var modelId = modelIdInput.val();
    
    console.log("model id : " + modelId);

    var modelParam = row.find("input[type='hidden'][name='modelParam']").val();

    var listDescriptionObj = row.find("div.listDescription");
    
    console.log("List desc len: " + listDescriptionObj.length);
    var listDescription = row.find("div.listDescription").html();

    console.log("List desc : " + listDescription);
    
    var ajaxServletUrl = $('#ajaxServletUrl').val().split(";")[0];
    
    $.ajax({

        async: true,
        type: 'POST',
        url: ajaxServletUrl + '/loadCreateList',
        data: {
            "establishementId": uai,
            "modelId": modelId,
            "listDescription": listDescription,
            "modelParam": modelParam
        },
        success: function (r) {
            console.log("loadCreateList success");

            $("#createListDialog").html(r);

            intializeCreateList(uai);

            $("#createListDialog").dialog({
                width: 860,
                modal: true,
                dialogClass: "createListDialog sympaDialog",
                close: handleCreateListDialogClose,
                buttons: [{
                    text: $("#createButtonText").html(),
                    click: function (e) {
                        handleCreateList(this, e);
                    }
                }, {
                    text: $("#cancelButtonText").html(),
                    click: function (e) {
                        handleCancelCreateList(this, e);
                    }
                }]
            });

        },
        error: function (r) {
            console.log("loadCreateList error");
        },
        complete: function (r) {

        }
    });
}


$(document).ready(function () {
    console.log("document.ready createListTable.js");

    $("img.createListButton").bind("click", handleOpenCreateList);

    $("img.contextHelpButton").bind("click", handleOpenContextHelp);


});


