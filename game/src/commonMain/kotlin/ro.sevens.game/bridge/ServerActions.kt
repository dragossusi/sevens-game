package ro.dragossusi.sevens.game.bridge

interface ServerActions : ClientActions {
    fun connect()

    fun disconnect()
}