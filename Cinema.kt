package cinema

import java.text.DecimalFormat

data class RoomConfig(val rows: Int, val seatsPerRow: Int)
data class SeatPosition(val row: Int, val column: Int)
const val REGULAR_PRICE = 10
const val LOW_PRICE = 8

class CinemaRoom(private val roomConfig: RoomConfig){
    private val reservedSeats = mutableListOf<SeatPosition>()
    private val capacity = roomConfig.rows * roomConfig.seatsPerRow
    private val maxProfit = maxProfit()
    private val decFormat = DecimalFormat("#,##0.00")

    private fun maxProfit():Int {
        return if(capacity <= 60){
            capacity * 10
        }else{
            val expensiveTickets = (roomConfig.rows / 2) * roomConfig.seatsPerRow * 10
            val cheapsTickets = (roomConfig.rows - roomConfig.rows / 2) * roomConfig.seatsPerRow * 8
            expensiveTickets + cheapsTickets
        }
    }

    private fun calcTicketPrice(row:Int): Int{
        return if(capacity <= 60){
            REGULAR_PRICE
        }else{
            val expensiveRows = (roomConfig.rows / 2)
            if(row > expensiveRows) LOW_PRICE else REGULAR_PRICE
        }
    }

    fun printRooms(){
        print("Cinema:\n ")
        (1..roomConfig.seatsPerRow).forEach {columnNumber -> print(" $columnNumber") }
        println()
        for(currentRow in 1..roomConfig.rows) {
            print(currentRow)
            for(currentSeat in 1..roomConfig.seatsPerRow) { print(" ${if(SeatPosition(currentRow,currentSeat) in reservedSeats)"B" else "S" }") }
            println()
        }
        println()
    }

    fun buyATicket() {
        fun askSelectedSeat():Pair<Int,Int>{
            println("Enter a row number:")
            val row = readLine()!!.toInt()
            println("Enter a seat number in that row:")
            val seat = readLine()!!.toInt()
            return Pair(row, seat)
        }

        fun bookSeat(seatPosition: SeatPosition){
            if(seatPosition in reservedSeats) throw Exception("That ticket has already been purchased!")
            val invalidPosition = seatPosition.row > roomConfig.rows || seatPosition.column > roomConfig.seatsPerRow
            if(invalidPosition) throw Exception("Wrong input!")
            reservedSeats.add(seatPosition)
        }

        var validatedSeatPosition = false
        var seatPosition:SeatPosition
        do{
            val (row, column) = askSelectedSeat()
            seatPosition = SeatPosition(row,column)
            try {
                bookSeat(seatPosition)
                validatedSeatPosition = true
            } catch (e: Exception) {
                println(e.toString())
            }
        }while (!validatedSeatPosition)
        println("Ticket price: $${calcTicketPrice(seatPosition.row)}")
    }

    fun statistics() {
        val profit = reservedSeats.sumOf { calcTicketPrice(it.row) }
        println("""
            Number of purchased tickets: ${reservedSeats.size}
            Percentage: ${decFormat.format(reservedSeats.size.toDouble() * 100/capacity.toDouble())}%
            Current income: $${profit}
            Total income: $$maxProfit
        """.trimIndent())
    }

    fun printMenu() {
        println("""
            
        1. Show the seats
        2. Buy a ticket
        3. Statistics
        0. Exit
    """.trimIndent())
    }
}

fun getRoomConfig():RoomConfig{
    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    return RoomConfig(rows,readLine()!!.toInt())
}

fun main() {
    val cinema = CinemaRoom(getRoomConfig())
    do {
        cinema.printMenu()
        var option = readLine()!!.toInt()
        when(option){
            1 -> cinema.printRooms()
            2 -> cinema.buyATicket()
            3 -> cinema.statistics()
            else -> continue
        }
    }while (option != 0)
}





