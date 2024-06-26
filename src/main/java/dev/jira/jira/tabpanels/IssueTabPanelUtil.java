package dev.jira.jira.tabpanels;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class IssueTabPanelUtil {


    private final UserManager userManager;

    public IssueTabPanelUtil(UserManager userManager
    ){
        this.userManager = userManager;
    }


    public String getUserWikiRender(String userKey){
        ApplicationUser user = userManager.getUserByKey(userKey);
        if(user == null){
            return null;
        }

        String userName = user.getName();

        return userName;
    }

    public String convertDate(Timestamp timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        return sdf.format(new Date(timestamp.getTime()));
    }
}
