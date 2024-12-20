package dev.jira.jira.tabpanels;

import com.atlassian.jira.avatar.AvatarService;
import com.atlassian.jira.bc.user.search.UserSearchParams;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.changehistory.ChangeHistory;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.SearchableField;
import com.atlassian.jira.issue.fields.renderer.JiraRendererPlugin;
import com.atlassian.jira.issue.tabpanels.GenericMessageAction;
import com.atlassian.jira.plugin.issuetabpanel.*;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.util.VelocityParamFactory;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.velocity.VelocityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.atlassian.jira.plugin.issuetabpanel.GetActionsRequest.FetchMode.*;

public class PaginatedIssueTabCustom implements PaginatedIssueTabPanel {
    IssueTabPanelModuleDescriptor descriptor;

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


    public PaginatedIssueTabCustom(VelocityManager velocityManager
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

    @Override
    public void init(IssueTabPanelModuleDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public boolean showPanel(ShowPanelRequest request) {
        return true;
    }

    @Override
    public Page<IssueAction> getActions(GetActionsRequest request) {

        String webworkEncoding = this.applicationProperties.getString("webwork.i18n.encoding");
        Map<String, Object> context = this.velocityParamFactory.getDefaultVelocityParams();
        context.put("i18n", this.jiraAuthenticationContext.getI18nHelper());
        JiraRendererPlugin renderer = rendererManager.getRendererForType("atlassian-wiki-renderer");
        List<ChangeHistory> changeHistories = changeHistoryManager.getChangeHistories(request.issue());
        IssueTabPanelUtil issueTabPanelUtil = new IssueTabPanelUtil(userManager, avatarService);

        context.put("changeHistoryManager", changeHistoryManager);
        context.put("issueTabPanelUtil", issueTabPanelUtil);
        context.put("changeHistories",changeHistories);
        context.put("renderer",renderer);
        context.put("this",this);

        String renderedText = this.velocityManager.getEncodedBody("templates/tabpanels/", "new-issue-tab-panel.vm", webworkEncoding, context);

        return new Page<IssueAction>() {
            @Override
            public boolean isFirstPage() {
                return true;
            }

            @Override
            public boolean isLastPage() {
                return true;
            }

            @Override
            public List<IssueAction> getPageContents() {

                return Collections.singletonList(new GenericMessageAction(renderedText));
            }
        };
    }

    @Override
    public String 	getPersistentMarkup(GetActionsRequest request) {
        return "";
    }


    @Override
    public boolean paginationSupported() {
        return true;
    }


    @Override
    public String getHeader(GetActionsRequest request) {
        return "";
    }

    public List<ApplicationUser> findUsers(String search){
        UserSearchParams userSearchParams = new UserSearchParams.Builder().allowEmptyQuery(true).includeActive(true).includeInactive(true).build();
        List<ApplicationUser> userList = userSearchService.findUsers("", userSearchParams);
        return  userList;
    }

    public Set<SearchableField> findFields(){
        Set<SearchableField> fieldList = this.fieldManager.getAllSearchableFields();
        return  fieldList;
    }

    public Set<String> getHistoryUsers(List<ChangeHistory> changeHistories){
        Set<String> historyUsers = new HashSet<>();
        

        for (ChangeHistory changeHistory:changeHistories){
            String historyUser = changeHistory.getAuthorKey();
            log.error(historyUser);
            historyUsers.add(historyUser);
        }

        return historyUsers;
    }

    public Set<String> getHistoryFields(List<ChangeHistory> changeHistories){
        Set<String> historyFields = new HashSet<>();


        for (ChangeHistory changeHistory:changeHistories){
            List<org.ofbiz.core.entity.GenericValue> histories = changeHistory.getChangeItems();
            for (org.ofbiz.core.entity.GenericValue historyField:histories){
                historyFields.add(String.valueOf(historyField.get("field")));
            }

        }

        return historyFields;
    }
}

