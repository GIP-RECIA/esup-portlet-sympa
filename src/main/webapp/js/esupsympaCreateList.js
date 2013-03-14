function handleCreateList(dialogDomElement, e) {
	
	cursor_wait();
	
	var createListURLBase = $("#createListURLBase").text();
	

	console.log(createListURLBase);
	
	var listSelectedGroups = new Array();
	
	$("li > input:checked").each(function(index, obj) {
		var requestId = getJqueryObj(obj).parent().find("input[type='hidden'][name='idRequest']").val();
		//console.log(obj);
		//console.log(getJqueryObj(obj));
		//console.log(getJqueryObj(obj).find("input[type='hidden'][name='idRequest']"));
		console.log("checkbox val " + requestId);
		listSelectedGroups.push(requestId);
	});
	

	var uai = $("#userUAI").val() || " ";
	uai = $.trim(uai);
	
	var userAttributes = $("#userAttributes").val();
	
	var typeParam = $("#createListURL_typeParam").html() || " ";
	typeParam = $.trim(typeParam);
	
	var typeParamName = $("#createListURL_typeParamName").html() || " ";
	typeParamName = $.trim(typeParamName);
	
	var type = $("#createListURL_type").html() || " ";
	console.log("type " + type);
	type = $.trim(type);
	
	var queryString = 'operation=CREATE&policy=newsletter';
	
	queryString = queryString + "&type=" + type; 
	
	//queryString = queryString + "&uai=" + uai;
	
	queryString = queryString + userAttributes;
	
	if (listSelectedGroups.length > 0) {
		queryString = queryString + '&editors_aliases=' + encodeURIComponent(listSelectedGroups.join('$'));
	}
	
	var listAddedGroups = getSelectedLists();
	
	if (listAddedGroups.length > 0) {
		queryString = queryString + '&editors_groups=' + encodeURIComponent(listAddedGroups.join('$'));
	}
	
	
	if (typeParam && typeParamName) {
		queryString = queryString + "&type_param=" + encodeURIComponent(typeParamName + "$" + typeParam);
	}
	
	console.log("URL created is " + createListURLBase + "?" + queryString);
		
	//It is in fact the server that will open a connection to the ESCO-SympaRemote php page
	//This is because 1)  It will have access to the site and 2)  Avoids cross domain scripting security limitations
	
	$.ajax({

        async: true,
        type: 'POST',
        url: '/esup-portlet-sympa/servlet-ajax/doCreateList',
        data: {
        	queryString : queryString
        },
        success: function (r) {
            console.log("doCreateList success " + r);
            showResultsDialog(r);            
        },
        error: function (xhr,err) {
            console.log("doCreateList error ");
            showResultsDialog(xhr.responseText);
        },
        complete: function (r) {
        	cursor_clear();
        	getJqueryObj(dialogDomElement).dialog("close");
        }
    });
	
}

function handleCloseResultsDialog(dialogElem) {
	
	dialogElem.dialog("close");
	
	refreshCreateListTable(true);
}

function showResultsDialog(text) {
	$("#resultsDialogText").html(text);
	
	$( "#resultsDialog" ).dialog({
		buttons: [
        {
            text: "Ok",
            click: function() { return handleCloseResultsDialog($(this)); }
        }],
        modal: true,
        dialogClass: "resultsDialog sympaDialog"
	});
}

function getSelectedLists() {
	var listAddedGroups = new Array();
	
	var subscriberCheckbox = $("#createListSubscriberCheckbox");
	
	if (subscriberCheckbox.is(':checked')) {
		listAddedGroups.push(subscriberCheckbox.val());
	}
	
	$("#createListSelectedGroups_list > li").each(function(index, obj) {
		listAddedGroups.push(getJqueryObj(obj).text());
		console.log("Selected group [" + getJqueryObj(obj).text() + "]");
	});
	
	return listAddedGroups;
}

function handleCancelCreateList(dialogDomElement, e) {
    getJqueryObj(dialogDomElement).dialog("close");
}


function showHideToggle(show) {
	// MBD: The tree is closed to ease the search of a group.
	closeOpenNodes();
	
	if (show) {
		$(".createListShowHideToggle").show();
		$("#createListShow").hide();
		$("#createListHide").show();
	} else {
		$(".createListShowHideToggle").hide();
		$("#createListShow").show();
		$("#createListHide").hide();
	}
	
}

function selectGroup(selectedItemInTree) {
	//There should only be one selected, but we must loop anyway being jQuery
    jQuery.each(selectedItemInTree, function (index, item) {
        var jqTreeItem = $(item);
        //jqTreeItem.hide();
        var nodeName = jqTreeItem.data("groupName");

        $("#createListSelectedGroups_list").append('<li>' + nodeName + '</li>');
        
        //Needed to prevent selectedGroup from still being returned as the current selection after clicking right arrow
        $("#createListTree").jstree("deselect_all");
        $("#createListTree").jstree("refresh");
    });
}

function handleCreateListDialogClose(e) {
	$("#tree").jstree("destroy");
    $("#createListDialog").dialog("destroy");
    $("body").unbind("keydown",handleCreateListDialogKeyDown);
}

function handleCreateListDialogKeyDown(e) {
	var key = e.which;
	console.log("2244K8eydown : " + key);
	if (key == 46) {
		$("#left_arrow").click();
		return false;
	}
	return true;
}

function intializeCreateList(uai) {
    initJstree(uai);

    showHideToggle(false);

    $("#createListShow").bind("click", function() {
    	showHideToggle(true);
    });
    
    $("#createListHide").bind("click", function() {
    	showHideToggle(false);
    });
    
    $("#right_arrow").bind("click", function () {
        console.log("Right arrow");
        var selectedItemInTree = $("#createListTree").jstree("get_selected");
        console.log(selectedItemInTree);

        selectGroup(selectedItemInTree);
        
        //$("#createListTree").jstree("refresh");
        
    
    });
    $("#left_arrow").bind("click", function () {
        console.log("Left arrow");

        //Get the selected list item
        var selectedItemInList = $("#createListSelectedGroups_list > li.ui-selected");
        
        //Remove the item in the list		        
        selectedItemInList.remove();
        
        console.log("Refreshing jsTree after removing selection");
        $("#createListTree").jstree("refresh");
    });
    
    
    $("body").bind("keydown",handleCreateListDialogKeyDown);

    $("#createListSelectedGroups_list > *").click(function () {
        // $(this).toggleClass("ui-selected").siblings().removeClass("ui-selected");
    });

    $("#createListSelectedGroups_list").selectable({
        selected: function (event, ui) {
            //Enforce single selection behavior
            var selectedListItem = getJqueryObj(ui.selected);
            console.log("Selected : " + selectedListItem);
            selectedListItem.siblings().removeClass("ui-selected");
        }
    });
    
    bindDrop();
}



/**
 * Initializes the tree from an establishment id
 * @param uai
 */

function initJstree(uai) {
    //When a node is opened, ensure all the drag/drop functionality
    //is binded.  
    $("#createListTree").bind("loaded.jstree", function (event, data) {
        console.log("JSTree loaded.jstree event");
        $("#createListTree").jstree("open_all", -1);
    }).bind("refresh.jstree", function(event, data) {
    	console.log("JSTree refresh event");
    	bindDrag();
    }).bind("open_all.jstree", function(event, data) {
    	console.log("JSTree open_all event");
    	bindDrag();
    })
    .jstree({
        // the list of plugins to include
        //"plugins" : [ "themes", , "ui", "crrm", , "dnd", "search", "types", "hotkeys", "contextmenu"],
        /* GIP RECIA : add plugins "contextmenu", "dnd", "html_data" */
        "plugins": ["themes", "ui", "cookies", "crrm", "types", "json_data"],
        // Plugin configuration
/*"html_data": {
            "data": $("#tree").html()
        },*/
        "json_data": {

            "ajax": {

                "url": "/esup-portlet-sympa/servlet-ajax/jstreeData",
                "traditional" : true,

                "data": function() {
                	return {
                    "establishementId": uai,
                    "selectedGroups" :  getSelectedLists()
                	};
                }, "success": function (data, textStatus, jqXHR) {
                    console.log("JSTree json ajax data loaded");
                    bindDrag();
                }

            }

        },


        /* GIP RECIA : END --> Configuration of contextmenu plugin */
        // the UI plugin - it handles selecting/deselecting/hovering nodes
        "ui": {
            // this makes the node with ID node_4 selected onload
            "select_multiple_modifier": false,
            "select_limit": 1
        },
        // Using types - most of the time this is an overkill
        // Still meny people use them - here is how
        "types": {
            // I set both options to -2, as I do not need depth and children count checking
            // Those two checks may slow jstree a lot, so use only when needed
            "max_depth": -2,
            "max_children": -2,
            // I want only `drive` nodes to be root nodes 
            // This will prevent moving or creating any other type as a root node
            "valid_children": "all",
            "types": {
                // The default type
                "default": {
                    // I want this type to have no children (so only leaf nodes)
                    // In my case - those are files
                    "valid_children": "none"
                    // If we specify an icon for the default type it WILL OVERRIDE the theme icons
                },
                "folder": {
                    "icon": {
                        "image": "/esup-portlet-sympa/media/icons/folder.png"
                    	//"image" : ""
                    },
                    // can have files and other folders inside of it, but NOT `drive` nodes
                    "valid_children": ["group"],
                    "select_node": false
                },
                // The `drive` nodes 
                "group": {
                    "icon": {
                        "image": "/esup-portlet-sympa/media/icons/groupe.png"
                    	//"image" : ""
                    },
                    // can have files and folders inside, but NOT other `drive` nodes
                    "valid_children": "none",
                    "select_node": true
                }
            }
        }


    });
    
}

function bindDrop() {
	console.log("Binding drop!");
	
	$("#createListSelectedGroups div.createListBox").droppable({
        //activeClass: "ui-state-hover",
        hoverClass: "ui-selected",
        greedy: true,
        drop: function (event, ui) {
            console.log("Drop event in createListBox");
            handleDrop(ui.draggable, $(this));
            
            //http://bugs.jqueryui.com/ticket/4550
        }
    });

}

function handleDrop(dragElement, dropElement) {
	console.log("handleDrop");
	
	//$("#createListTree .draggable").draggable("destroy");
	
	var sourcePath, targetPath;
	//console.log(dragElement);
	//console.log(dropElement);
	var elem = getJqueryObj(dragElement).closest("li");
	//console.log(elem);
	
	//In order to prevent a JS bug due to the draggable element being removed
	//$("#createListTree .draggable").draggable("option", "revert", false);
	
	if (elem.length >= 1) {
		//We cannot remove the draggable item right away (caused by the refresh
		//of the tree, so we mark it to do in the stop event
        //http://bugs.jqueryui.com/ticket/4550
		console.log("Marking elem for selectGroup " + elem[0]);
		$("#createListTree").data("selectGroup", elem);
	}
	
}

function bindDrag() {
	//console.log($('#createListTree').html());
	console.log("bindDrag number of nodes : " + $('#createListTree [rel="group"]').length);
	//Add css classes to flag drag/dropable
	
	//The folder nodes are contained within the drive nodes (the parents) so
	//we need to set "greedy" to make sure a drop in a folder takes precedence
	$('#createListTree [rel="group"]').children("a").each(function (idx, val) {
        //console.log(val);
        var jqObj = $(val);
        jqObj.addClass("draggable");
    });
	
	
	console.log("Number of draggables : " + $("#createListTree .draggable").length);
		
	//Add the drag / drop functionality
	$("#createListTree .draggable").draggable({ 
        helper: 'clone',
        revert: true,
        start: function () {
            console.log("Tree Drag Start");
        },
        drag: function () {
            console.log("Drag");
        },
        stop: function (event, ui) {
            console.log("Tree Drag Stop");
        	var elem = $("#createListTree").data("selectGroup");
        	$("#createListTree").data("selectGroup", null);
        	if (elem) {
        		selectGroup(elem);        		
        	}
        
        }
    });

   
}

function cursor_wait() {
    console.log("cursor_wait");

    var dialogElem = $("#waitingDialog");
    
    if (dialogElem.dialog("isOpen")) {
        console.log("cursor_wait already open");
    }

    dialogElem.dialog({
        modal: true,
        resizable: false,
        closeOnEscape: false,
        dialogClass: "waiting-dialog sympaDialog",
        open: function(event, ui) { 
        	//hide close button.
        	$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
        }
    });
    
    
}

// Returns the cursor to the default pointer

function cursor_clear() {
    //$("body").css("cursor", "auto");
    console.log("Cursor clear");
    $("#waitingDialog").dialog('close');
}

/** Let only the root node open. */
function closeOpenNodes() {
	console.log("closeOpenNodes()");
	$("#createListTree").jstree('close_all', -1);
	$("#createListTree").jstree("open_node", 'ul > li:first', false, true);
}