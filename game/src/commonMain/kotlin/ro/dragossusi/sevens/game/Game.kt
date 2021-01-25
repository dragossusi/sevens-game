package ro.dragossusi.sevens.game

import ro.dragossusi.sevens.game.session.PlayerSession
import ro.dragossusi.sevens.payload.Card

interface Game {
    fun chooseCard(session: PlayerSession, card: Card): Boolean
    fun drawCard(session: PlayerSession): Boolean
    fun selectCardType(session: PlayerSession, type: Card.Type)
    fun endTurn(session: PlayerSession)
}