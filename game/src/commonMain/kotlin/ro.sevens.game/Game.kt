package ro.sevens.game

import ro.sevens.game.session.PlayerSession
import ro.sevens.payload.Card

interface Game {
    fun chooseCard(session: PlayerSession, card: Card): Boolean
    fun drawCard(session: PlayerSession): Boolean
    fun selectCardType(session: PlayerSession, type: Card.Type)
    fun endTurn(session: PlayerSession)
}