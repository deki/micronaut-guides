package example.micronaut

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FunctionRequestHandlerTest {

    @Test
    fun testHandler() {
        val handler = FunctionRequestHandler()
        val request = APIGatewayProxyRequestEvent()
        request.httpMethod = "GET"
        request.path = "/"
        val response = handler.execute(request)
        assertEquals(200, response.statusCode.toInt())
        assertEquals("{\"message\":\"Hello World\"}", response.body)
        handler.close()
    }
}
