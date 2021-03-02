package customRiadaLibraries.jiraShortcuts.examples.configInventory.classes

import com.atlassian.jira.issue.Issue
import customRiadaLibraries.jiraShortcuts.JiraShortcuts

trait Configuration {

    JiraShortcuts js = new JiraShortcuts()

    abstract String getJql()
    abstract Map toMap()


    ArrayList<Issue>getIssues() {
        js.jql(this.jql)
    }

}