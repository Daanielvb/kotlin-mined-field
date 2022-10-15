package model

import java.util.*
import kotlin.collections.ArrayList

enum class TableEvent { WIN, LOSE }

class Table(
    val qtLines: Int,
    val qtColumns: Int,
    var qtMines: Int
) {

    private val fields = ArrayList<ArrayList<Field>>()

    private val callbacks = ArrayList<(TableEvent) -> Unit>()

    init {
        generateFields()
        associateNeighbours()
        sortMines()
    }

    private fun generateFields(){
        for (line in 0 until qtLines){
            fields.add(ArrayList())
            for(col in 0 until qtColumns){
                val newField = Field(line, col)
                newField.onEvent(this::verifyWinOrLose)
                fields[line].add(newField)
            }
        }

    }

    private fun associateNeighbours(){
        forEachField { associateNeighbours(it) }
    }

    private fun associateNeighbours(field: Field){
        val (line, col) = field
        val lines = arrayOf(line - 1, line, line + 1)
        val cols = arrayOf(col - 1, col, col + 1)

        lines.forEach { l ->
            cols.forEach { c ->
                val current = fields.getOrNull(l)?.getOrNull(c)
                current?.takeIf { field != it }?.let {
                    field.addNeighbour(it)
                }
            }
        }
    }

    private fun sortMines(){
        val generator = Random()

        var sortedLine: Int
        var sortedCol: Int
        var sortedMines = 0

        while (sortedMines < qtMines){
            sortedLine = generator.nextInt(qtLines)
            sortedCol = generator.nextInt(qtColumns)

            val sortedField = fields[sortedLine][sortedCol]
            if(sortedField.safe){
                sortedField.mine()
                sortedMines++
            }
        }
    }

    private fun hasSucceeded(): Boolean {
        var won = true
        forEachField { if (!it.objectiveCompleted) won = false }
        return won
    }

    fun forEachField(callback: (Field) -> Unit){
        fields.forEach { line -> line.forEach(callback) }
    }

    private fun verifyWinOrLose(field: Field, event: FieldEvent){
        if (event == FieldEvent.EXPLODE){
            callbacks.forEach { it(TableEvent.LOSE) }
        } else if (hasSucceeded()){
            callbacks.forEach { it(TableEvent.WIN) }
        }
    }

    fun onEvent(callback: (TableEvent) -> Unit ){
        callbacks.add(callback)
    }

    fun restart(){
        forEachField { it.restart() }
        sortMines()
    }

}