package view

import model.Field
import model.FieldEvent
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingUtilities


private val BG_REGULAR = Color(184, 184, 184)
private val BG_MARKED = Color(8, 179, 247)
private val BG_EXPLOSION = Color(189, 66, 68)
private val BG_GREEN = Color(0, 100, 0)


class FieldButton(private val field: Field) : JButton() {

    init {
        font = font.deriveFont(Font.BOLD)
        background = BG_REGULAR
        isOpaque = true
        border = BorderFactory.createBevelBorder(0)
        addMouseListener(MouseClickListener(field, {it.open()}, {it.changeMarker() }))

        field.onEvent(this::applyStyle)
    }

    private fun applyStyle(field: Field, event: FieldEvent){
        when(event){
            FieldEvent.EXPLODE -> applyExplodeStyle()
            FieldEvent.OPEN -> applyOpenStyle()
            FieldEvent.MARK -> applyMarkStyle()
            else -> applyDefaultStyle()
        }

        SwingUtilities.invokeLater {
            repaint()
            validate()
        }
    }

    private fun applyExplodeStyle(){
        background = BG_EXPLOSION
        text = "X"
    }

    private fun applyOpenStyle(){
        background = BG_REGULAR
        border = BorderFactory.createLineBorder(Color.GRAY)

        foreground = when(field.minedNeighboursQuantity){
            1 -> BG_GREEN
            2 -> Color.BLUE
            3 -> Color.YELLOW
            4, 5, 6 -> Color.RED
            else -> Color.PINK
        }

        text = if(field.minedNeighboursQuantity > 0) field.minedNeighboursQuantity.toString() else " "
    }

    private fun applyMarkStyle(){
        background = BG_MARKED
        foreground = Color.BLACK
        text = "M"
    }

    private fun applyDefaultStyle(){
        background = BG_REGULAR
        border = BorderFactory.createBevelBorder(0)
        text = ""
    }

}