package customRiadaLibraries.jiraShortcuts.examples.configInventory.classes

import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.fields.CustomField

class FieldConfig implements Configuration{


    CustomField customFieldObject


    FieldConfig(CustomField customField) {

        this.customFieldObject = customField

    }

    ArrayList<Issue>getAllFieldIssues() {
        return js.jql(this.jql)
    }

    ArrayList<FieldContextConfig>getFieldContexts() {
        this.customFieldObject.configurationSchemes.collect {new FieldContextConfig(this.customFieldObject,it)}
    }

    String getJql() {
        return "cf[${this.customFieldObject.idAsLong}] is not empty"
    }

    Map toMap() {

        Map resultMap = [
                Name: customFieldObject.name,
                Type: customFieldObject.customFieldType.name,
                Id: customFieldObject.idAsLong,
                Description: customFieldObject.description,
                Jql:this.jql,
                Issues:this.issues,
                "Field Contexts":fieldContexts.collect {it.toMap()}
        ]

        resultMap.put("Number of issues",resultMap.get("Issues").size())

        return resultMap

    }

}
