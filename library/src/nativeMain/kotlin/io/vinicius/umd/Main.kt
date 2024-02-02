package io.vinicius.umd

suspend fun main() {
    val umd = Umd("https://www.coomer.su/onlyfans/user/atomicbrunette18")
    val response = umd.queryMedia()
    println(response)
}