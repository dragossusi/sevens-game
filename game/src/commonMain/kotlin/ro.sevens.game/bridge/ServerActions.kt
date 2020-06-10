package ro.sevens.game.bridge

interface ServerActions : ClientActions {
    suspend fun connect()

    fun disconnect()
}