package view

import model.Table
import java.awt.GridLayout
import javax.swing.JPanel

class TablePanel(table : Table) : JPanel() {

    init {
        layout = GridLayout(table.qtLines, table.qtColumns)
        table.forEachField { field ->
            val button = FieldButton(field)
            add(button)
        }
    }

}
