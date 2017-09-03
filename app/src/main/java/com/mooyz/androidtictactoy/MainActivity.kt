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

    val NO_PLAYER = -1
    val PLAYER_1 = 1
    val PLAYER_2 = 2

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
    }

    var player1Picks = arrayListOf<Int>()
    var player2Picks = arrayListOf<Int>()
    var ActivePlayer = PLAYER_1

    fun buClick(view: View) {
        val buSelected = view as Button
        var cellId = buttonIdToCell(buSelected.id)

        playGame(cellId, buSelected)
    }

    fun playGame(cellId: Int, buSelected: Button) {
        var winningPlayer:Int
        if (ActivePlayer == PLAYER_1) {
            playerMove(cellId, buSelected, player1Picks, Color.GREEN, "X")
            winningPlayer = if (checkWinner(player1Picks)) PLAYER_1 else NO_PLAYER
            if (getEmptyCells(player1Picks, player2Picks, allCellNumbers).isNotEmpty()) {
                ActivePlayer = PLAYER_2
            }
        } else {
            playerMove(cellId, buSelected, player2Picks, Color.BLUE, "O")
            winningPlayer = if (checkWinner(player2Picks)) PLAYER_2 else NO_PLAYER
            ActivePlayer = PLAYER_1
        }

        if (winningPlayer != NO_PLAYER) {
            Toast.makeText(this, "Player $winningPlayer wins the game", Toast.LENGTH_LONG).show()
            endGame()
        } else if ( ActivePlayer == PLAYER_2) {
            autoPlay()
        }
    }

    fun endGame() {
        buttonMap.mapValues { b -> b.value.isEnabled = false }
    }

    fun playerMove(cellId: Int, buSelected: Button, playerPicks: ArrayList<Int>, color: Int, playerMark: String) {
        buSelected.text = playerMark
        buSelected.setBackgroundColor(color)
        buSelected.isEnabled = false
        playerPicks.add(cellId)
    }

    fun checkWinner(playerPicks: ArrayList<Int>): Boolean {
        return hasRun(playerPicks, ROW_1) || hasRun(playerPicks, ROW_2) || hasRun(playerPicks, ROW_3)
                || hasRun(playerPicks, COL_1) || hasRun(playerPicks, COL_2) || hasRun(playerPicks, COL_3)
                || hasRun(playerPicks, DIAG_1) || hasRun(playerPicks, DIAG_2)
    }

    fun hasRun(playerPicks: List<Int>, cellsForRun: List<Int>): Boolean {
        return playerPicks.containsAll(cellsForRun)
    }

    fun getEmptyCells(player1Picks:List<Int>, player2Picks:List<Int>, allCellNumbers:List<Int>):List<Int> {
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
        val emptyCells = getEmptyCells(player1Picks, player2Picks, allCellNumbers)

        val randomIndex = Random().nextInt(emptyCells.size-0)+0
        val cellId = emptyCells[randomIndex]

        val buSelected = cellIdToButton(cellId)
        playGame(cellId, buSelected)
    }
}
