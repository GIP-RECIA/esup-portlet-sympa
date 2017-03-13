
function handleSubmitUserListsCriteria(e) {
	refreshMyListsTable(false);
	
	//Form is submitting via Ajax so return false to prevent default action
	return false;	
}

$("form.userListCriteriaForm").ready(function () {
    console.log("document.ready userLists.js");

    $(".submitUserListsCriteriaButton").bind("click", handleSubmitUserListsCriteria);

});