package model

enum class FieldEvent { OPEN, MARK, UNMARK, EXPLODE, RESTART }

data class Field (val line: Int, val column: Int){

    private val neighbours = ArrayList<Field>()
    private val callbacks = ArrayList<(Field, FieldEvent) -> Unit>()

    var marked: Boolean = false
    var open: Boolean = false
    var mined: Boolean = false


    //Read-only fields
    val unmarked: Boolean get() = !marked
    val closed: Boolean get() = !open
    val safe: Boolean get() = !mined
    val objectiveCompleted: Boolean get() = safe && open || mined && marked

    val minedNeighboursQuantity: Int get() = neighbours.filter {it.mined}.size

    val neighboursSafe: Boolean
    get() = neighbours.map{it.safe}.reduce { result, safe -> result && safe  }

    fun addNeighbour(n: Field){
        neighbours.add(n)
    }

    fun onEvent(callback: (Field, FieldEvent) -> Unit){
        callbacks.add(callback)
    }

    fun open(){
        if(closed){
            open = true
            if(mined){
                callbacks.forEach{ it(this, FieldEvent.EXPLODE) }
            } else {
                callbacks.forEach{ it(this, FieldEvent.OPEN) }
                neighbours.filter { it.closed && it.safe && neighboursSafe }.forEach { it.open() }
            }
        }
    }

    fun changeMarker(){
        if(closed){
            marked = !marked
            val event = if(marked) FieldEvent.MARK else FieldEvent.UNMARK
            callbacks.forEach { it(this, event) }
        }
    }

    fun mine(){
        mined = true
    }

    fun restart(){
        open = false
        mined = false
        marked = false
        callbacks.forEach { it(this, FieldEvent.RESTART) }
    }

}