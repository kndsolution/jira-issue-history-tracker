package dev.jira.jira.tabpanels;

import com.atlassian.jira.avatar.Avatar;
import com.atlassian.jira.avatar.AvatarServiceImpl;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.avatar.AvatarService;

import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class IssueTabPanelUtil {

    private final UserManager userManager;
    private final AvatarService avatarService;

    public IssueTabPanelUtil(UserManager userManager, AvatarService avatarService
    ){
        this.userManager = userManager;
        this.avatarService = avatarService;
    }




    public String getUserWikiRender(String userKey){
        ApplicationUser user = userManager.getUserByKey(userKey);
        if(user == null){
            return null;
        }

        String userName = user.getName();

        return userName;
    }

    public String getSearchUserHTML(String userKey){
        ApplicationUser user = userManager.getUserByKey(userKey);

        Avatar avatar = getUserAvatar(userKey);
        String imgURL = "<span class=\"aui-avatar aui-avatar-small\"><span class=\"aui-avatar-inner\"><img class=\"aui-ss-entity-icon rounded\" src='http://localhost:2990/jira/secure/useravatar?size=small&avatarId="+avatar.getId()+"'></span></span>";
        return "<p>"+imgURL+user.getDisplayName()+" - "+user.getEmailAddress()+"</p>";
    }

    public String getSearchUserAvatarId(String userKey){
        ApplicationUser user = userManager.getUserByKey(userKey);

        Avatar avatar = getUserAvatar(userKey);
        String avatarId = String.valueOf(avatar.getId());
        return avatarId;
    }

    public Avatar getUserAvatar(String userKey){
        ApplicationUser user = userManager.getUserByKey(userKey);

        return avatarService.getAvatar(user, user);
    }


    public String convertDate(Timestamp timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        return sdf.format(new Date(timestamp.getTime()));
    }
}
