# EventBus issues

The event bus currently uses anonymous functions as handlers. The implicit conversion of the 
functions to handlers is causing "unregisterHandler" not to work, as a new Handler[Message[X]] is 
created from the anonymous function every time. Therefore we need to provide a registerHandler 
method that takes a Handler[Message[X]] itself, if a user wants to unregister this handler later.

Constructing a java.core.Handler[java.core.eventbus.Message[X]] is not really easy to do in Scala 
thus we should provide another function, that helps users create this.

def convertMessageFunctionToHandler(handler: Message[X] => Unit): Handler[Message[X]