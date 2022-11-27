package co.kr.cracker_android.domain.entity

data class Location(
    val longitude: String,
    val latitude: String
) {
    override fun toString(): String {
        return "{\"longitude\":\"$longitude\", \"latitude\":\"$latitude\"}"
    }
}
