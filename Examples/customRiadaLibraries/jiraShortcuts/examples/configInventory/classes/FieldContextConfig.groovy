package customRiadaLibraries.jiraShortcuts.examples.configInventory.classes

import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.fields.config.FieldConfigScheme
import com.atlassian.jira.issue.issuetype.IssueType
import com.atlassian.jira.project.Project

class FieldContextConfig implements Configuration{

    CustomField customFieldObject
    FieldConfigScheme fieldContext

    FieldContextConfig(CustomField customField, FieldConfigScheme fieldConfigScheme) {
        this.customFieldObject = customField
        this.fieldContext = fieldConfigScheme
    }

    ArrayList<Project>getProjects() {
        return this.fieldContext.associatedProjectObjects
    }

    ArrayList<IssueType>getIssueTypes() {
        return this.fieldContext.associatedIssueTypes
    }

    String getJql() {
        String contextJql = "cf[${customFieldObject.idAsLong}] is not empty"


        if (this.fieldContext.allProjects) {
            //Ignoring projects from other contexts
            ArrayList<String>projectKeysFromOtherContexts = customFieldObject.configurationSchemes.collect {it.associatedProjectObjects.collect {it.key}}.flatten()
            if (!projectKeysFromOtherContexts.isEmpty()){
                contextJql += " and project not in (${projectKeysFromOtherContexts.join(",")})"
            }

        }else {
            contextJql += " and project in (${this.fieldContext.associatedProjectObjects.collect {"\"${it.key}\""}.join(",")})"
            //contextJql += context.associatedProjectObjects.isEmpty() ? "" : " and project in (${context.associatedProjectObjects.collect {"\"${it.key}\""}.join(",")})"
        }

        if (this.fieldContext.allIssueTypes) {
            //Ignoring issuetypes from other contexts
            ArrayList<String>issueTypesFromOtherContexts = customFieldObject.configurationSchemes.collect {it.associatedIssueTypes.collect {it ? "\"${it?.name}\"" : null}}.flatten().findAll {it != null}
            if (!issueTypesFromOtherContexts.isEmpty()) {
                contextJql += " and issuetype not in (${issueTypesFromOtherContexts.join(",")})"
            }
        }else {

            contextJql += " and issuetype in (" + this.fieldContext.associatedIssueTypes.collect {"\"${it.name}\""}.join(",") + ")"

        }

        return contextJql
    }

    Map toMap() {

        Map resultMap = [
                Name:fieldContext.name,
                Description:fieldContext.description,
                Global:fieldContext.global,
                "All Projects" : fieldContext.allProjects,
                Projects:this.projects,
                //Projects:this.projects.collect {[key:it.key, id:it.id]},
                "All IssueTypes":fieldContext.allIssueTypes,
                Issuetypes:this.issueTypes.findAll{it != null},
                "Jql":this.jql,
                "Issues":this.issues,


        ]
        resultMap.put("Number of issues",resultMap.get("Issues").size())



        return resultMap

    }
}
