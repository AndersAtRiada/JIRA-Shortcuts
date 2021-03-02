package customRiadaLibraries.jiraShortcuts.examples.configInventory
/**
 * Current status 20210302
 * A fairly decent JSON is produced showin field and field context.
 * Some basic information about hor many issues the fields are used in is returned.
 * I was planning to also figure out when the field/context was last used in an issue.
 *
 * The JSON is intended to be friendly for Insight but this has not been verified.
 */

import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.issuetype.IssueType
import com.atlassian.jira.project.Project
import customRiadaLibraries.jiraShortcuts.examples.configInventory.classes.Configuration
import customRiadaLibraries.jiraShortcuts.examples.configInventory.classes.FieldConfig
import groovy.json.JsonGenerator
import groovy.json.JsonOutput
import org.apache.log4j.Level
import org.apache.log4j.Logger

CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()
Logger log = Logger.getLogger("ConfigInventory")
log.setLevel(Level.ALL)

ArrayList<CustomField> allFields = customFieldManager.getCustomFieldObjects()

JsonGenerator.Options options = new JsonGenerator.Options()
options.addConverter(Issue, { Issue issue -> issue.key })
options.addConverter(Project, { Project project -> [Id: project.id, Name: project.name] })
options.addConverter(IssueType, { IssueType issueType ->
    if (issueType != null) {
        [Id: issueType.id, Name: issueType.name]
    }

}
)

ArrayList<FieldConfig> fieldConfigs = []

allFields.findAll { [10700 as long, 11003 as long].contains(it.idAsLong) }.each { customField ->

    FieldConfig fieldConfig = new FieldConfig(customField)

    fieldConfigs.add(fieldConfig)


}

//ArrayList<Map<String, ArrayList<FieldConfig>>> allConfigMaps = [
ArrayList allConfigMaps = [
        [
                Field: fieldConfigs.collect {it.toMap()}.flatten()
        ]
]

//log.debug(JsonOutput.prettyPrint(options.build().toJson(allConfig)))
log.debug(JsonOutput.prettyPrint(options.build().toJson(allConfigMaps)))
//log.debug(JsonOutput.prettyPrint(options.build().toJson(fieldConfigs.collect { it.toMap() })))

/*

FieldConfig fieldConfig =  fieldConfigs.find {it.customFieldObject.idAsLong == 11003 as long}

log.debug(fieldConfig.fieldContexts.jql.join("\n"))

def field = allConfigMaps.Field.first().find {it.Id == 11003 as long}
log.debug("field" + field)
log.debug("field" + field.valuegetClass())

//log.debug(allConfigMaps.Field)

 */

Map testField = allConfigMaps.Field.first().last()

log.debug("testField" + JsonOutput.prettyPrint(options.build().toJson(testField)))
log.debug("testField Contexts" + testField."Field Contexts".first().Jql)