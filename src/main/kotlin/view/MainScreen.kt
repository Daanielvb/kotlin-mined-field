package view

import model.Table
import model.TableEvent
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main() {
    MainScreen()
}


class MainScreen : JFrame(){
    
    private val table = Table(qtLines = 16, qtColumns= 30, qtMines= 60)
    private val tablePanel = TablePanel(table)

    init {
        table.onEvent(this::showResult)
        add(tablePanel)

        setSize(690, 438)

        //centralize
        setLocationRelativeTo(null)

        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Mined Field"
        isVisible = true
    }

    private fun showResult(event: TableEvent){
        SwingUtilities.invokeLater {
            val msg = when(event){
                TableEvent.WIN -> "You win"
                TableEvent.LOSE -> "You lose :p"
            }

            JOptionPane.showMessageDialog(this, msg)
            table.restart()
            tablePanel.repaint()
            tablePanel.validate()
        }
    }
}

