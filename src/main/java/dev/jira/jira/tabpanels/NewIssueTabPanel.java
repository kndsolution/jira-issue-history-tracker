package dev.jira.jira.tabpanels;
import com.atlassian.jira.avatar.AvatarService;
import com.atlassian.jira.bc.user.search.UserSearchParams;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.changehistory.ChangeHistory;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.SearchableField;
import com.atlassian.jira.user.util.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.issue.tabpanels.GenericMessageAction;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.VelocityParamFactory;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.velocity.VelocityManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.renderer.JiraRendererPlugin;
import com.atlassian.jira.bc.user.search.UserSearchService;

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
    @JiraImport
    private final UserSearchService userSearchService;
    @JiraImport
    private final FieldManager fieldManager;
    @JiraImport
    private final AvatarService avatarService;

    public NewIssueTabPanel(VelocityManager velocityManager
            , VelocityParamFactory velocityParamFactory
            , ApplicationProperties applicationProperties
            , JiraAuthenticationContext jiraAuthenticationContext
            , RendererManager rendererManager
            , ChangeHistoryManager changeHistoryManager
            , UserManager userManager
            , UserSearchService userSearchService
            , FieldManager fieldManager
            , AvatarService avatarService
    ){
        this.velocityManager = velocityManager;
        this.velocityParamFactory = velocityParamFactory;
        this.applicationProperties = applicationProperties;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.rendererManager = rendererManager;
        this.changeHistoryManager = changeHistoryManager;
        this.userManager = userManager;
        this.userSearchService = userSearchService;
        this.fieldManager = fieldManager;
        this.avatarService = avatarService;
    }
    public List getActions(Issue issue, ApplicationUser remoteUser) {
        String webworkEncoding = this.applicationProperties.getString("webwork.i18n.encoding");
        Map<String, Object> context = this.velocityParamFactory.getDefaultVelocityParams();
        context.put("i18n", this.jiraAuthenticationContext.getI18nHelper());
        JiraRendererPlugin renderer = rendererManager.getRendererForType("atlassian-wiki-renderer");
        List<ChangeHistory> changeHistories = changeHistoryManager.getChangeHistories(issue);
        IssueTabPanelUtil issueTabPanelUtil = new IssueTabPanelUtil(userManager, avatarService);


        context.put("changeHistoryManager", changeHistoryManager);
        context.put("issueTabPanelUtil", issueTabPanelUtil);
        context.put("changeHistories",changeHistories);
        context.put("renderer",renderer);
        context.put("this",this);

        String renderedText = this.velocityManager.getEncodedBody("templates/tabpanels/", "new-issue-tab-panel.vm", webworkEncoding, context);
        return Collections.singletonList(new GenericMessageAction(renderedText));
    }
    public boolean showPanel(Issue issue, ApplicationUser remoteUser)
    {
        return true;
    }

    public List<ApplicationUser> findUsers(String search){
        UserSearchParams userSearchParams = new UserSearchParams.Builder().allowEmptyQuery(true).includeActive(true).includeInactive(true).limitResults(10).build();
        List<ApplicationUser> userList = userSearchService.findUsers("", userSearchParams);
        return  userList;
    }

    public Set<SearchableField> findFields(){
        Set<SearchableField> fieldList = this.fieldManager.getAllSearchableFields();
        return  fieldList;
    }


}