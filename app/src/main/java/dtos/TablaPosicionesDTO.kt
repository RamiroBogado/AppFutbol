package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TablaPosicionesDTO(
    val standings: List<Standing>
)