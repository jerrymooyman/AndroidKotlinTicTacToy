package com.mooyz.androidtictactoy

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val ROW1 = ArrayList<Int>()
    val ROW2 = ArrayList<Int>()
    val ROW3 = ArrayList<Int>()

    val COL1 = ArrayList<Int>()
    val COL2 = ArrayList<Int>()
    val COL3 = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ROW1.addAll(listOf(1,2,3))
        ROW2.addAll(listOf(4,5,6))
        ROW3.addAll(listOf(7,8,9))

        COL1.addAll(listOf(1,4,7))
        COL2.addAll(listOf(2,5,8))
        COL3.addAll(listOf(3,6,9))
    }

    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var ActivePlayer = 1

    fun playGame(cellId: Int, buSelected: Button) {
        var winningPlayer = -1
        if (ActivePlayer == 1) {
            playerMove(cellId, buSelected, player1, Color.GREEN, "X")
            winningPlayer = if (checkWinner(player1)) 1 else -1
            ActivePlayer = 2
        } else {
            playerMove(cellId, buSelected, player2, Color.BLUE, "O")
            winningPlayer = if (checkWinner(player2)) 2 else -1
            ActivePlayer = 1
        }

        buSelected.isEnabled = false

        if (winningPlayer != -1) {
            Toast.makeText(this, "Player $winningPlayer wins the game", Toast.LENGTH_LONG).show()
        }
    }

    fun playerMove(cellId: Int, buSelected: Button, playerPicks: ArrayList<Int>, color: Int, playerMark: String) {
        buSelected.text = playerMark
        buSelected.setBackgroundColor(color)
        playerPicks.add(cellId)
    }

    fun buClick(view: View) {
        val buSelected = view as Button
        var cellId = 0
        when (buSelected.id) {
            R.id.bu1 -> cellId = 1
            R.id.bu2 -> cellId = 2
            R.id.bu3 -> cellId = 3
            R.id.bu4 -> cellId = 4
            R.id.bu5 -> cellId = 5
            R.id.bu6 -> cellId = 6
            R.id.bu7 -> cellId = 7
            R.id.bu7 -> cellId = 8
            R.id.bu9 -> cellId = 9
        }

        playGame(cellId, buSelected)
    }

    fun checkWinner(playerPicks: ArrayList<Int>): Boolean {
        return hasRow(playerPicks, ROW1) || hasRow(playerPicks, ROW2) || hasRow(playerPicks, ROW3)
                || hasCol(playerPicks, COL1) || hasCol(playerPicks, COL2) || hasCol(playerPicks, COL3)
    }

    fun hasRow(playerPicks: ArrayList<Int>, rowCells: ArrayList<Int>): Boolean {
        val r = playerPicks.containsAll(rowCells)
        return r
    }

    fun hasCol(playerPicks: ArrayList<Int>, colCells: ArrayList<Int>): Boolean {
        val r = playerPicks.containsAll(colCells)
        return r
    }
}
