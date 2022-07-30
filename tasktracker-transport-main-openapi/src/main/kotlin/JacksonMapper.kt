import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import ru.ac1d.api.v1.models.IRequest
import ru.ac1d.api.v1.models.IResponse

private val jacksonMapper = ObjectMapper().apply {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
}

fun responseSerialize(response: IResponse): String = jacksonMapper.writeValueAsString(response)
fun requestSerialize(request: IRequest): String = jacksonMapper.writeValueAsString(request)

fun jacksonSerialize(obj: Any): String =
    when (obj) {
        is IResponse -> responseSerialize(obj)
        is IRequest -> requestSerialize(obj)
        else -> throw IllegalArgumentException("Out of bounds the subject area")
    }

internal inline fun <reified T> jacksonDeserialize(json: String): T =
    jacksonMapper.readValue(json, T::class.java)
