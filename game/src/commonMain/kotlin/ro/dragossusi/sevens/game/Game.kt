package ro.dragossusi.sevens.game

import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.sevens.payload.Card

interface Game {
    fun chooseCard(session: RoomPlayer, card: Card): Boolean
    fun drawCard(session: RoomPlayer): Boolean
    fun selectCardType(session: RoomPlayer, type: Card.Type)
    fun endTurn(session: RoomPlayer)
}