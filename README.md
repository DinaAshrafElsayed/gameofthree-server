##Goal

The Goal is to implement a game with two independent units – the players –
communicating with each other using an API.

##Description
When a player starts, it incepts a random (whole) number and sends it to the second
player as an approach of starting the game. The receiving player can now always choose
between adding one of {-1, 0, 1} to get to a number that is divisible by 3. Divide it by three. The
resulting whole number is then sent back to the original sender.
The same rules are applied until one player reaches the number 1(after the division).
See example below.

##Solution
The game is implemented as a server client system, where the server is a java application that provides endpoints for 
- Registering a new player
- Unregistering a player
- Get list of available players (that are not currently playing)
- Send invitation to start a new game
- Make a move (Play in your turn)

And the client is a java web application that starts with registering the player, sending its own IP and port 
to the server (for communication) and of course provides an endpoint for accepting an invitation and updates with new moves

Both server and client are packaged as maven applications which can easily be built and run using the following commands

`mvn clean install`

`mvn spring-boot:run`


##Limitation
The client can not be run on Mobile Data Package, because the ip will keep changing. This can be 
fixed by periodic checks and re-registering, but unfortunately didn't have the time to implement it.