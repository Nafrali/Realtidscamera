Realtidscamera  
==============
Detta är vårt Realtidskamera system.

Ladda ner projektet genom att trycka på "zip" i högra hörnet.

Användarmanual:

1. För att lägga till en kamera välj från dropdown menyn "Menu" alternativet "Add camera".
2. Skriv in de efterfrågade uppgifterna från "Add camera" valet.
3. För att ta bort en kamera. Välj i samma meny "Remove Camera" och skriv in nummret på kameran som ska tas bort.

Installation:

1. Start av proxykamera enl. EDA040 hemsidas anvisningar.\n
2. Starta terminalen.
3. Navigera till mappen "jars" som innehåller de körbara .jar filerna.
4. Starta de båda jarfilerna i valfri ordning
(lägg märke till att det behövs en server.jar körandes för varje kamera)
Servern startas genom "java -jar Server.jar [client port] [camera address] [server port]".
Öppna ett nytt terminalfönster och starta clienten genom "java -jar Client.jar".
5. Server.jar:  
Starta server.jar genom "java -jar server.jar args[0] args[1] args[2]"
i terminalen. Där args[0] är serverporten, args[1] är kameraaddressen,
args[2] är klientporten
6. Client.jar:  
Starta client.jar genom "java -jar client.jar".


Contributors: Kasper Bratz, Oskar Holmberg, Erik Munkby, Niklas Karlsson
