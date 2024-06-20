package dev.jira.jira.tabpanels;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;


public class GetUserWiki {


    private final UserManager userManager;

    public GetUserWiki(UserManager userManager
    ){
        this.userManager = userManager;
    }


    public String getUserWikiRender(String userKey){
        ApplicationUser user = userManager.getUserByKey(userKey);
        if(user == null){
            return null;
        }

        String userName = user.getName();
        userName = "[~" +userName+ "]";
        return userName;
    }
}
