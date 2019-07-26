Flight application

Vert.x based application with the base functionality to book the flight.

To start the project type : mvn clean compile vertx:run

Sevices:

1 ticket check for id = 12
http://localhost:8080/ticket12 

2 baggage check for baggage id = 12 and dest id = 12
http://localhost:8080/baggage11/dest12

3 coupon booking for id = 11 and money = 3000
http://localhost:8080/coupon11/price3000

cache has been implemented in the fastest way but only for tickets.