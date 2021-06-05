import java.util.*
import java.io.*
import java.math.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args : Array<String>) {
    //keeps the all zones
    var zones: MutableMap<Int, Zone> = HashMap()
    // keeps the zone that I moved to 
    var zoneDestination: MutableMap<Int, List<Zone>> = HashMap()
    val podsCount = arrayListOf<Int>()
    val input = Scanner(System.`in`)
    val playerCount = input.nextInt() // the amount of players (always 2)
    val myId = input.nextInt() // my player ID (0 or 1)
    val zoneCount = input.nextInt() // the amount of zones on the map
    val linkCount = input.nextInt() // the amount of links between all zones

    //Stores the zones in hashmap where the key is zoneId and the value is zone object
    for (i in 0 until zoneCount) {
        val zoneId = input.nextInt() // this zone's ID (between 0 and zoneCount-1)
        val platinumSource = input.nextInt() // Because of the fog, will always be 0
        val zone = Zone(zoneId)
        zones.put(zoneId, zone)
        
    }
    //updates the zones in the hashmap by adding their neighbours
    for (i in 0 until linkCount) {
        val zone1 = input.nextInt()
        val zone2 = input.nextInt()
        val firstZone : Zone? = zones.get(zone1)
        val secondZone : Zone? = zones.get(zone2)
        firstZone?.neighbors?.add(secondZone!!)
        secondZone?.neighbors?.add(firstZone!!)
        zones.replace(zone1, firstZone!!);
        zones.replace(zone2, secondZone!!);
    }
    
    // game loop
    var turn = 0
    while (true) {
        //stores the zones which I have Pods on. Updates every new loop
        val zoneOrigin = arrayListOf<Zone>()
        val myPlatinum = input.nextInt() // your available Platinum
        for (i in 0 until zoneCount) {
            val zId = input.nextInt() // this zone's ID
            val ownerId = input.nextInt() // the player who owns this zone (-1 otherwise)
            val podsP0 = input.nextInt() // player 0's PODs on this zone
            val podsP1 = input.nextInt() // player 1's PODs on this zone
            val visible = input.nextInt() // 1 if one of your units can see this tile, else 0
            val platinum = input.nextInt() // the amount of Platinum this zone can provide (0 if hidden by fog)
            val zone = zones.get(zId)
            zone!!.platinum = platinum
            zone!!.ownerId = ownerId
            if(visible == 1) zone!!.visible = true
            if ((myId == 0 && podsP0 > 0) || (myId == 1 && podsP1 > 0)){
                if(myId==0){
                	zone!!.myPods=podsP0;
                	zone!!.enemyPods=podsP1
                }else{
                	zone!!.myPods=podsP1;
                	zone!!.enemyPods=podsP0
                }
                zoneOrigin.add(zone)
                
            }
        
            zoneDestination.put(i, zone.neighbors)
        }
            
            //deciding which neighbor my Pods are going to move
            for(j in 0 until zoneOrigin.size){
                 val currentZone : Zone? = zones.get(zoneOrigin.get(j).zoneId)
                 val currentZone_neighbours : List<Zone>? = zoneDestination.get(zoneOrigin.get(j).zoneId)
                 // if there is enemy zone I can move, I move that zone.If not, I move to neutral zone or move randomly
                 var index = checkZone(currentZone_neighbours,myId)
                 if(index != -1){
                        val nextZone : List<Zone> = listOf(currentZone!!.neighbors.get(index))
                        zoneDestination.put(zoneOrigin.get(j).zoneId, nextZone)
                 }
                 // If there is no enemy zone I can move, else will be called
                 else{
                 for(k in 0 until currentZone_neighbours!!.size){
                    if(currentZone_neighbours.get(k).ownerId != myId){
                         val nextZone : List<Zone> = listOf(currentZone!!.neighbors.get(k))
                         zoneDestination.put(zoneOrigin.get(j).zoneId, nextZone)
                         break
                     }
                    if(k == (currentZone_neighbours.size)-1){
                         val random = (0 until currentZone!!.neighbors.size).random()
                         val nextZone : List<Zone> = listOf(currentZone!!.neighbors.get(random))
                         zoneDestination.put(zoneOrigin.get(j).zoneId, nextZone)
                     }
                     
                    }
                 }
            }
        
            //Making the move by appropriate print
            for(k in 0 until zoneOrigin.size){
                //Separates the first Pods into 2 Pods
                if(turn == 0){
                    println("${zoneOrigin.get(k).myPods / 2} ${zoneOrigin.get(k).zoneId} ${zoneDestination.get(zoneOrigin.get(k).zoneId)?.get(0)?.zoneId} ");
                    continue
                }
                
                //Separates the Pods in to 2 Pods if the power higher and equal to 20
                if(zoneOrigin.get(k).myPods >= 20){
                    print(" ${zoneOrigin.get(k).myPods / 2}  ${zoneOrigin.get(k).zoneId}  ${zoneDestination.get(zoneOrigin.get(k).zoneId)?.get(0)?.zoneId} ");
                    
                }
                if (k == ((zoneOrigin.size) - 1)){
                println("${zoneOrigin.get(k).myPods} ${zoneOrigin.get(k).zoneId} ${zoneDestination.get(zoneOrigin.get(k).zoneId)?.get(0)?.zoneId} ");
                
                } else{
                print(" ${zoneOrigin.get(k).myPods}  ${zoneOrigin.get(k).zoneId}  ${zoneDestination.get(zoneOrigin.get(k).zoneId)?.get(0)?.zoneId} ");
                }
            }
            println("WAIT")

            
                
        

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");


        // first line for movement commands, second line no longer used (see the protocol in the statement for details)
        
        turn++

 }
}
// checks the neighbour zones.If one of them belongs to enemy,return that zone
fun checkZone(currentZone_neighbours : List<Zone>?,myId:Int):Int{
    for(i in 0 until currentZone_neighbours!!.size){
        if(currentZone_neighbours.get(i).ownerId != myId && currentZone_neighbours.get(i).ownerId != -1){
            return i
        }
    }
    return -1
}

//Model class for the hexagonal zones
class Zone(
    var zoneId:Int
){
    var ownerId:Int = -1
    var podsP0:Int = 0
    var podsP1:Int = 0
    var visible:Boolean = false
    var platinum:Int = 0
    val neighbors : ArrayList<Zone> = arrayListOf()
    var myPods:Int = 0
    var enemyPods:Int = 0
        init{
        this.zoneId = zoneId

    }
}