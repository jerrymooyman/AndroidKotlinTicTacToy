package com.mooyz.androidtictactoy

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    class Player(val id: Int, val colour: Int, val mark: String, var isActive: Boolean) {
        val picks = arrayListOf<Int>()
        lateinit var opponent:Player

        fun passTurn() {
            isActive = false
            opponent.acceptTurn()
        }

        fun acceptTurn() {
            isActive = true
        }

        fun addPick(cellId: Int) {
            // is there a way to avoid mutation?
            // i.e. create a new collection which includes the new item.
            picks.add(cellId)
        }

        fun acceptOpponent(otherPlayer:Player) {
            // is there a way i can assign this only once?
            opponent = otherPlayer
        }
    }

    // is there a const equivilant?
    val PLAYER_1 = 1
    val PLAYER_2 = 2

    var player1 = Player(PLAYER_1, Color.GREEN, "X", true)
    var player2 = Player(PLAYER_2, Color.BLUE, "O", false)

    val allCellNumbers = arrayListOf(1,2,3,4,5,6,7,8,9)

    var buttonMap = HashMap<Int, Button>()

    val ROW_1 = listOf(1,2,3)
    val ROW_2 = listOf(4,5,6)
    val ROW_3 = listOf(7,8,9)

    val COL_1 = listOf(1,4,7)
    val COL_2 = listOf(2,5,8)
    val COL_3 = listOf(3,6,9)

    val DIAG_1 = listOf(1,5,9)
    val DIAG_2 = listOf(3,5,7)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonMap = hashMapOf(
                1 to bu1,
                2 to bu2,
                3 to bu3,
                4 to bu4,
                5 to bu5,
                6 to bu6,
                7 to bu7,
                8 to bu8,
                9 to bu9
        )

        player1.acceptOpponent(player2)
        player2.acceptOpponent(player1)
    }

    fun buClick(view: View) {
        val buSelected = view as Button
        playGame(buSelected)
    }

    fun playGame(buSelected: Button) {
        val currentPlayer = if (player1.isActive) player1 else player2
        playerMove(buSelected, currentPlayer)

        if (hasWon(currentPlayer)) {
            Toast.makeText(this, "Player " + currentPlayer.id + " wins the game", Toast.LENGTH_LONG).show()
            endGame()
        } else if (getEmptyCells(player1.picks, player2.picks, allCellNumbers).isNotEmpty()) {
            currentPlayer.passTurn()
            if(player2.isActive) {
                autoPlay()
            }
        } else {
            Toast.makeText(this, "Its a draw", Toast.LENGTH_LONG).show()
        }
    }

    fun endGame() {
        buttonMap.mapValues { b -> b.value.isEnabled = false }
    }

    fun playerMove(buSelected: Button, player:Player) {
        // apply mutation to button... (yuk!)
        buSelected.text = player.mark
        buSelected.setBackgroundColor(player.colour)
        buSelected.isEnabled = false

        var cellId = buttonIdToCell(buSelected.id)
        player.addPick(cellId)
    }

    fun hasWon(player:Player): Boolean {
        return hasRun(player.picks, ROW_1) || hasRun(player.picks, ROW_2) || hasRun(player.picks, ROW_3)
                || hasRun(player.picks, COL_1) || hasRun(player.picks, COL_2) || hasRun(player.picks, COL_3)
                || hasRun(player.picks, DIAG_1) || hasRun(player.picks, DIAG_2)
    }

    fun hasRun(playerPicks: List<Int>, cellsForRun: List<Int>): Boolean {
        return playerPicks.containsAll(cellsForRun)
    }

    fun getEmptyCells(player1Picks:List<Int>, player2Picks:List<Int>, allCellNumbers:List<Int>):List<Int> {
        // this feels very verbose; is there a more succinct way of doing this?
        var selectedCells = ArrayList<Int>()
        selectedCells.addAll(player1Picks)
        selectedCells.addAll(player2Picks)

        val allCells = ArrayList<Int>()
        allCells.addAll(allCellNumbers)
        allCells.removeAll(selectedCells)

        val remainingEmptyCells = allCells

        return remainingEmptyCells
    }

    fun buttonIdToCell(buttonId:Int):Int {
        val s = buttonMap.filter { entry -> entry.value.id == buttonId }
        return if (s.size == 1) return s.keys.first() else throw Exception("Invalid buttonId: $buttonId")
    }

    fun cellIdToButton(cellId:Int):Button {
        val r = buttonMap[cellId]
        return if (r != null) return r else throw Exception("Invalid cellId: $cellId")
    }

    fun autoPlay() {
        val emptyCells = getEmptyCells(player1.picks, player2.picks, allCellNumbers)

        val randomIndex = Random().nextInt(emptyCells.size-0)+0
        val cellId = emptyCells[randomIndex]

        val buSelected = cellIdToButton(cellId)
        playGame(buSelected)
    }
}
