package pet.articles.test.tool.extension

import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.web.servlet.MvcResult

fun <T> MvcResult.extract(jsonMapper: JacksonTester<T>): T {
    val content: String = response.contentAsString
    return jsonMapper.parseObject(content)
}