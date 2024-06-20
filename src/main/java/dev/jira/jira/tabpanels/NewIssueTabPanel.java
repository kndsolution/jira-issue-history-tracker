package dev.jira.jira.tabpanels;
import com.atlassian.jira.issue.changehistory.ChangeHistory;
import com.atlassian.jira.issue.changehistory.ChangeHistoryItem;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.user.util.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel;
import com.atlassian.jira.plugin.issuetabpanel.IssueTabPanel;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.issue.tabpanels.GenericMessageAction;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.VelocityParamFactory;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.atlassian.velocity.VelocityManager;
import com.atlassian.jira.util.VelocityParamFactory;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.renderer.JiraRendererPlugin;

import javax.inject.Inject;

public class NewIssueTabPanel extends AbstractIssueTabPanel
{
    private static final Logger log = LoggerFactory.getLogger(NewIssueTabPanel.class);
    @JiraImport
    private final VelocityManager velocityManager;
    @JiraImport
    private final ApplicationProperties applicationProperties;
    @JiraImport
    private final VelocityParamFactory velocityParamFactory;
    @JiraImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @JiraImport
    private final RendererManager rendererManager;
    @JiraImport
    private final ChangeHistoryManager changeHistoryManager;
    @JiraImport
    private final UserManager userManager;

    public NewIssueTabPanel(VelocityManager velocityManager
            , VelocityParamFactory velocityParamFactory
            , ApplicationProperties applicationProperties
            , JiraAuthenticationContext jiraAuthenticationContext
            , RendererManager rendererManager
            , ChangeHistoryManager changeHistoryManager
            ,UserManager userManager
    ){
        this.velocityManager = velocityManager;
        this.velocityParamFactory = velocityParamFactory;
        this.applicationProperties = applicationProperties;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.rendererManager = rendererManager;
        this.changeHistoryManager = changeHistoryManager;
        this.userManager = userManager;
    }
    public List getActions(Issue issue, ApplicationUser remoteUser) {
        String webworkEncoding = this.applicationProperties.getString("webwork.i18n.encoding");
        Map<String, Object> context = this.velocityParamFactory.getDefaultVelocityParams();
        context.put("i18n", this.jiraAuthenticationContext.getI18nHelper());
        JiraRendererPlugin renderer = rendererManager.getRendererForType("atlassian-wiki-renderer");
        List<ChangeHistory> changeHistories = changeHistoryManager.getChangeHistories(issue);
        for (ChangeHistory changeHistory:changeHistories) {
            List<org.ofbiz.core.entity.GenericValue> historyItems = changeHistory.getChangeItems();
            int item1 = historyItems.size();
            //Object test = item1.get("newstring");
            context.put("item1", item1);
        }


        Object test = changeHistories.get(0).getChangeItems().get(0);
        GetUserWiki getUserWiki = new GetUserWiki(userManager);
        context.put("changeHistoryManager", changeHistoryManager);
        context.put("getUserWiki", getUserWiki);

        context.put("changeHistories",changeHistories);
        context.put("renderer",renderer);


        String renderedText = this.velocityManager.getEncodedBody("templates/tabpanels/", "new-issue-tab-panel.vm", webworkEncoding, context);
        return Collections.singletonList(new GenericMessageAction(renderedText));
    }
    public boolean showPanel(Issue issue, ApplicationUser remoteUser)
    {
        return true;
    }


}