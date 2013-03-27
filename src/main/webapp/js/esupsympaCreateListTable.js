
function handleOpenContextHelp(e, targetPane, targetDialog) {
	$(targetDialog).dialog({
        width: 650,
        modal: true,
        buttons: [{
            text: $(targetPane + " .okButtonText").text().trim(),
            click: function (dialogElem) {
            	$(this).dialog("close");
            }
        }],
        dialogClass: "sympaDialog"
    });
}

function openCreateFormDialog(e, targetPane, targetDialog) {
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
    var listDescription = row.find("div.listDescription").html().trim();

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

            var staticContent = $(targetPane + " div.subTitleText").html();
            staticContent = staticContent.replace("{0}", listDescription);
            $(targetDialog).html(staticContent + r);

            intializeCreateList(uai);

            $(targetDialog).dialog({
                width: 860,
                modal: true,
                dialogClass: "createListDialog sympaDialog",
                close: handleCreateListDialogClose,
                buttons: [{
                    text: $(targetPane + " div.validButtonText").text().trim(),
                    click: function (e) {
                        handleCreateList(this, e);
                    }
                }, {
                    text: $(targetPane + " div.cancelButtonText").text().trim(),
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

function handleOpenCreateList(e) {
	openCreateFormDialog(e, "#tabs-2", "#createListDialog");
}

/**
 * MBD: The update list form is the creation form.
 * Indeed, to modify the list, we simply recreate it.
 */
function handleOpenUpdateList(e) {
	openCreateFormDialog(e, "#tabs-3", "#updateListDialog");
}

function HandleOpenCreateHelp(e) {
	handleOpenContextHelp(e, "#tabs-2", "#createContextHelpDialog");
}

function HandleOpenUpdateHelp(e) {
	handleOpenContextHelp(e, "#tabs-3", "#updateContextHelpDialog");
}

$(document).ready(function () {
    console.log("document.ready createListTable.js");

    $("img.createListButton").bind("click", handleOpenCreateList);
    
    $("img.updateListButton").bind("click", handleOpenUpdateList);
    
    $("img.closeListButton").bind("click", handleCloseList);

    $("#tabs-2 img.contextHelpButton").bind("click", HandleOpenCreateHelp);

    $("#tabs-3 img.contextHelpButton").bind("click", HandleOpenUpdateHelp);
});


