package libetal.kotlin.compose.narrator

/** NOTE
 * State narratives can't prevent
 * navigation flow as their states aren't monitored
 * i.e if a state changes and we'd wan't to prevent flow
 * what value does the state take and how do we map it to
 * an event call?
 *
 * Also Be carefull how you addOnExitRequests as the state needs
 * to be well placed in the data flow of the application else your gate
 * won't work
 **/
class StateNarrativeScope : NarrativeScope(){

}