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


JIRA.ViewIssueTabs.onTabReady(function() {
    AJS.$('.tab-content').find('#custom-issue-tabpanel').addClass('hidden');
})
