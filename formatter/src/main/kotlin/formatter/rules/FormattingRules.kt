package formatter.rules

import org.yaml.snakeyaml.Yaml
import java.io.FileInputStream

interface FormattingRules<T> {
    fun applyRule(): T

    fun getConfigFileValue(
        ruleName: String,
        convert: (String) -> T,
        filePath: String,
    ): T {
        val input = FileInputStream(filePath)
        val yaml = Yaml()
        val data = yaml.load(input) as Map<String, Map<String, Any>>
        val rulesMap = data["rules"] ?: throw IllegalArgumentException("Invalid YAML content")

        val keyValue =
            when (ruleName) {
                "spaceBeforeColon" -> rulesMap["spaceBeforeColon"]
                "spaceAfterColon" -> rulesMap["spaceAfterColon"].toString()
                "spaceAroundAssignment" -> rulesMap["spaceAroundAssignment"].toString()
                "newlineBeforePrintln" -> rulesMap["newlineBeforePrintln"].toString()
                "nSpacesIndentationForIfStatement" -> rulesMap["nSpacesIndentationForIfStatement"].toString()
                else -> throw IllegalArgumentException("Invalid rule name")
            }
        return convert(keyValue.toString())
    }
}
