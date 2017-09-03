package com.mooyz.androidtictactoy

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.function.BiConsumer
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    val gameOver = false
    val NO_PLAYER = -1
    val PLAYER_1 = 1
    val PLAYER_2 = 2

    val allCellNumbers = listOf<Int>(1,2,3,4,5,6,7,8,9)

    var buttonIdMap = HashMap<Int, Int>()
    var buttonMap = HashMap<Int, Button>()
    val ROW_1 = ArrayList<Int>()
    val ROW_2 = ArrayList<Int>()
    val ROW_3 = ArrayList<Int>()

    val COL_1 = ArrayList<Int>()
    val COL_2 = ArrayList<Int>()
    val COL_3 = ArrayList<Int>()

    val DIAG_1 = ArrayList<Int>()
    val DIAG_2 = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init local vars
        buttonIdMap[R.id.bu1] = 1
        buttonIdMap[R.id.bu2] = 2
        buttonIdMap[R.id.bu3] = 3
        buttonIdMap[R.id.bu4] = 4
        buttonIdMap[R.id.bu5] = 5
        buttonIdMap[R.id.bu6] = 6
        buttonIdMap[R.id.bu7] = 7
        buttonIdMap[R.id.bu8] = 8
        buttonIdMap[R.id.bu9] = 9

        buttonMap[1] = bu1
        buttonMap[2] = bu2
        buttonMap[3] = bu3
        buttonMap[4] = bu4
        buttonMap[5] = bu5
        buttonMap[6] = bu6
        buttonMap[7] = bu7
        buttonMap[8] = bu8
        buttonMap[9] = bu9

        ROW_1.addAll(listOf(1,2,3))
        ROW_2.addAll(listOf(4,5,6))
        ROW_3.addAll(listOf(7,8,9))

        COL_1.addAll(listOf(1,4,7))
        COL_2.addAll(listOf(2,5,8))
        COL_3.addAll(listOf(3,6,9))

        DIAG_1.addAll(listOf(1,5,9))
        DIAG_2.addAll(listOf(3,5,7))
    }

    var player1Picks = ArrayList<Int>()
    var player2Picks = ArrayList<Int>()
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
            if (!getEmptyCells(player1Picks, player2Picks).isEmpty()) {
                ActivePlayer = PLAYER_2
            }
        } else {
            playerMove(cellId, buSelected, player2Picks, Color.BLUE, "O")
            winningPlayer = if (checkWinner(player2Picks)) PLAYER_2 else NO_PLAYER
            ActivePlayer = PLAYER_1
        }

        buSelected.isEnabled = false

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
        playerPicks.add(cellId)
    }

    fun checkWinner(playerPicks: ArrayList<Int>): Boolean {
        return hasRun(playerPicks, ROW_1) || hasRun(playerPicks, ROW_2) || hasRun(playerPicks, ROW_3)
                || hasRun(playerPicks, COL_1) || hasRun(playerPicks, COL_2) || hasRun(playerPicks, COL_3)
                || hasRun(playerPicks, DIAG_1) || hasRun(playerPicks, DIAG_2)
    }

    fun hasRun(playerPicks: ArrayList<Int>, cellsForRun: ArrayList<Int>): Boolean {
        return playerPicks.containsAll(cellsForRun)
    }

    fun getEmptyCells(player1Picks:ArrayList<Int>, player2Picks:ArrayList<Int>):ArrayList<Int> {
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
        val r = buttonIdMap[buttonId]
        return if (r != null) return r else throw Exception("Invalid buttonId: $buttonId")
    }

    fun cellIdToButton(cellId:Int):Button {
        val r = buttonMap[cellId]
        return if (r != null) return r else throw Exception("Invalid cellId: $cellId")
    }

    fun autoPlay() {
        val emptyCells = getEmptyCells(player1Picks, player2Picks)

        val randomIndex = Random().nextInt(emptyCells.size-0)+0
        val cellId = emptyCells[randomIndex]

        val buSelected = cellIdToButton(cellId)
        playGame(cellId, buSelected)
    }
}
