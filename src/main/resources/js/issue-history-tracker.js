const WRMRequire = require("wrm/require");
console.log("start >>>>>>>>>>>>>>>>>>>>");
WRMRequire('wr!com.atlassian.jira.jira-frontend-plugin:entrypoint-activityTabs').then(function() {
    console.log("middle 1 >>>>>>>>>>>>>>>>>>>>");
    loadHistoryTracker();
});


JIRA.ViewIssueTabs.onTabReady(function() {
    loadHistoryTracker();
})

function format(el) {
    let avatarId = jQuery('#issue-tab-panel-user-selector option[value="'+el.id+'"]').attr('avatarId');
    console.log(">>>", avatarId);
    return "<span class='user-hover user-hover-replaced'>"+"<span class=\"aui-avatar aui-avatar-small\"><span class=\"aui-avatar-inner\"><img class=\"aui-ss-entity-icon rounded\" src='"+AJS.contextPath()+"/secure/useravatar?size=small&ownerId="+el.id+"&avatarId="+avatarId+"'></span></span>  "+el.text+"</span>";
}

function filterChange(){
    let selectedUsers = AJS.$('#issue-tab-panel-user-selector').val();
    let selectedField = AJS.$('#issue-tab-panel-field-selector').val();
    for( let tr of AJS.$('#issue-tab-panel-result-table > tbody > tr')) {
        // user
        let row = AJS.$(tr);
        let trUser = row.find('td[headers="updated-user"]');
        let trField = row.find('td[headers="updated-field"]');
        let isShowUser = selectedUsers == null || selectedUsers.length==0 || selectedUsers.includes(trUser.attr('value'));
        let isShowField = selectedField == null || selectedField.length==0 || selectedField.toLowerCase()==trField.attr('value').toLowerCase();
        //AJS.$.inArray(trUser.attr('value'), selectedUsers) >= 0;
        console.log(trUser.attr('value'),trUser, isShowUser);
        console.log(trField.attr('value'),trField, isShowField, selectedField);
        if (!isShowUser) {
            row.hide();
            continue;
        }
        if(!isShowField){
            row.hide();
            continue;
        }
        row.show();
    }
}

function loadHistoryTracker(){
    AJS.$("#issue-tab-panel-user-selector").auiSelect2({formatResult: format,
                                                       formatSelection: format});
    AJS.$("#issue-tab-panel-field-selector").val('').auiSelect2();

       console.log("auiSelect2 >>>>>>>>>>>>>>>>>>>>");
       AJS.tablessortable.setTableSortable(AJS.$(".aui-table-sortable"));
       console.log("tablessortable >>>>>>>>>>>>>>>>>>>>");

       AJS.$('#issue-tab-panel-user-selector').off('change');
       AJS.$('#issue-tab-panel-user-selector').on('change',function(e){
           let selectedUsers = AJS.$(e.target).val();

           filterChange();
       });
       AJS.$('#issue-tab-panel-field-selector').off('change');
       AJS.$('#issue-tab-panel-field-selector').on('change',function(e){
           filterChange();

       });
       AJS.$("#reset-button").off('click');
       AJS.$("#reset-button").on('click', function() {
           AJS.$('#issue-tab-panel-user-selector').val('').auiSelect2();
           AJS.$('#issue-tab-panel-field-selector').val('').auiSelect2();
           filterChange();
       });
}
