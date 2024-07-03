const WRMRequire = require("wrm/require");
console.log("start >>>>>>>>>>>>>>>>>>>>");
WRMRequire('wr!com.atlassian.jira.jira-frontend-plugin:entrypoint-activityTabs').then(function() {
    console.log("middle 1 >>>>>>>>>>>>>>>>>>>>");
    require('jira/activity-tabs/items-lazy-loader').then(function(itemsLazyLoader) {
        console.log("middle 2 >>>>>>>>>>>>>>>>>>>>");
        itemsLazyLoader.registerTab('paginatedreference-tabpanel', {
            version: 1
        });
        console.log("finish >>>>>>>>>>>>>>>>>>>>");
    });
});

AJS.tablessortable.setTableSortable(AJS.$(".aui-table-sortable"));
AJS.$("#issue-tab-panel-user-selector").auiSelect2();
AJS.$('#issue-tab-panel-user-selector').on('change',function(e){
    let selectedUser = e.target.value;
    for( let user of AJS.$('#issue-tab-panel-result-table > tbody > tr > td[headers="updated-user"]')) {
        let trUser = AJS.$(user);
        if(selectedUser == user.getAttribute('value')){
            trUser.parent().show();
        }
        else{
            trUser.parent().hide();
        }
    }
});

AJS.$('#issue-tab-panel-field-selector').on('change',function(e){
    let selectedField = e.target.value;
    for( let field of AJS.$('#issue-tab-panel-result-table > tbody > tr > td[headers="updated-field"]')) {
        let trField = AJS.$(field);
        if(selectedField.toLowerCase() == field.getAttribute('value').toLowerCase()){
            trField.parent().show();
            console.log("Show search field : ", selectedField);
            console.log("Show field : ", field.getAttribute('value'));
        }
        else{
            trField.parent().hide();
            console.log("Hide search field : ", selectedField);
            console.log("Hide field : ", field.getAttribute('value'));
        }
    }
});

JIRA.ViewIssueTabs.onTabReady(function() {
    AJS.$('.tab-content').find('#custom-issue-tabpanel').addClass('hidden');
})
